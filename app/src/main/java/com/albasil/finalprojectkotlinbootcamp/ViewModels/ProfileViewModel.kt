package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.Repo.fireStore
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)

    fun getAllMyArticles(myID: String, articleList: MutableList<Article>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Article>> {

        val myArticles = MutableLiveData<MutableList<Article>>()
        repo.getAllMyArticles(myID, articleList).observe(viewLifecycleOwner, {
            myArticles.postValue(it)

        })
        return myArticles
    }

    fun addUserInfo(myID:String,userInformation:String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.addUserInformation(myID, userInformation)
        }
    }

    fun upDateUserInformation(upDateName: String, upDatePhoneNumber: String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.upDateUserInfo(upDateName,upDatePhoneNumber)
        }
    }


    fun getUserInformation(myID: String):Users{

     return repo.getInfo(myID)
    }

    fun deleteArticle(articleID:String){
        repo.deleteArticle(articleID)
    }




    fun addArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addArticleToFirestore(article)

        }
    }

}

