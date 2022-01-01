package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article

class HomePageViewModel(context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)

    //----------------------getAllArticles-----------------------------------
    fun getAllArticles(articleList: MutableList<Article>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Article>> {

        val allArticles = MutableLiveData<MutableList<Article>>()
        repo.getAllArticles(articleList).observe(viewLifecycleOwner, {
            allArticles.postValue(it)

        })
        return allArticles
    }


    //----------------------getAllMyArticles-----------------------------------
    fun removeAllArticles(articleList: MutableList<Article>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Article>> {

        val removeArticles = MutableLiveData<MutableList<Article>>()
        repo.removeAllArticles(articleList).observe(viewLifecycleOwner, {
            removeArticles.postValue(it)

        })
        return removeArticles
    }//removeAllArticles
}