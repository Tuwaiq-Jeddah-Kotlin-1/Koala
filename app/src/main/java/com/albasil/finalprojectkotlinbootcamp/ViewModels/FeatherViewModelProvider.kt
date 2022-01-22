package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//this class is to define how my ViewModel should be created .. NOW_FOR_TEST_ONLY
class FeatherViewModelProvider(val app : Application ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeatherViewModel(app ) as T
    }
}