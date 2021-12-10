package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.albasil.finalprojectkotlinbootcamp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfile : Fragment() {

    private val args by navArgs<UserProfileArgs>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        getUserInfo(args.userNAME.toString())
        val view =inflater.inflate(R.layout.fragment_user_profile, container, false)

        view.userName_xml.text ="${args.userNAME.toString()}"

        return view
    }




    fun getUserInfo(userID:String) = CoroutineScope(Dispatchers.IO).launch {

        try {
            //coroutine
            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document("$userID")
                .get().addOnCompleteListener {
                    it

                    if (it.getResult()?.exists()!!) {

                        //+++++++++++++++++++++++++++++++++++++++++
                        var name = it.getResult()!!.getString("userNamae")
                        var userEmail = it.getResult()!!.getString("userEmail")

                        Log.e("user Info","userName ${name.toString()} \n ${userEmail.toString()}")

                        Toast.makeText(context,"userName ${name.toString()} \n ${userEmail.toString()}",Toast.LENGTH_SHORT).show()


                        //++++++++++++++++++++++++++++++++++++++++++++++++++++
                     /*   tvEmail.setText(eamil)
                        tvName.setText(name)
                        tvBirthDay.setText(birthDay)
                        tvPhone.setText(phoneNumber)
                        tvCity.setText(city)
                        tvExperience.setText(experience)*/





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




}