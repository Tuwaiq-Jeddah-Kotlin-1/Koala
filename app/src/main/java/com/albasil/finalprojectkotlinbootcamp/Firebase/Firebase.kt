package com.albasil.finalprojectkotlinbootcamp.Firebase

import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthentication {

    fun registerUser(email:String , password:String) {

        val email2: String = email.toString().trim { it <= ' ' }
        val password2: String = password.toString().trim { it <= ' ' }

        //Phone number must be 10

        // create an instance and create a register with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email2, password2)
            .addOnCompleteListener { task ->

                // if the registration is sucessfully done
                if (task.isSuccessful) {
                    //firebase register user
                    val firebaseUser: FirebaseUser = task.result!!.user!!


                    Log.e("OK","registration is sucessfully done")
                    Log.e("OK","registration is sucessfully doe")

                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    ///   findNavController().navigate(R.id.categoryFragment)


                } else {
                    Log.e("OK","registration is not  sucessfully done")


                }
            }


    }


}



