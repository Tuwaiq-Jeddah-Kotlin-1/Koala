package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)

    fun getAllMyArticles(myID: String, articleList: MutableList<Article>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Article>> {

        val myArticles = MutableLiveData<MutableList<Article>>()
        repo.getUserArticles(myID, articleList).observe(viewLifecycleOwner, {
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


    fun getUserInformation(myID: String, userInfo: Users, viewLifecycleOwner: LifecycleOwner):LiveData<Users>{
        val user = MutableLiveData<Users>()
     repo.getUserInfo(myID,userInfo).observe(viewLifecycleOwner,{
         user.postValue(userInfo)
     })
        return user
    }



    fun deleteArticle(articleID:String){
        repo.deleteArticle(articleID)
    }




    //i can add article after deleted
    fun addArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addArticleToFirestore(article)

        }
    }


    fun getUserProfile(): File {

        return repo.getUserPhoto()
    }




}

