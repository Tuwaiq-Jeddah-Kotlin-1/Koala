package com.albasil.finalprojectkotlinbootcamp.UI

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.up_date_user_information.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class Profile : Fragment() {


    lateinit var binding: FragmentProfileBinding
    private lateinit var imageUrl : Uri


    private lateinit var recyclerView : RecyclerView
    private lateinit var articleList :ArrayList<Article>
    private lateinit var userList :ArrayList<Users>
    private lateinit var articleAdapter : ArticleUserProfileAdapter
    private lateinit var fireStore :FirebaseFirestore


    private lateinit var preferences: SharedPreferences





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       //getUserPhoto()




        binding = FragmentProfileBinding.inflate(inflater,container,false)


        val uid=FirebaseAuth.getInstance().uid

        getUserInfo("${uid}")

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //---------------------------------------------------
        getUserPhoto()


        //---------------------------------------------------------

        val uid=FirebaseAuth.getInstance().uid

        recyclerView = view.findViewById(R.id.userProfileRecyclerView_xml)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        articleList = arrayListOf()
        userList = arrayListOf()

        articleAdapter = ArticleUserProfileAdapter(articleList)

        recyclerView.adapter = articleAdapter

        EventChangeListener("${uid}")

        //---------------------------------------------------------



        binding.myArticleXml.setOnClickListener {

            binding.userProfileRecyclerViewXml.setBackgroundColor(Color.GRAY)
            binding.myArticleXml.setBackgroundColor(Color.GRAY)

        }

        binding.myFavoriteXml.setOnClickListener {

            binding.userProfileRecyclerViewXml.setBackgroundColor(Color.CYAN)
            binding.myFavoriteXml.setBackgroundColor(Color.CYAN)

        }




        binding.userImageProfileXml.setOnClickListener {


            selectImage()
        }


        binding.upDateButtonXml.setOnClickListener {

            upDateUserInfoBottomSheet()
        }



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
                        binding.userInfoXml.text ="${userEmail.toString()} \n " +
                                "${userPhone.toString()}"
                        binding.userFollowersXml.text ="${userFollowers?.toString()}"
                        binding.userFollowingXml.text ="${userFollowing?.toString()}"




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


    private fun EventChangeListener(uId:String){

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").whereEqualTo("userId","${uId}")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null){
                        Log.e("Firestore",error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!){

                        if (dc.type == DocumentChange.Type.ADDED){
                            articleList.add(dc.document.toObject(Article::class.java))

                        }
                    }

                    articleAdapter.notifyDataSetChanged()


                }

            })







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

            binding.userImageProfileXml.setImageURI(imageUrl)

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

            binding.userImageProfileXml.load(bitmap)


        }.addOnFailureListener{
            /*if (progressDialog.isShowing)
                progressDialog.dismiss()*/

            Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }


    private fun upDateUserInfoBottomSheet() {
        val view:View = layoutInflater.inflate(R.layout.up_date_user_information,null)

        val builder = BottomSheetDialog(requireView()?.context as Context)
        builder.setTitle("Up Date Information")

        //upDateUserInfoBottomSheet
       val  editTextName  = view.editTextTextUserName.text.toString()
       val  editTextUserPhone  = view.editTextPhone.text.toString()
        val upDateInfoButton =  view.upDateInfoButton_xml

        view.editTextTextUserName.setText("${binding.userNameXml.text.toString()}")
      //  view.editTextPhone.setText("${tvPhone.text.toString()}")


        upDateInfoButton.setOnClickListener {

            if (view.editTextTextUserName.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().length ==10){


                upDateUserInfo("${view.editTextTextUserName.text.toString()}",
                    "${view.editTextPhone.text.toString()}")


            }else{

                Toast.makeText(context,"Please enter correct Information!!! ",Toast.LENGTH_SHORT).show()
            }

        }
        builder.setContentView(view)
        builder.show()
    }


    private fun upDateUserInfo(upDateName:String,upDatePhoneNumber:String){

        val upDateUser= hashMapOf(
            "userNamae" to "${upDateName.toString()}",
            "userPhone" to "${upDatePhoneNumber.toString()}",
        )
        val userRef = Firebase.firestore.collection("Users")
        //-----------UID------------------------
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        userRef.document("$uId").set(upDateUser, SetOptions.merge()).addOnCompleteListener { it
            when {
                it.isSuccessful -> {
                    getUserInfo("${uId}")

                    Toast.makeText(context,"UpDate ",Toast.LENGTH_SHORT).show()

                }
                else -> {

                }
            }
        }

    }







}