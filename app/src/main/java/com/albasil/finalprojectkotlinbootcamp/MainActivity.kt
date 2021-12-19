package com.albasil.finalprojectkotlinbootcamp

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.albasil.finalprojectkotlinbootcamp.Adapter.TabAdapter
import com.albasil.finalprojectkotlinbootcamp.UI.AddArticle
import com.albasil.finalprojectkotlinbootcamp.UI.HomePage
import com.albasil.finalprojectkotlinbootcamp.UI.Profile
import com.albasil.finalprojectkotlinbootcamp.UI.Setting
import com.albasil.finalprojectkotlinbootcamp.workManager.NotificationRepo
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private  lateinit var addEventBar:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // NotificationRepo().myNotification(this)


        val sharedPreferencesSettings = this.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferencesSettings.getString("Settings", "")
        if (language.toString() == "ar") {
            //Toast.makeText(this, " arabic",Toast.LENGTH_LONG).show()
            setLocate()

        } else {
            //Toast.makeText(this, "English", Toast.LENGTH_LONG).show()
        }


        addEventBar=findViewById(R.id.addEventBtn)
     /*   addEventBar.setOnClickListener {
       //     findNavController(R.id.apiFragment)
          //  findNavController(R.id.apiFragment).navigate(R.id.apiFragment)

        }*/

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val bottomBar = findViewById<BottomAppBar>(R.id.bottomAppBar)


        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreen -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                R.id.signUP -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                R.id.sign_in -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    bottomBar.visibility = View.VISIBLE
                    addEventBar.visibility = View.VISIBLE
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

