package com.albasil.finalprojectkotlinbootcamp.SaginInAndSignUP

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.autofill.UserData
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.up_date_user_information.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUP : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    private var imageUrl: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        binding = FragmentSignUpBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignUpXml.setOnClickListener {

            SignUpFirebaseAuth()
        }

        binding.userImageXml.setOnClickListener {

            selectImage()
        }

    }


    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            imageUrl = data?.data!!

            binding.userImageXml.setImageURI(imageUrl)

            //*******************************************************

        }

    }

    fun upLoadImage(uId: String) {

        /*   val progressDialog = ProgressDialog(context)
           progressDialog.setMessage("Uploading File ...")
           progressDialog.setCancelable(false)

           progressDialog.show()*/

        //-----------UID------------------------

        val storageReference = FirebaseStorage.getInstance().getReference("imagesUsers/${uId}")

        imageUrl?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    //   userImage.setImageURI(null)
                    Toast.makeText(context, "uploading image", Toast.LENGTH_SHORT).show()

                    //     if (progressDialog.isShowing)progressDialog.dismiss()


                }.addOnFailureListener {
                    // if (progressDialog.isShowing)progressDialog.dismiss()
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun SignUpFirebaseAuth() {
        when {
            TextUtils.isEmpty(binding.etEmailXml.text.toString().trim { it <= ' ' }) -> {
                binding.etSignUpEmailLayout.helperText = "Please Enter Email"
            }

            TextUtils.isEmpty(binding.etUserNameXml.text.toString().trim { it <= ' ' }) -> {
                binding.tvFullName.helperText = "Please Enter Password"

            }
            TextUtils.isEmpty(binding.etPhoneXml.text.toString().trim { it <= ' ' }) -> {
                binding.tvPhone.helperText = "Please Enter Phone Number"

            }
            TextUtils.isEmpty(binding.etPasswordXml.text.toString().trim { it <= ' ' }) -> {
                binding.tvPassword.helperText = "Please Enter Your password"

            }
            else -> {
                if (binding.etPhoneXml.text.toString().length == 10) {
                    registerUser(
                        "${binding.etEmailXml.text.toString()}",
                        "${binding.etPasswordXml.text.toString()}"
                    )

                } else {
                    binding.tvPhone.helperText = "Must be 10 number"


                }
            }
        }

    }


    //class Firebase
    fun registerUser(email: String, password: String) {

        val email: String = email.toString().trim { it <= ' ' }
        val password: String = password.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Log.e("OK", "registration is sucessfully done")

                    insertUser(
                        "${email.toString()}",
                        "${binding.etUserNameXml.text.toString()}",
                        "${binding.etPhoneXml.text.toString()}"
                    )


                    findNavController().navigate(R.id.action_signUP_to_tabBarFragment)


                } else {

                    Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG)
                        .show()


                }
            }.addOnCompleteListener {}


    }


    //class
    fun insertUser(email: String, userName: String, userPhone: String) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val user = Users()
        user.userId = userId.toString()
        user.userEmail = email.toString()
        user.userName = userName.toString()
        user.userPhone = userPhone.toString()

        createUserFirestore(user)
    }


    //firebase class
    fun createUserFirestore(user: Users) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val userRef = Firebase.firestore.collection("Users")
            //-----------UID------------------------

            userRef.document("${user.userId}").set(user).addOnCompleteListener {
                it
                when {
                    it.isSuccessful -> {
                        upLoadImage("${user.userId}")

                        Toast.makeText(context, "Welcome", Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> {
                        Toast.makeText(context, "is not Successful", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }


}

