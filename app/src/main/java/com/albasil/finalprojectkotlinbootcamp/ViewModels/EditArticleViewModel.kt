package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditArticleViewModel(context: Application) : AndroidViewModel(context) {
    val repo : AppRepo = AppRepo(context)


    fun editArticle(articleID: String,title: String,description: String,category:String,iamgeArtcileId:String,view:View){
        viewModelScope.launch (Dispatchers.IO){

            repo.editArticleData(articleID,title,description,category,iamgeArtcileId,view)

        }
    }
}