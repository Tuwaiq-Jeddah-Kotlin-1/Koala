package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.albasil.finalprojectkotlinbootcamp.Repo.fireStore
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.firestore.*

class ProfileViewModel() :ViewModel(){



     fun getAllMyArticles(uId: String):LiveData<List<Article>> {
        val article= MutableLiveData<List<Article>>()

        val articleList :MutableList<Article> = mutableListOf()


        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").whereEqualTo("userId", "${uId}")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {

                            articleList.add(dc.document.toObject(Article::class.java))

                        }
                    }

                    article.value = articleList
                }

            })


        return article
    }

}