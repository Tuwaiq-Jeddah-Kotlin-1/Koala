package com.albasil.finalprojectkotlinbootcamp

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FeatherViewModel
import androidx.navigation.fragment.findNavController


class SplashScreen : Fragment() {

    lateinit var viewModel: FeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        // to access the activity's ViewModel
        viewModel = (activity as MainActivity).viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Handler().postDelayed({
            if(viewModel.hasInternetConnection()){
               // findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignIn())
            }else{
                Toast.makeText(context, "No Internet !!!!", Toast.LENGTH_SHORT).show()
            }
           findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignIn())

        }, 5000)

    }
}