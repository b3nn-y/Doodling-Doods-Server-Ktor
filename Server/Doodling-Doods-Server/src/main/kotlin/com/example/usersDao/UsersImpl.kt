package com.example.usersDao


import com.example.schemas.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.example.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UsersImpl:UsersDao {

    // signup a user
    override suspend fun signUp(user_name: String, mail_id: String, password: String): Boolean = dbQuery {
        if (!checkAlreadyUserExist(user_name, mail_id)) {
            val insertStatement = UsersTable.insert {
                it[UsersTable.user_name] = user_name
                it[UsersTable.mail_id] = mail_id
                it[UsersTable.password] = password
            }
            insertStatement.resultedValues?.isNotEmpty() ?: false
        } else{
            false
        }

    }
    //return all users from db
    override suspend fun allUsers(): List<UsersDataClass> = dbQuery {
        UsersTable.selectAll().map(::resultRowUsersDataClass)
    }


    // sign in a user
    override suspend fun signIn(mail_id: String, password: String): Boolean = dbQuery {
        val user = UsersTable.select {
            (UsersTable.mail_id eq mail_id) and (UsersTable.password eq password)
        }.singleOrNull()

        user != null
    }


    // check if a user already exist for sign up using email and user name
    private suspend fun checkAlreadyUserExist(user_name: String, mail_id: String):Boolean= dbQuery{

        val userByUsername = UsersTable.select { UsersTable.user_name eq user_name }.singleOrNull()
        val userByMailId = UsersTable.select { UsersTable.mail_id eq mail_id }.singleOrNull()

        userByUsername != null || userByMailId != null
    }
    // filter for sign up call
    override suspend fun userInputFilter(user_name:String, mail_id: String, password: String):Boolean{


        val emailRegex = Regex("[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}")
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$")
        val usernameRegex = Regex("^[a-zA-Z][a-zA-Z0-9_-]{2,19}\$")

        return user_name.matches(usernameRegex) &&
                mail_id.matches(emailRegex) && password.matches(passwordRegex)


    }

    // filter for login call
    override suspend fun userInputFilter(mail_id: String, password: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}")
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$")


        return mail_id.matches(emailRegex) && password.matches(passwordRegex)
    }


    // used to retrieve all users from db
    private fun resultRowUsersDataClass(row : ResultRow) = UsersDataClass(
        id = row[UsersTable.id],
        user_name = row[UsersTable.user_name],
        mail_id = row[UsersTable.mail_id],
        password = row[UsersTable.password]

    )
}