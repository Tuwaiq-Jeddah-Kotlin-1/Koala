package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleInformationViewModel (context: Application) : AndroidViewModel(context) {
    val repo : AppRepo = AppRepo(context)

    fun checkIfFavorite(myID: String,articleID: String,view:View){
        viewModelScope.launch (Dispatchers.IO){
            repo.checkIfFavorite(myID,articleID,view)

        }
    }
    fun udDateFavorite(myID:String,articleID: String,userID: String,view: View){
        viewModelScope.launch (Dispatchers.IO){
            repo.upDateFavorite(myID,articleID,userID,view)

        }
    }//upDateFavorite



}