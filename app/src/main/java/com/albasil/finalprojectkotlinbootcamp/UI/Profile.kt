package com.albasil.finalprojectkotlinbootcamp.UI

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.ProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
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
    private lateinit var imageUrl: Uri
    private lateinit var userPhoneNumber: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleAdapter: ArticleUserProfileAdapter
    private lateinit var fireStore: FirebaseFirestore

    private lateinit var profileViewModel:ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = FragmentProfileBinding.inflate(inflater, container, false)


        val uid = FirebaseAuth.getInstance().uid
       getUserInfo("${uid}")

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //---------------------------------------------------
        getUserPhoto()

        //---------------------------------------------------------

        val uid = FirebaseAuth.getInstance().uid

        recyclerView = view.findViewById(R.id.userProfileRecyclerView_xml)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

        articleList = arrayListOf()

        articleAdapter = ArticleUserProfileAdapter(articleList)

        recyclerView.adapter = articleAdapter

     //   binding.myArticlesXml.setText(articleList.size.toString())

        //---------------------------------------------------------

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.getAllMyArticles(uid.toString()).observe(viewLifecycleOwner,{

            binding.userProfileRecyclerViewXml.adapter=ArticleUserProfileAdapter(it)

            articleAdapter.notifyDataSetChanged()
            getAllMyArticles("${uid}")


        })

       // getAllMyArticles("${uid}")

        articleAdapter.notifyDataSetChanged()


        //---------------------------------------------------------


        binding.myFavoriteXml.setOnClickListener {
            findNavController().navigate(R.id.favoriteFragment)
        }


        //-----------------------------------------------------
        binding.tvFollowersXml.setOnClickListener {


            val check = ProfileDirections.actionProfileToFollowersUserFragment("Followers")
            NavHostFragment.findNavController(this).navigate(check)



        }
        binding.tvFollowingXml.setOnClickListener {


            val check =
                ProfileDirections.actionProfileToFollowersUserFragment("Following")
            NavHostFragment.findNavController(this).navigate(check)
//            findNavController().navigate(R.id.followersUserFragment)

        }
        //----------------------------------------------

        binding.myArticleXml.setOnClickListener {

            binding.userProfileRecyclerViewXml.setBackgroundColor(Color.GRAY)
            binding.myArticleXml.setBackgroundColor(Color.GRAY)

        }

        binding.userImageProfileXml.setOnClickListener {

            selectImage()

        }

        binding.addInformationXml.setOnClickListener {
            showdialoguserInfo()
        }

        binding.upDateButtonXml.setOnClickListener {
            upDateUserInfoBottomSheet()
        }

        //
        articleAdapter.notifyDataSetChanged()
    }


    fun showdialoguserInfo() {

        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("User Information")
        val view: View = layoutInflater.inflate(R.layout.add_user_information_dialog, null)
        val editTextExperience: EditText =
            view.findViewById(R.id.editTextAddExperience)//saveExperience_id
        builder.setView(view)
        editTextExperience.setText(binding.userInfoXml.text.toString())
        builder.setPositiveButton("Save") { _, _ ->

            addInformation("${editTextExperience.text.toString()}")
            binding.userInfoXml.setText(editTextExperience.text.toString())

        }

        builder.setNegativeButton("Cancel", { _, _ -> })

        builder.show()
    }


    fun addInformation(addExperience: String) = CoroutineScope(Dispatchers.IO).launch {

        val userExperience = hashMapOf("moreInfo" to "${addExperience}",)

        val userRef = Firebase.firestore.collection("Users")
        //-----------UID------------------------
        val uId = FirebaseAuth.getInstance().currentUser?.uid

        userRef.document("$uId").set(userExperience, SetOptions.merge()).addOnCompleteListener { it
            when {
                it.isSuccessful -> { }
                else -> {
                }
            }
        }

    }


    fun getUserInfo(userID: String) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document("$userID")
                .get().addOnCompleteListener {
                    it

                    if (it.result?.exists()!!) {

                        //+++++++++++++++++++++++++++++++++++++++++
                        var name = it.result!!.getString("userName")
                        var userEmail = it.result!!.getString("userEmail")
                        var userFollowing = it.result!!.get("following")
                        var userFollowers = it.result!!.get("followers")
                        var userPhone = it.result!!.getString("userPhone")//moreInfo
                        var userInfo = it.result!!.getString("moreInfo")//moreInfo

                        Log.e("user Info", "userName ${name.toString()} \n ${userEmail.toString()}")

                        binding.userNameXml.text = "${name.toString()}"
                        binding.userInfoXml.text = "${userInfo.toString()}"
                        binding.userFollowersXml.text = "${userFollowers?.toString()}"
                        binding.userFollowingXml.text = "${userFollowing?.toString()}"

                        userPhoneNumber = "${userPhone.toString()}"


                    } else {
                        Log.e("error \n", "errooooooorr")
                    }

                    binding.myArticlesXml.setText(articleList.size.toString())
                }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Toast.makeText(coroutineContext,0,0, e.message, Toast.LENGTH_LONG).show()
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }


    }


    private fun getAllMyArticles(uId: String) {

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").whereEqualTo("userId", "${uId}")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.add(dc.document.toObject(Article::class.java))
                        }
                    }
                    articleAdapter.notifyDataSetChanged()

                }

            })


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

            binding.userImageProfileXml.setImageURI(imageUrl)

            //*******************************************************

            upLoadImage()
        }

    }



    fun upLoadImage() {

        //-----------UID------------------------
        val uId = FirebaseAuth.getInstance().currentUser?.uid
//
//        val progressDialog = ProgressDialog(context)
//        progressDialog.setMessage("Uploading File ...")
//        progressDialog.setCancelable(false)
//
//        progressDialog.show()

        val storageReference = FirebaseStorage.getInstance().getReference("imagesUsers/${uId}")

        storageReference.putFile(imageUrl)
            .addOnSuccessListener {
                Toast.makeText(context, "uploading image", Toast.LENGTH_SHORT).show()

//                if (progressDialog.isShowing) progressDialog.dismiss()

                getUserPhoto()


            }.addOnFailureListener {
//                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun getUserPhoto() {


        val imageName = "${FirebaseAuth.getInstance().currentUser?.uid}"

        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesUsers/$imageName")


        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

              binding.userImageProfileXml.load(bitmap)
          //  binding.userImageProfileXml.load(localFile)


        }.addOnFailureListener {

            Toast.makeText(context, "Failed image ", Toast.LENGTH_SHORT).show()

        }
    }


    private fun upDateUserInfoBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.up_date_user_information, null)

        val builder = BottomSheetDialog(requireView()?.context as Context)
        builder.setTitle("Up Date Information")

        //upDateUserInfoBottomSheet
        val editTextName = view.editTextTextUserName.text.toString()
        val editTextUserPhone = view.editTextPhone.text.toString()
        val upDateInfoButton = view.upDateInfoButton_xml

        view.editTextTextUserName.setText("${binding.userNameXml.text.toString()}")
        view.editTextPhone.setText("${userPhoneNumber.toString()}")

        upDateInfoButton.setOnClickListener {

            if (view.editTextTextUserName.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().length == 10
            ) {

                //view.editTextTextUserName.setText("${binding.userNameXml.text.toString()}")

                binding.userNameXml.setText(view.editTextTextUserName.text.toString())

                upDateUserInfo("${view.editTextTextUserName.text.toString()}", "${view.editTextPhone.text.toString()}")
                builder.dismiss()

            } else {

                Toast.makeText(context, "Please enter correct Information!!! ", Toast.LENGTH_SHORT).show()
            }

        }
        builder.setContentView(view)
        builder.show()
    }

    private fun upDateUserInfo(upDateName: String, upDatePhoneNumber: String) {

        val uId = FirebaseAuth.getInstance().currentUser?.uid
        val upDateUserData = Firebase.firestore.collection("Users")
        upDateUserData.document(uId.toString()).update("userNamae", "${upDateName.toString()}", "userPhone", "${upDatePhoneNumber.toString()}")

    }

}
