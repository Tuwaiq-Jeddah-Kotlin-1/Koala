package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingUp_ViewModel(context: Application) : AndroidViewModel(context) {

        val repo : AppRepo = AppRepo(context)

    // add to firebase
    fun addUser(users: Users){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertUserToDB(users)

        }

    }


}