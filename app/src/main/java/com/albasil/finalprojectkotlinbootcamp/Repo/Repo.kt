package com.albasil.finalprojectkotlinbootcamp.Repo

import android.content.Context
import android.util.Log
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


var fireStore :FirebaseFirestore= FirebaseFirestore.getInstance()
class AppRepo(context: Context) {


    val firebase = FirebaseAuthentication()

    suspend fun insertUserToDB(users: Users) {
        //appDB.taskDao.insert(addTask)

        //firebase.registerUser("F","F")

    }
}

