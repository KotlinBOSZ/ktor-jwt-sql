package me.szydelko.data.dao.user

interface UserService {
    suspend fun getUserByUsername(username: String): UserDAO?
    suspend fun insertUser(username: String, password: String, salt: String): UserDAO

}