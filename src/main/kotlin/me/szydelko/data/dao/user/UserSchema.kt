package me.szydelko.data.dao.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class UserDto(val username: String, val password: String)
object Users : IntIdTable() {
    val username = varchar("username", length = 50).uniqueIndex()
    val password = varchar("password", length = 255)
    val salt = varchar("salt", length = 255)
}

class UserDAO(id : EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)
    var username by Users.username
    var password by Users.password
    var salt by Users.salt

}


//    suspend fun create(user: ExposedUser): Int = dbQuery {
//        Users.insert {
//            it[name] = user.name
//            it[age] = user.age
//        }[Users.id]
//    }
//
//    suspend fun read(id: Int): ExposedUser? {
//        return dbQuery {
//            Users.select { Users.id eq id }
//                .map { ExposedUser(it[Users.name], it[Users.age]) }
//                .singleOrNull()
//        }
//    }
//
//    suspend fun update(id: Int, user: ExposedUser) {
//        dbQuery {
//            Users.update({ Users.id eq id }) {
//                it[name] = user.name
//                it[age] = user.age
//            }
//        }
//    }
//
//    suspend fun delete(id: Int) {
//        dbQuery {
//            Users.deleteWhere { Users.id.eq(id) }
//        }
//    }

