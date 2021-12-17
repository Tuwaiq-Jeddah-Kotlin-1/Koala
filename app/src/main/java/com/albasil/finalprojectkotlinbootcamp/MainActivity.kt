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
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.albasil.finalprojectkotlinbootcamp.workManager.NotificationRepo
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // NotificationRepo().myNotification(this)


        val sharedPreferencesSettings = this.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferencesSettings.getString("Settings", "")
        if (language.toString() == "ar") {
            Toast.makeText(this, " arabic",Toast.LENGTH_LONG).show()
            setLocate()

        } else {
            Toast.makeText(this, "English", Toast.LENGTH_LONG).show()
        }


            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
            val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

//        val fragmentContainerView = findViewById<FragmentContainerView>(R.id.container_fragment)
            bottomNavView.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.sign_in -> {
                        bottomNavView.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE

                        //bottomNavView.visibility = View.VISIBLE
                    }
                    R.id.signUP -> {
                        bottomNavView.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE

                    }
                    R.id.splashScreen -> {
                        bottomNavView.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE


                    }
                    else -> {
                        bottomNavView.visibility = View.VISIBLE
                        bottomAppBar.visibility = View.VISIBLE

                    }
                }
            }


        }




    private fun setLocate() {
        val locale = Locale("ar")

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale

        //---------------------------------------------------------------
        this?.resources?.updateConfiguration(config, this.resources.displayMetrics)

    }


}

