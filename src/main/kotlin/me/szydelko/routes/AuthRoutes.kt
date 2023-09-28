package me.szydelko.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.szydelko.data.dto.requests.AuthRequest
import me.szydelko.data.dao.user.UserService
import me.szydelko.data.dto.responses.AuthResponse
import me.szydelko.routes.authenticate
import me.szydelko.security.token.TokenClaim
import me.szydelko.security.token.TokenConfig
import me.szydelko.security.token.TokenService
import me.szydelko.security.token.hashing.HashingService
import me.szydelko.security.token.hashing.SaltedHash
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.singUp(
    hashingService: HashingService,
    userService: UserService
) {
    post("singup") {

        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8

        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        try {
            userService.insertUser(request.username, saltedHash.hash, saltedHash.salt)
        }catch (_: ExposedSQLException){
            call.respond(HttpStatusCode.Conflict)
        }
        call.respond(HttpStatusCode.Created)
    }
}

fun Route.singIn(
    hashingService: HashingService,
    userService: UserService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("singin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByUsername(request.username)
        if (user == null){
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
        }
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user!!.password,
                salt = user.salt
            )
        )
        if (!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token
            )
        )
    }
}

fun Route.authenticate(){
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo(){
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)
            call.respond(HttpStatusCode.OK,"Your UserId is $userId")
        }
    }
}

