package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.EditArticleViewModel
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentEditArticleBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_edit_article.view.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EditArticle : Fragment() {
    private val args by navArgs<EditArticleArgs>()
    lateinit var binding: FragmentEditArticleBinding
    private  var imageUrl : Uri?=null
    lateinit var categorySelected:String

    private lateinit var editArticleViewModel:EditArticleViewModel



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

        editArticleViewModel = ViewModelProvider(this).get(EditArticleViewModel::class.java)

        binding.tvEditDateXml.text = " ${args.editArticle.date.toString()}"
        binding.etTitleArticleXml.setText("${args.editArticle.title.toString()}")
        binding.etDescraptaionArticleXml.setText("${args.editArticle.description.toString()}")
        binding.editSpinnerCategoryXml.setText("${args.editArticle.category.toString()}")
        categorySelected=args.editArticle.category.toString()

        getUserPhoto("${args.editArticle.articleImage}")

        Toast.makeText(context, "Imag ID ${args.editArticle.articleImage}", Toast.LENGTH_SHORT).show()
        //-------------------------------------------------------------------

        binding.editImageViewArticleXml.setOnClickListener {


            selectImage()

        }
        //---------------------------------------------------------------------

        val category = resources.getStringArray(R.array.categories)
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
            }
        }

        //----------------------------------------------------------------------


        binding.btnUpDateArticleXml.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.etTitleArticleXml.text.toString().trim { it <= ' ' }) -> {
                    binding.tvEditTitleArticleXml.helperText="Title Article is null"
                }
                TextUtils.isEmpty(binding.etDescraptaionArticleXml.text.toString().trim { it <= ' ' }) -> {
                    binding.tvEditDescraptaionArticleXml.helperText="Descraptaion Article is null"
                }
                else -> {
                    if (categorySelected.isNullOrEmpty()){
                        Toast.makeText(context, "Please Select Category", Toast.LENGTH_LONG).show()
                    }else{

                        if (imageUrl!=null) {
                            editArticleViewModel.editArticle(
                                "${args.editArticle.articleID.toString()}",
                                "${binding.etTitleArticleXml.text.toString()}",
                                "${binding.etDescraptaionArticleXml.text.toString()}",
                                categorySelected.toString(),
                                args.editArticle.articleID.toString(),
                                view
                            )
                            Toast.makeText(context, "upate image", Toast.LENGTH_SHORT).show()

                            upLoadImage(args.editArticle.articleID.toString())
                        }else{

                            editArticleViewModel.editArticle(
                                "${args.editArticle.articleID.toString()}",
                                "${binding.etTitleArticleXml.text.toString()}",
                                "${binding.etDescraptaionArticleXml.text.toString()}",
                                categorySelected.toString(),
                               "",
                                view
                            )

                            //add articles
                            findNavController().navigate(R.id.profile)

                        }

                    }
                }
            }
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

    fun upLoadImage(imageArticleID:String){

      /* val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)

        progressDialog.show()*/

        val storageReference = FirebaseStorage.getInstance().getReference("imagesArticle/${imageArticleID}")

        imageUrl?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    //   userImage.setImageURI(null)
                  //  Toast.makeText(context,"uploading image",Toast.LENGTH_SHORT).show()

                //    if (progressDialog.isShowing)progressDialog.dismiss()
                    
                }.addOnFailureListener{
                 //   if (progressDialog.isShowing)progressDialog.dismiss()
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }







    fun getUserPhoto(articleImagePath:String){


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