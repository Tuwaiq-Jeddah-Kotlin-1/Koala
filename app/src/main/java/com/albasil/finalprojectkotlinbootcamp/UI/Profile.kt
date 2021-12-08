package com.albasil.finalprojectkotlinbootcamp.UI

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class Profile : Fragment() {


    lateinit var binding: FragmentProfileBinding
    private lateinit var imageUrl : Uri

    private lateinit var preferences: SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        getUserPhoto()



        binding = FragmentProfileBinding.inflate(inflater,container,false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //tvProfileShow
        binding.tvProfileShow.setOnClickListener {


            preferences =
                this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
            val emailPref = preferences.getString("EMAIL", "")
           // userEmail.text = emailPref
            val passwordPref = preferences.getString("PASSWORD", "")



                val editor: SharedPreferences.Editor = preferences.edit()
                editor.clear()
                editor.apply()
                findNavController().navigate(R.id.action_profile_to_sign_in)



        }





        binding.userImageXml.setOnClickListener {


            selectImage()
        }



    }





    private fun selectImage(){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){

            imageUrl = data?.data!!

            binding.userImageXml.setImageURI(imageUrl)

            //*******************************************************

            upLoadImage()
        }

    }

    fun upLoadImage(){

        //-----------UID------------------------
        val uId = FirebaseAuth.getInstance().currentUser?.uid

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)

        progressDialog.show()


        val storageReference = FirebaseStorage.getInstance().getReference("imagesUsers/${uId}")

        storageReference.putFile(imageUrl)
            .addOnSuccessListener {
                //   userImage.setImageURI(null)
                Toast.makeText(context,"uploading image", Toast.LENGTH_SHORT).show()

                if (progressDialog.isShowing)progressDialog.dismiss()
                getUserPhoto()



            }.addOnFailureListener{
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
            }
    }



    fun getUserPhoto(){


        val imageName = "${FirebaseAuth.getInstance().currentUser?.uid}"

        val storageRef= FirebaseStorage.getInstance().reference
            .child("imagesUsers/$imageName")

        val localFile = File.createTempFile("tempImage","jpg")

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            binding.userImageXml.setImageBitmap(bitmap)


        }.addOnFailureListener{
            /*if (progressDialog.isShowing)
                progressDialog.dismiss()*/

            Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }












}