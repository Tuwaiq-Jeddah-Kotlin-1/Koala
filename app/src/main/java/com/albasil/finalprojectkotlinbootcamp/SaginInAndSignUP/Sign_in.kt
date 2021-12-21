package com.albasil.finalprojectkotlinbootcamp.SaginInAndSignUP

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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

        //-------------------------------------------------------------------------------------------------------
        sharedPreferences = this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {
            findNavController().navigate(R.id.action_sign_in_to_homePage)
        }

        //-------------------------------------------------------------------------------------------------------------

        binding.btnSignInXml.setOnClickListener {

            when {
                TextUtils.isEmpty(binding.etSignInEmailXml.text.toString().trim { it <= ' ' }) -> {
                    //  val toastMessageEmail: String = this.getResources().getString(R.string.please_enter_email)
                    Toast.makeText(context, "toastMessageEmail", Toast.LENGTH_LONG).show()
                    binding.etSignInEmailLayout.helperText="gvhjjbkjh"
                }
                TextUtils.isEmpty(binding.etSignInPasswordXml.text.toString().trim { it <= ' ' }) -> {
                    //  val toastMessagePassword: String = this.getResources().getString(R.string.please_enter_password)
                    Toast.makeText(context, "toastMessagePassword", Toast.LENGTH_LONG).show()

                    binding.etSignInPasswordLayout.helperText="gvhjjbkjh"

                }
                else -> {

                    val firebaseLogIn =FirebaseAuthentication()

                    firebaseLogIn.logInAuthentication("${binding.etSignInEmailXml.text.toString()}",
                        "${binding.etSignInPasswordXml.text.toString()}"
                        ,view,rememberMe.isChecked)






                    //fun
                    //----------------------------------------------------------
                    val emailPreference: String = binding.etSignInEmailXml.text.toString()
                    val passwordPreference: String = binding.etSignInPasswordXml.text.toString()
                    val checked: Boolean = rememberMe.isChecked

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("EMAIL", emailPreference)
                    editor.putString("PASSWORD", passwordPreference)
                    editor.putBoolean("CHECKBOX", checked)
                    editor.apply()

                    //------------------------------------------------------------


                    // logInAuthentication()

                }}

        }


        binding.ForgotPasswordXml.setOnClickListener {
            forgotPasswordDialog()
        }

        binding.newAccountXml.setOnClickListener {

            findNavController().navigate(R.id.action_sign_in_to_signUP)

        }





    }


    fun forgotPasswordDialog(){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Forgot Password")
        val view: View = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val userEmail: EditText = view.findViewById(R.id.etForgotPassword)
        builder.setView(view)
        builder.setPositiveButton("Rest", { _, _ ->
            forgotPassword(userEmail)
        })
        builder.setNegativeButton("Close", { _, _ -> })
        builder.show()
    }


    private fun forgotPassword(userEmail: EditText) {

        val firebseAuth =FirebaseAuth.getInstance()
        if (userEmail.text.toString().isEmpty()){return}

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString()).matches()) {return}
        firebseAuth.sendPasswordResetEmail(userEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Send password Reset Email", Toast.LENGTH_SHORT).show()
                }
            }


    }


/*
    private fun logInAuthentication() {

                val email: String = binding.etSignInEmailXml.text.toString().trim { it <= ' ' }
                val password: String = binding.etSignInPasswordXml.text.toString().trim { it <= ' ' }

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



    */

}







