package com.albasil.finalprojectkotlinbootcamp.Firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseAuthentication (){
    lateinit var binding: FirebaseAuthentication

    fun logInAuthentication(emailSignIn: String,passwordSignIn: String,view: View) {

        val email: String = emailSignIn.toString().trim { it <= ' ' }
        val password: String = passwordSignIn.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController(view).navigate(R.id.action_sign_in_to_tabBarFragment)

                 //  Toast.makeText(view.context, "${email.toString()}  ${password.toString()}  ${rememberMe.toString()} ", Toast.LENGTH_LONG).show()

                } else {
                   Toast.makeText(view.context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()

                }
            }
    }

}


