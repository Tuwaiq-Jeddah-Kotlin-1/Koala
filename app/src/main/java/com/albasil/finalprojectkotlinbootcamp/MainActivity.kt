package com.albasil.finalprojectkotlinbootcamp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FeatherViewModel
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FeatherViewModelProvider
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : FeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // NotificationRepo().myNotification(this)

        val viewModelProviderFactory = FeatherViewModelProvider(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FeatherViewModel::class.java)

        //------------------------------------------------------------------------------------
        val sharedPreferencesSettings = this.getSharedPreferences("preference", Activity.MODE_PRIVATE)
        val language = sharedPreferencesSettings.getString("preference", "")

        if (language.toString() == "ar") {
            setLocate()
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val bottomBar = findViewById<BottomAppBar>(R.id.bottomAppBar)


        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreen -> {
                  //  getSupportActionBar()?.hide()context as Activity

                    //bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    supportActionBar!!.hide()

                }
                R.id.signUP -> {
                   // bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    //        requireActivity().supportActionBar?.hide()
                   // (this as AppCompatActivity?)!!.supportActionBar!!.customView
                    supportActionBar!!.hide()

                }
                R.id.sign_in -> {
                 //   bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
              //      (this as AppCompatActivity?)!!.supportActionBar!!.hide()
                    supportActionBar!!.hide()


                }
                else -> {
                    //bottomNavView.visibility = View.VISIBLE
                    bottomBar.visibility = View.VISIBLE
                    supportActionBar!!.show()
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
        this.resources?.updateConfiguration(config, this.resources.displayMetrics)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.share -> {
                val shareArticle = Intent(Intent.ACTION_SEND)
                shareArticle.type = "text/plain"
                shareArticle.putExtra(Intent.EXTRA_TEXT,"Feather ")
                shareArticle.putExtra(Intent.EXTRA_TITLE,"titleArticle.toString()")
                shareArticle.putExtra(Intent.EXTRA_SUBJECT,"subjectArticle")

                startActivity(shareArticle)
                return true
            }
            R.id.about_Feather -> {
                AlertDialog.Builder(this)
                    .setTitle("About Feather")
                    .setIcon(R.drawable.ic_baseline_remember_me_24)
                    .setMessage("تم تطوير تطبيق ريشة من قبل المطور باسل تحت أكادمية طويق 100 بأشراف أ/شادي سليم و أ//سمية الطويرقي  ")
                    .setPositiveButton("OK"){
                            dialog,_ ->

                        dialog.dismiss()
                    }.create().show()
                return true
            }
            else -> return true
        }
    }


}