package com.albasil.finalprojectkotlinbootcamp.ViewModels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.albasil.finalprojectkotlinbootcamp.MainActivity
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.Repo.AppRepo
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.change_langauge.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.zip.Inflater

class SettingsViewModel(context: Application) : AndroidViewModel(context) {

    val repo : AppRepo = AppRepo(context)

    private lateinit var settings :SharedPreferences
/*
    private fun dialogChangeLanguage(context:Context) {
        val view: View =this.viewModelScope(R.layout.change_langauge, null)
        val builder = BottomSheetDialog(context)
        builder.setTitle("Change Language")
        val btnChangeLanguage = view.btnChangeLanguage
        var radioGroup = view.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var selectedLanguage: RadioButton =view.findViewById(checkedId)
            if (selectedLanguage != null) btnChangeLanguage.setOnClickListener {
                if (selectedLanguage.text.toString()=="Arabic"){
                    setLocaleFeather("ar")
                }else if (selectedLanguage.text.toString()=="English"){
                    setLocaleFeather("en")
                }
            }
        }
        builder.setContentView(view)
        btnChangeLanguage.setOnClickListener {

            if (view is RadioButton) {
                val checked = view.isChecked

                when (view.getId()) {
                    R.id.englishLanguageXml ->
                        if (checked) {
                            Log.e("Language","English")
                        }
                    R.id.arabicLanguageXml ->
                        if (checked) {
                            Log.e("Language","عربي")
                        }
                }
            }


        }
        builder.show()
    }
*/

    //**************************************************
    private fun setLocaleFeather(localeName: String,context:Context) {
        val locale = Locale(localeName)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        //---------------------------------------------------------------
        context?.resources?.updateConfiguration(config,context.resources.displayMetrics)
        settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = settings.edit()
        editor.putString("Settings", "${locale.toString()}")
        editor.apply()
        val refresh = Intent(context, MainActivity::class.java)
        startActivity(context,refresh,refresh.extras)
    }




    // Change Password
    fun changePassword( oldPassword: String, newPassword: String, confirmNewPassword: String,view:View){
        viewModelScope.launch (Dispatchers.IO){
            repo.changePassword(oldPassword,newPassword,confirmNewPassword,view)

        }
    }





}