package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.firestore.auth.User

class FollowersUserViewModel (context: Application) : AndroidViewModel(context) {
    val repo: AppRepo = AppRepo(context)


    fun getFollowersAndFollowing(type:String, articleList: MutableList<Users>, viewLifecycleOwner: LifecycleOwner): MutableLiveData<MutableList<Users>> {

        val followersAndFollowingList = MutableLiveData<MutableList<Users>>()
        repo.getFollowersAndFollowing(type, articleList).observe(viewLifecycleOwner, {
            followersAndFollowingList.postValue(it)

        })
        return followersAndFollowingList
    }

}