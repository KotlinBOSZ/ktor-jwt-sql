package me.szydelko.security.token.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
