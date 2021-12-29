package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.Repo.fireStore
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(context: Application) : AndroidViewModel(context) {
    val repo : AppRepo = AppRepo(context)

    fun getAllMyArticles(myID:String,articleList: MutableList<Article>,viewLifecycleOwner: LifecycleOwner):LiveData<MutableList<Article>>{
    //    viewModelScope.launch (Dispatchers.IO){
        val myArticles= MutableLiveData<MutableList<Article>>()
       // myArticles.postValue(repo.getAllMyArticles(myID,articleList))
            repo.getAllMyArticles(myID,articleList).observe(viewLifecycleOwner,{
                myArticles.postValue(it)

            })

        //}


        return myArticles
    }

}