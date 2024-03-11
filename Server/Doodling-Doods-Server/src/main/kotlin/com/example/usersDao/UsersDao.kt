package com.example.usersDao

interface UsersDao {
    suspend fun signUp(user_name:String, mail_id:String, password: String):Boolean

    suspend fun allUsers():List<UsersDataClass>
    suspend fun signIn(mail_id: String, password: String):Boolean
    // for sign up
    suspend fun userInputFilter(user_name:String,mail_id: String):Boolean
    // for login
//    suspend fun userInputFilter(mail_id: String,password: String):Boolean
//    suspend fun hashPassword(password: String):String


    suspend fun getUser(mail_id: String):UsersDataClass?


}