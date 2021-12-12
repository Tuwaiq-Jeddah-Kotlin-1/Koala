package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import java.io.File
class ArticleInformation : Fragment() {
    private val args by navArgs<ArticleInformationArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =inflater.inflate(R.layout.fragment_article_information, container, false)

        getArticlePhoto("${args.articleData.articleImage.toString()}")

        view.titleArticleInfo_xml.text = " ${args.articleData.title.toString()}"
      view.userNameInfo_xml.text = " ${args.articleData.userName.toString()}"
       view.articleDateInfo_xml.text = " ${args.articleData.date.toString()}"
        view.articleCategoryInfo_xml.text = " ${args.articleData.category.toString()}"
        view.articleDescraptionInfo_xml.text = " ${args.articleData.description.toString()}"



        view.shearArticle_xml.setOnClickListener {

            Toast.makeText(context,"${view.titleArticleInfo_xml.text}\n" +
                    "${view.articleDescraptionInfo_xml.text}",Toast.LENGTH_SHORT).show()

            shareArticle("${view.titleArticleInfo_xml.text}",
                "${view.articleDescraptionInfo_xml.text}")

        }

        view.favoriteArticle_xml.setOnClickListener {
            view.favoriteArticle_xml.setBackgroundColor(Color.RED)
        }


        Log.d("image",args.articleData.articleImage.toString().toUri().toString())

        getUserPhoto2(args.articleData.articleImage.toString())
        return view
    }





    fun shareArticle(titleArticle:String,subjectArticle:String){

        val shareArticle = Intent(Intent.ACTION_SEND)
        shareArticle.type = "text/plain"
        shareArticle.putExtra(Intent.EXTRA_TEXT,subjectArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_TITLE,titleArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_SUBJECT,subjectArticle)

        startActivity(shareArticle)
    }


    fun getUserPhoto2(imagePath:String){


        val imageName = "${imagePath.toString()}"

        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/$imageName")


        val localFile = File.createTempFile("tempImage","*")//jpg

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            view?.articlePhotoInfo_xml?.load(bitmap)


        }.addOnFailureListener{
            /*if (progressDialog.isShowing)
                progressDialog.dismiss()*/

            Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }


    fun getArticlePhoto(imagePath:String){

        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/${imagePath.toString()}")

        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            //viewimageArticle.load(bitmap)
        }
    }






    fun addFavorite(){


    }

}

