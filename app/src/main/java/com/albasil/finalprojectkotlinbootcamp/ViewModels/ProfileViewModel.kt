package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(context: Application) : AndroidViewModel(context) {

    //Live Data
    var uploadImageLiveData = MutableLiveData<String>()
    var postLiveData = MutableLiveData<String>()
    var postErrorLiveData = MutableLiveData<String>()

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


    //add article after deleted
    fun addArticle(article: Article, view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addArticleToFirestore(article,view)

        }
    }




    fun uploadUserImage(imageUri: Uri, userID: String) {
        try {
            val response = repo.uploadUserImage(imageUri, userID)

            response.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadImageLiveData.postValue(userID)
                } else {
                    postErrorLiveData.postValue(response.exception?.message)
                }
            }
        } catch (e: Exception) {
            postErrorLiveData.postValue(e.message)
        }
    }





}

