package me.szydelko.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)
data class TokenClaim(
    val name: String,
    val value: String
)