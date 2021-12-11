package com.albasil.finalprojectkotlinbootcamp.Repo

import android.content.Context
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication

class AppRepo(context: Context) {


    val firebase =FirebaseAuthentication()

    suspend fun insertUserToDB(users: Users){
        //appDB.taskDao.insert(addTask)

        //firebase.registerUser("F","F")

    }
}