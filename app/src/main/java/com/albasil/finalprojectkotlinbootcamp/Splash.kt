package com.albasil.finalprojectkotlinbootcamp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.albasil.finalprojectkotlinbootcamp.SaginInAndSignUP.Sign_in
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class Splash : AppCompatActivity() {

    private lateinit var settings : SharedPreferences

    private lateinit var selectedLanguage:String

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val test = intent.getStringExtra("currentLang")

         selectedLanguage = test.toString()


        sharedPreferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        var editor1: SharedPreferences.Editor = sharedPreferences.edit()
        editor1.putString("Language", selectedLanguage)

        editor1.apply()
        editor1.toString()
        Toast.makeText(this," selectedLanguage is  ${ selectedLanguage.toString()}",Toast.LENGTH_LONG).show()





        val sharedPreferencesSettings= this.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferencesSettings.getString("Settings","")


        Toast.makeText(this," language is  ${ language.toString()}",Toast.LENGTH_LONG).show()

        if (language.toString()=="ar"){

            Toast.makeText(this," arabic",Toast.LENGTH_LONG).show()

            setLocate()

        }else{
            Toast.makeText(this," Else",Toast.LENGTH_LONG).show()

        }




        supportActionBar?.hide()
        Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)




    }

    private fun setLocate(){
        val locale = Locale("ar")

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale

        //---------------------------------------------------------------
        this?.resources?.updateConfiguration(config, this.resources.displayMetrics)


        settings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        settings.edit()
        settings.getString("My_lang","ar".toString())
        settings.apply{}

    }


}

