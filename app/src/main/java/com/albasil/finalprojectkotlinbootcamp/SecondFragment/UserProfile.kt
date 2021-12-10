package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSignUpBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UserProfile : Fragment() {

    lateinit var binding: FragmentUserProfileBinding

    private val args by navArgs<UserProfileArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentUserProfileBinding.inflate(inflater,container,false)

//        val view =inflater.inflate(R.layout.fragment_user_profile, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserPhoto(args.userID.toUri())

        getUserInfo(args.userID.toString())


    }


    fun getUserInfo(userID:String) = CoroutineScope(Dispatchers.IO).launch {

        try {
            //coroutine
            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document("$userID")
                .get().addOnCompleteListener {
                    it

                    if (it.result?.exists()!!) {

                        //+++++++++++++++++++++++++++++++++++++++++
                        var name = it.result!!.getString("userNamae")
                        var userEmail = it.result!!.getString("userEmail")
                        var userFollowing = it.result!!.get("following")
                        var userFollowers = it.result!!.get("followers")
                        var userPhone = it.result!!.getString("userPhone")

                        Log.e("user Info","userName ${name.toString()} \n ${userEmail.toString()}")


                        binding.userNameXml.text ="${name.toString()}"
                        binding.userInfoXml.text ="${userEmail.toString()}"
                        binding.userFollowersXml.text ="${userFollowers?.toString()}"
                        binding.userFollowingXml.text ="${userFollowing?.toString()}"

                        binding.userCallXml.setOnClickListener {
                            Toast.makeText(context,"call : ${userPhone.toString()}",Toast.LENGTH_SHORT).show()

                        }





                       // Log.e("error \n", "name $name   email $userEmail  //// $eamil")
                    } else {
                        Log.e("error \n", "errooooooorr")
                    }


                }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Toast.makeText(coroutineContext,0,0, e.message, Toast.LENGTH_LONG).show()
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }


    }


    fun getUserPhoto(userPhoto:Uri){


        val storageRef= FirebaseStorage.getInstance().reference
            .child("imagesUsers/$userPhoto")


        val localFile = File.createTempFile("tempImage","jpg")

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            binding.userImageProfileXml.load(bitmap)


        }.addOnFailureListener{
            /*if (progressDialog.isShowing)
                progressDialog.dismiss()*/

            Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }




}