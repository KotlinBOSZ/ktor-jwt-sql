package me.szydelko

import io.ktor.server.application.*
import me.szydelko.data.dao.user.UserServiceImpl
import me.szydelko.plugins.*
import me.szydelko.security.token.JwtTokenService
import me.szydelko.security.token.TokenConfig
import me.szydelko.security.token.hashing.SHA256HashingService
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val database = configureDatabases()

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    val userService = UserServiceImpl()


    configureSecurity(tokenConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(hashingService, userService, tokenService, tokenConfig)

}
