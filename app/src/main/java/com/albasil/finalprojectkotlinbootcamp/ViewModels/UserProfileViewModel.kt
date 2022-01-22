package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users

class UserProfileViewModel(context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)

    fun getUserArticles(
        myID: String,
        articleList: MutableList<Article>,
        viewLifecycleOwner: LifecycleOwner
    ): LiveData<MutableList<Article>> {

        val myArticles = MutableLiveData<MutableList<Article>>()
        repo.getUserArticles(myID, articleList).observe(viewLifecycleOwner, {
            myArticles.postValue(it)

        })
        return myArticles
    }


    fun deleteFollowersAndFollowing(myId: String, userId: String) {
        repo.deleteFollowing(myId, userId)
        repo.deleteFollowers(myId, userId)


    }
    fun addFollowersAndFollowing(myId: String, userId: String) {
        repo.addFollowing(myId, userId)
        repo.addFollowers(myId, userId)


    }


    fun getUserInformation(userID: String, userInfo: Users, viewLifecycleOwner: LifecycleOwner
    ): LiveData<Users> {
        val user = MutableLiveData<Users>()
        repo.getUserInfo(userID, userInfo).observe(viewLifecycleOwner, {
            user.postValue(userInfo)
        })
        return user
    }




}