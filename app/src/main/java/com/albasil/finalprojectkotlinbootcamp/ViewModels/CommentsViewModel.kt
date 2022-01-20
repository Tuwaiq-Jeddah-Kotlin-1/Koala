package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentsViewModel(context:Application):AndroidViewModel(context) {

        val repo: AppRepo = AppRepo(context)

     fun addComments(comment: Comment, view: View){
        viewModelScope.launch (Dispatchers.IO){
            repo.addComments(comment,view)

        }
    }



    fun getAllComment(articleID:String,articleList: MutableList<Comment>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Comment>> {

        val allComment = MutableLiveData<MutableList<Comment>>()
        repo.getAllComments(articleID,articleList).observe(viewLifecycleOwner, {
            allComment.postValue(it)

        })
        return allComment
    }


    }