package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "AddPostActivity"

class EditArticleViewModel(context: Application) : AndroidViewModel(context) {
    val repo : AppRepo = AppRepo(context)


    //Live Data
    var uploadImageLiveData = MutableLiveData<String>()
    var postLiveData = MutableLiveData<String>()
    var postErrorLiveData = MutableLiveData<String>()



    fun editArticle(articleID: String,title: String,description: String,category:String,iamgeArtcileId:String,view:View){
        viewModelScope.launch (Dispatchers.IO){

            repo.editArticleData(articleID,title,description,category,iamgeArtcileId,view)

        }
    }



    // Upload image to the firestore ---------------------------------------------------------------
    fun uploadArticleImage(imageUri: Uri, filename: String) {
        try {
            val response = repo.uploadArticleImage(imageUri, filename)

            response.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "upload image success")
                    uploadImageLiveData.postValue(filename)
                } else {
                    Log.d(TAG, "upload image fail - else")
                    postErrorLiveData.postValue(response.exception?.message)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "upload image fail - catch")
            postErrorLiveData.postValue(e.message)
        }
    }
}