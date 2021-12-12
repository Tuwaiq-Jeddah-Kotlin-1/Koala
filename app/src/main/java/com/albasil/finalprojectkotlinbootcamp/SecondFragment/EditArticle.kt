package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentEditArticleBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import kotlinx.android.synthetic.main.fragment_edit_article.view.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EditArticle : Fragment() {
    private val args by navArgs<EditArticleArgs>()
    lateinit var binding: FragmentEditArticleBinding
    private lateinit var imageUrl : Uri


    lateinit var categorySelected:String




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        binding = FragmentEditArticleBinding.inflate(inflater,container,false)




        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.tvEditDateXml.text = " ${args.editArticle.date.toString()}"
        binding.etTitleArticleXml.setText("${args.editArticle.title.toString()}")
        binding.etDescraptaionArticleXml.setText("${args.editArticle.description.toString()}")
        binding.editSpinnerCategoryXml.setText("${args.editArticle.category.toString()}")
        categorySelected=args.editArticle.category.toString()

        getUserPhoto2("${args.editArticle.articleID.toString()}")



        //-------------------------------------------------------------------

        binding.editImageViewArticleXml.setOnClickListener {


            selectImage()

        }




        //---------------------------------------------------------------------


        val category = resources.getStringArray(R.array.Category)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)

        binding.editSpinnerCategoryXml.setAdapter(arrayAdapter)
        binding.editSpinnerCategoryXml.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                categorySelected = "${category[position]}"

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                categorySelected = "${category[position]}"
               Toast.makeText(context,"you selected :  ${category[position]}",Toast.LENGTH_SHORT).show()
            }
        }



        //----------------------------------------------------------------------


        binding.btnUpDateArticleXml.setOnClickListener {


            editArticleData("${args.editArticle.articleID.toString()}",
                "${binding.etTitleArticleXml.text.toString()}"
                ,"${binding.etDescraptaionArticleXml.text.toString()}",categorySelected.toString())

        }



    }



    //------------------------------------------

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

            binding.editImageViewArticleXml.setImageURI(imageUrl)

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



    //-----------------------------------------
    fun checkEditText(){

    }
    fun editArticleData(articleID:String,TitleArticle:String,DescraptaionArtic:String,category:String){
// Date
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formatted = current.format(formatter)
        val upDateUser= hashMapOf(
            "title" to TitleArticle.toString(),
            "description" to DescraptaionArtic,
            "date" to formatted,
            "category" to category
        )

        val userRef = Firebase.firestore.collection("Articles")
        userRef.document("${articleID}").set(upDateUser, SetOptions.merge()).addOnCompleteListener { it
            when {
                it.isSuccessful -> {
                    //getUserInfo("${uId}")

                    upLoadImage(articleID)
                    Toast.makeText(context,"UpDate ",Toast.LENGTH_SHORT).show()

                }
                else -> {
                    Toast.makeText(context,"Error to Update ",Toast.LENGTH_SHORT).show()


                }
            }
        }

    }






    fun getUserPhoto2(articleImagePath:String){


        val imageName = "${articleImagePath.toString()}"

        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/$imageName")


        val localFile = File.createTempFile("tempImage","*")//jpg

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            view?.editImageViewArticle_xml?.load(bitmap)


        }.addOnFailureListener{

            Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }

}