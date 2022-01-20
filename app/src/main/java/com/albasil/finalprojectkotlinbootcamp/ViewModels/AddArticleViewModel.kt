package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


private const val TAG = "AddPostActivity"


class AddArticleViewModel(context: Application,) : AndroidViewModel(context) {


    //Live Data
    var uploadImageLiveData = MutableLiveData<String>()
    var postLiveData = MutableLiveData<String>()
    var postErrorLiveData = MutableLiveData<String>()



    val repo : AppRepo = AppRepo(context)
    // add to firebase
     fun addArticle(article: Article, view: View){
        viewModelScope.launch (Dispatchers.IO){
            repo.addArticleToFirestore(article,view)
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