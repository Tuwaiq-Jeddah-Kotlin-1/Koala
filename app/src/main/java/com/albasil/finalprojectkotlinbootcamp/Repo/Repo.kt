package com.albasil.finalprojectkotlinbootcamp.Repo

import android.content.Context
import android.net.Uri
import android.util.Log
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


var fireStore :FirebaseFirestore= FirebaseFirestore.getInstance()
class AppRepo(context: Context) {

    private  var imageUrl : Uri?=null

    val firebase = FirebaseAuthentication()

    suspend fun insertUserToDB(users: Users) {
        //appDB.taskDao.insert(addTask)

        //firebase.registerUser("F","F")

    }


    suspend fun addArticleToFirestore(article: Article) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val articleRef = Firebase.firestore.collection("Articles")
            articleRef.document(article.articleID).set(article).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {
//                        upLoadImage("${article.articleImage.toString()}")

                        Log.e("Add Article", "Done ${article.title}")

                    }
                    else -> {
                        Log.e("Field Article", "Error ${article.title}")

                    }
                }
            }
//            withContext(Dispatchers.Main) { }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }



    fun upLoadImage(uIdCategory:String){
        val storageReference = FirebaseStorage.getInstance().getReference("imagesArticle/${uIdCategory}")

        imageUrl?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {

                    Log.e("addOnSuccessListener", "addOnSuccessListener")

                }.addOnFailureListener{
                    Log.e("Failed", "Failed")

                }
        }
    }




}

