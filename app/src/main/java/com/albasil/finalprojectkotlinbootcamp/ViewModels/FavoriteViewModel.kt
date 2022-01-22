package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article

class FavoriteViewModel (context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)



    fun favoriteArticles(myID: String, articleList: MutableList<Article>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Article>> {

        val myArticles = MutableLiveData<MutableList<Article>>()
        repo.favoriteArticles(myID, articleList).observe(viewLifecycleOwner, {
            myArticles.postValue(it)

        })
        return myArticles
    }

}