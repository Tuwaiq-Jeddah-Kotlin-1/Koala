package com.albasil.finalprojectkotlinbootcamp.SaginInAndSignUP

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Sign_in : Fragment() {
    lateinit var binding:FragmentSignInBinding


    private lateinit var sharedPreferences: SharedPreferences

    var isRemembered = false
    private lateinit var rememberMe: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /// val signinVM = ViewModelProvider(this).get(SignIn_ViewModel::)


        rememberMe = view.findViewById(R.id.cbRemember)
        sharedPreferences =
            this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {
            findNavController().navigate(R.id.action_sign_in_to_homePage)
        }


        binding.btnSignInXml.setOnClickListener {
            Toast.makeText(context," clicked", Toast.LENGTH_SHORT).show()

            when {
                TextUtils.isEmpty(binding.etSignInEmailXml.text.toString().trim { it <= ' ' }) -> {
                    //  val toastMessageEmail: String = this.getResources().getString(R.string.please_enter_email)

                    Toast.makeText(
                        context, "toastMessageEmail", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(binding.etSignInPasswordXml.text.toString().trim { it <= ' ' }) -> {
                    //  val toastMessagePassword: String = this.getResources().getString(R.string.please_enter_password)
                    Toast.makeText(context, "toastMessagePassword", Toast.LENGTH_LONG).show()

                }
                else -> {

                    logInAuthentication()

                }}

        }

        binding.newAccountXml.setOnClickListener {

            findNavController().navigate(R.id.profile)

        }





    }


    private fun logInAuthentication() {

                val email: String = binding.etSignInEmailXml.text.toString().trim { it <= ' ' }
                val password: String = binding.etSignInPasswordXml.text.toString().trim { it <= ' ' }

                // create an instance and create a register with email and password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            findNavController().navigate(R.id.action_sign_in_to_profile)


                            // val toastMessageWelcome: String = this@Login.getResources().getString(R.string.welcome)
                           // Toast.makeText(context, "$"toastMessageWelcome" ${email.toString()}", Toast.LENGTH_SHORT).show()

                            val emailPreference: String = email
                            val passwordPreference: String = password
                               val checked: Boolean = rememberMe.isChecked

                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("EMAIL", emailPreference)
                            editor.putString("PASSWORD", passwordPreference)
                            editor.putBoolean("CHECKBOX", checked)
                            editor.apply()

                            Toast.makeText(context, "toastMessageInfoSaved", Toast.LENGTH_LONG).show()


                           Toast.makeText(context, "${email.toString()}  ${password.toString()} ", Toast.LENGTH_LONG).show()



                        } else {
                            Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()

                        }
                    }
            }
        }





