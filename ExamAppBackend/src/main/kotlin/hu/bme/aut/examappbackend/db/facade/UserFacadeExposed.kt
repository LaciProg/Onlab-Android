package hu.bme.aut.examappbackend.db.facade

import hu.bme.aut.examappbackend.db.DatabaseFactory.dbQuery
import hu.bme.aut.examappbackend.db.model.UserDB
import hu.bme.aut.examappbackend.dto.UserDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class UserFacadeExposed : UserFacade {

    private fun resultRowToUser(row: ResultRow) = UserDto(
        uuid = row[UserDB.id].toString(),
        name = row[UserDB.name],
        password = row[UserDB.password]
    )

    override suspend fun getAllUser(): List<UserDto> = dbQuery {
        UserDB.selectAll().map(::resultRowToUser)
    }

    override suspend fun getUserById(uuid: String): UserDto? = dbQuery {
        UserDB.selectAll().where {UserDB.id eq UUID.fromString(uuid)}
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getUserByName(name: String): UserDto? = dbQuery {
        UserDB.selectAll().where {UserDB.name eq name}
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun deleteUser(user: String): Boolean = dbQuery {
        UserDB.deleteWhere { UserDB.id eq UUID.fromString(user) } > 0
    }

    override suspend fun updateUser(user: UserDto): Boolean = dbQuery {
        UserDB.update( { UserDB.id eq UUID.fromString(user.uuid) } ){
            it[password] = user.password
            it[name] = user.name
        } > 0
    }

    override suspend fun insertUser(user: UserDto): UserDto? = dbQuery {
        val insertStatement = UserDB.insert {
            it[password] = user.password
            it[name] = user.name
        }
        return@dbQuery insertStatement.resultedValues?.singleOrNull<ResultRow>()?.let(::resultRowToUser)

    }
}