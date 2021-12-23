package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import com.albasil.finalprojectkotlinbootcamp.ArticleApp
import com.albasil.finalprojectkotlinbootcamp.MainActivity

class FeatherViewModel(val app : Application) : AndroidViewModel(app) {

    // this function is to check the internet connectivity .. cuz there are some things I don't want to run if there is no connection ..
    fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<ArticleApp>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // here is to select wish function to use to check for connectivity cuz it's changed during apis updates ..
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}