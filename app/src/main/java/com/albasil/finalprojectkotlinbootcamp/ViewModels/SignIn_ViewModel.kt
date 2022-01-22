package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SignIn_ViewModel(context: Application):AndroidViewModel(context){

        val firebaseAuth : FirebaseAuthentication = FirebaseAuthentication()

        // add to firebase
        fun signIn(userEmail:String,userPasword:String,view: View){
            viewModelScope.launch (Dispatchers.IO){

               firebaseAuth.logInAuthentication(userEmail,userPasword,view)

            }

        }


    }



