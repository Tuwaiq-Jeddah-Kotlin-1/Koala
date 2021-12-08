package com.albasil.finalprojectkotlinbootcamp.UI

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentAddArticleBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddArticle : Fragment() {

    lateinit var binding: FragmentAddArticleBinding
    private lateinit var imageUrl : Uri
    val userId = FirebaseAuth.getInstance().currentUser?.uid


    var categorySelected:String?=null

    // Date  object
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatted = current.format(formatter)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddArticleBinding.inflate(inflater, container, false)

        binding.tvDateXml.setText(" Date :${formatted}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = resources.getStringArray(R.array.Category)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)

        binding.spinnerCategoryXml.setAdapter(arrayAdapter)
        binding.spinnerCategoryXml.onItemClickListener = object :AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                categorySelected = "${category[position]}"

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                categorySelected = "${category[position]}"
//                Toast.makeText(context,"you selected :  ${category[position]}",Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnAddArticleXml.setOnClickListener {

            checkFields()


        }

        binding.articlerPhotoXml.setOnClickListener {

            selectImage()

        }





    }

     fun checkFields(){
         // Date  object
         val current = LocalDateTime.now()
         val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-SS")
         val formatted2 = current.format(formatter)

        when {
            TextUtils.isEmpty(binding.etTitleArticleXml.text.toString().trim { it <= ' ' }) -> {

                Toast.makeText(
                    context, "etTitleArticleXml", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(binding.etDescraptaionArticleXml.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "etDescraptaionArticleXml", Toast.LENGTH_LONG).show()

            }
            else -> {

                if (categorySelected.isNullOrEmpty()){
                    Toast.makeText(context, "categorySelected is null", Toast.LENGTH_LONG).show()

                }else{


                    articleData("${categorySelected.toString()}"
                        ,"${binding.etTitleArticleXml.text.toString()}"
                        ,"${binding.etDescraptaionArticleXml.text.toString()}"
                        ,"${userId}${binding.etTitleArticleXml.text.toString()}${formatted2}")

                }


            }}


    }



    //fireStore
     fun articleData(category:String,title: String,description:String, articlePhoto: String){


        val userId = FirebaseAuth.getInstance().currentUser?.uid


        val article =Article()
        article.category = category.toString()
        article.userId = userId.toString()
        article.date = formatted.toString()
        article.description = description.toString()
        article.title = title.toString()
        article.articleImage = articlePhoto.toString()


        addArticleToFirestore(article)

    }


    //firebase class
    fun addArticleToFirestore(article: Article) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val articleRef = Firebase.firestore.collection("Articles")
            //-----------UID------------------------
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            articleRef.add(article).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {

                        upLoadImage("${article.articleImage.toString()}")



                        Toast.makeText(context, "is Successful fire store", Toast.LENGTH_LONG).show()



                    }
                    else -> {

                        Toast.makeText(context, "is not Successful fire store ", Toast.LENGTH_LONG).show()


                    }


                }
            }

            withContext(Dispatchers.Main) {
                //Toast.makeText(coroutineContext.javaClass, "Welcome ${user.fullName.toString()}", Toast.LENGTH_LONG).show()

            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Toast.makeText(coroutineContext,0,0, e.message, Toast.LENGTH_LONG).show()
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }





    //images
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

            binding.articlerPhotoXml.setImageURI(imageUrl)

            //*******************************************************

        }

    }

    fun upLoadImage(uIdCategory:String){

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)

        progressDialog.show()


        val storageReference = FirebaseStorage.getInstance().getReference("imagesArticle/${uIdCategory}")

        storageReference.putFile(imageUrl)
            .addOnSuccessListener {
                //   userImage.setImageURI(null)
                Toast.makeText(context,"uploading image",Toast.LENGTH_SHORT).show()

                if (progressDialog.isShowing)progressDialog.dismiss()



            }.addOnFailureListener{
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
            }
    }



}