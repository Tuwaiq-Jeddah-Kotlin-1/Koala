package com.albasil.finalprojectkotlinbootcamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class Controller : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


     /*   val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment

        val navController = navHostFragment.navController

         val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
          bottomNavView.setupWithNavController(navController)
          navController.addOnDestinationChangedListener { _, destination, _ ->
              when (destination.id) {
                  R.id.settingsFragment -> {
                      bottomNavView.visibility = View.GONE
                  }
                  R.id.profile -> {
                      bottomNavView.visibility = View.GONE
                  }
                  else -> {
                      bottomNavView.visibility = View.VISIBLE
                  }
              }
          }*/


    }
}

