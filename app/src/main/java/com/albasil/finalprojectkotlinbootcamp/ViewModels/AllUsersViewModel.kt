package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.*
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Users

class AllUsersViewModel(context: Application) : AndroidViewModel(context) {

    val repo: AppRepo = AppRepo(context)




}