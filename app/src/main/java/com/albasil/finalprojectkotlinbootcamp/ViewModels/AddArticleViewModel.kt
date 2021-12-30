package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class AddArticleViewModel(context: Application) : AndroidViewModel(context) {
    val repo : AppRepo = AppRepo(context)
    // add to firebase
     fun addArticle(article: Article){
        viewModelScope.launch (Dispatchers.IO){
            repo.addArticleToFirestore(article)

        }
    }



/*
    fun articleImage(articleImage: String){
        viewModelScope.launch (Dispatchers.IO){

            repo.upLoadImage(articleImage)
        }
    }
*/
}