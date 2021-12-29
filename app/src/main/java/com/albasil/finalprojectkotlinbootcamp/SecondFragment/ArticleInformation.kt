package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import com.albasil.finalprojectkotlinbootcamp.Adapter.db
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import java.io.File

class ArticleInformation : Fragment() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    private var likesCounter: Int = 0


    private val args by navArgs<ArticleInformationArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article_information, container, false)

        //------------------------------------------------------------------------
        view.titleArticleInfo_xml.text = " ${args.articleData.title.toString()}"
        view.userNameInfo_xml.text = " ${args.articleData.userName.toString()}"
        view.articleDateInfo_xml.text = " ${args.articleData.date.toString()}"
        view.articleCategoryInfo_xml.text = " ${args.articleData.category.toString()}"
        view.articleDescraptionInfo_xml.text = " ${args.articleData.description.toString()}"



        likesCounter = args.articleData.like.toInt()


        if (args.articleData.articleImage.toString().isNullOrEmpty()) {
            Toast.makeText(
                context,
                " if (args.articleData.articleImage.toString().isNullOrEmpty()){",
                Toast.LENGTH_LONG
            ).show()
        }
        getArtciclePhoto(args.articleData.articleImage.toString())


        //يتاكد اذا في المفضلة او لا
        checkIfFavorite(args.articleData.articleImage.toString())
        Log.e(
            "args.articleData.articleImage.toString()",
            "${args.articleData.articleID.toString()}"
        )
        //----------------------
        view.favoriteArticle_xml.setOnClickListener {

            upDateFavorite(args.articleData.articleImage.toString())

        }

        view.shearArticle_xml.setOnClickListener {

            shareArticle(
                "${view.titleArticleInfo_xml.text}",
                "${view.articleDescraptionInfo_xml.text}"
            )
        }


        return view
    }

    //---------------------------------------------------------------------
    private fun checkIfFavorite(articleID: String) {

        val uId = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$uId")
            .collection("Favorite").document(articleID).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {
                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)

                } else {
                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)
                }
            }
    }

    //---------deleteFavorite------------------------------------------------------------------------------------------
    fun deleteFavorite(articleID: String) {
        userId
        val deleteFavoriteArticle = FirebaseFirestore.getInstance()
        deleteFavoriteArticle.collection("Articles").document(articleID)
            .collection("Favorite").document("${userId.toString()}").delete()
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        Log.e("Delete Article ", "Delete From Articles Favorite")
                    }
                }
            }

        //-------------deleteFavoriteArticleUser----------------------------------------------------------
        val deleteFavoriteArticleUser = FirebaseFirestore.getInstance()
        deleteFavoriteArticleUser.collection("Users").document(userId.toString())
            .collection("Favorite").document("${articleID.toString()}").delete()
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        Log.e("Delete Article ", "Delete From User Favorite")
                    }
                }
            }
        numberOfFavorite(articleID)
    }


    //---------------addFavorite-------------------------------------------------------------------------------------------
    private fun addFavorite(articleID: String, article: Article) {
        val addFavorite = hashMapOf(
            "articleID" to "${article.articleID}",
            "userId" to "${article.userId}",
        )
        //---------------------------------------------------------------------------------
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val articleRef = Firebase.firestore.collection("Users")
        articleRef.document(userId.toString()).collection("Favorite")
            .document("${articleID.toString()}")
            .set(addFavorite).addOnCompleteListener {
                it
                when {
                    it.isSuccessful -> {
                        Log.d("Add Article", "Done to add User Favorite")
                    }
                    else -> {
                        Log.d("Error", "is not Successful fire store")
                    }
                }

                //---------------------------------------------------------------------------------
                val addToArticle = Firebase.firestore.collection("Articles")
                addToArticle.document(articleID.toString()).collection("Favorite")
                    .document("${userId.toString()}").set(addFavorite)
                //---------------------------------------------------------------------------------


                //delete ...
                numberOfFavorite(articleID)

            }
    }

    private fun numberOfFavorite(articleID: String) {
        db.collection("Articles").document(articleID)
            .collection("Favorite").get()
            .addOnSuccessListener {
                var numberOfFavorite = it.size()
                val userRef = Firebase.firestore.collection("Articles")
                userRef.document("$articleID").update("like", numberOfFavorite)

            }
    }


    //---------------------------------------
    private fun upDateFavorite(articleID: String) {
        //check in the fireStore
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$userId")
            .collection("Favorite").document(articleID.toString()).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {

                    deleteFavorite(articleID)

                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)


                } else {
                    articleData(
                        view?.userNameInfo_xml?.text.toString(),
                        "${view?.articleCategoryInfo_xml?.text.toString()}",
                        "${view?.titleArticleInfo_xml?.text.toString()}",
                        "${view?.articleDescraptionInfo_xml?.text}",
                        "${args.articleData.articleImage.toString()}",
                        view?.articleDateInfo_xml?.text.toString()
                    )

                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)


                }
            }
    }


    //------------------------------------------------------------------------------------------
    fun articleData(
        userName: String,
        category: String,
        title: String,
        description: String,
        articlePhoto: String,
        articleDate: String
    ) {
        val article = Article()
        article.userName = userName.toString()
        article.category = category.toString()
        article.userId = userId.toString()
        article.date = articleDate
        article.description = description.toString()
        article.title = title.toString()
        article.articleImage = articlePhoto.toString()
        article.articleID = articlePhoto.toString()


        // addUserFavorite(article)

        addFavorite(article.articleID, article)
    }

    fun shareArticle(titleArticle: String, subjectArticle: String) {

        val shareArticle = Intent(Intent.ACTION_SEND)
        shareArticle.type = "text/plain"
        shareArticle.putExtra(Intent.EXTRA_TEXT, subjectArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_TITLE, titleArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_SUBJECT, subjectArticle)

        startActivity(shareArticle)
    }


    fun getArtciclePhoto(imagePath: String) {


        val imageName = "${imagePath.toString()}"

        val storageRef = FirebaseStorage.getInstance().reference
            .child("/imagesArticle/$imageName")


        val localFile = File.createTempFile("tempImage", "*")//jpg

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            view?.articlePhotoInfo_xml?.load(bitmap)


        }.addOnFailureListener {


            //   Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }

}

