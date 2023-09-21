package me.szydelko.data.dao.user

import me.szydelko.plugins.dbQuery

class UserServiceImpl : UserService {
    override suspend fun getUserByUsername(username: String): UserDAO? = dbQuery {
        UserDAO.find { Users.username eq username }.firstOrNull()
    }

    override suspend fun insertUser(username: String, password: String, salt: String): UserDAO = dbQuery {
        UserDAO.new {
            this.username = username
            this.password = password
            this.salt = salt
        }
    }

}
