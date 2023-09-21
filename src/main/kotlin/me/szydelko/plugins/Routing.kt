package me.szydelko.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.szydelko.data.dao.user.UserService
import me.szydelko.routes.authenticate
import me.szydelko.routes.getSecretInfo
import me.szydelko.security.token.hashing.HashingService
import me.szydelko.routes.singIn
import me.szydelko.routes.singUp
import me.szydelko.security.token.TokenConfig
import me.szydelko.security.token.TokenService

fun Application.configureRouting(
    hashingService: HashingService,
    userService: UserService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        singUp(hashingService, userService)
        singIn(hashingService, userService, tokenService, tokenConfig)
        authenticate()
        getSecretInfo()
    }
}
