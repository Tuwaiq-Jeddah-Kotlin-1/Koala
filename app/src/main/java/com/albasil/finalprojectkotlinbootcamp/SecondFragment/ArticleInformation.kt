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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.properties.Delegates

class ArticleInformation : Fragment() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

     private var likesCounter : Int=0


    private val args by navArgs<ArticleInformationArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_article_information, container, false)

        //------------------------------------------------------------------------
        view.titleArticleInfo_xml.text = " ${args.articleData.title.toString()}"
        view.userNameInfo_xml.text = " ${args.articleData.userName.toString()}"
        view.articleDateInfo_xml.text = " ${args.articleData.date.toString()}"
        view.articleCategoryInfo_xml.text = " ${args.articleData.category.toString()}"
        view.articleDescraptionInfo_xml.text = " ${args.articleData.description.toString()}"



        likesCounter=args.articleData.like.toInt()


        if (args.articleData.articleImage.toString().isNullOrEmpty()){
            Toast.makeText(context," if (args.articleData.articleImage.toString().isNullOrEmpty()){",Toast.LENGTH_LONG).show()
        }
        getArtciclePhoto(args.articleData.articleImage.toString())




        //يتاكد اذا في المفضلة او لا
        checkIfFavoirte(args.articleData.articleImage.toString())
        Log.e("args.articleData.articleImage.toString()", "${args.articleData.articleID.toString()}")
        //----------------------
        view.favoriteArticle_xml.setOnClickListener {

            upDateFavorite(args.articleData.articleImage.toString())

        }

        view.shearArticle_xml.setOnClickListener {

            shareArticle("${view.titleArticleInfo_xml.text}",
                "${view.articleDescraptionInfo_xml.text}")
        }


        return view
    }

    //---------------------------------------------------------------------
    private fun checkIfFavoirte(articleID:String){
        val uId =FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$uId")
            .collection("Favorite").document(articleID.toString()).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!){

                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)

                }else{
                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)


                }
            }
    }







    //---------------------------------------
    private fun upDateFavorite(articleID:String,){
        //check in the fireStore
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$userId")
            .collection("Favorite").document(articleID.toString()).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!){

                  deleteFavoiret(articleID.toString())

                    likesCounter = likesCounter-1

                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)
                    upDateArticleLike(articleID, likesCounter)


                }else{

                  articleData(view?.userNameInfo_xml?.text.toString(),
                        "${view?.articleCategoryInfo_xml?.text.toString()}",
                        "${view?.titleArticleInfo_xml?.text.toString()}",
                        "${view?.articleDescraptionInfo_xml?.text}",
                        "${args.articleData.articleImage.toString()}"
                        ,view?.articleDateInfo_xml?.text.toString())

                    likesCounter = likesCounter+1

                    upDateArticleLike(articleID,likesCounter!!.toInt())



                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)




                }
            }
    }


    private fun deleteFavoiret(articleID:String) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .document(userId.toString())
            .collection("Favorite")
            .document("${articleID.toString()}").delete()

        Toast.makeText(context,"delete article ",Toast.LENGTH_LONG).show()

    }



    private fun upDateArticleLike(articleID: String,upDateLike:Int,){

        val upDateLike= hashMapOf(
            "like" to upDateLike.toInt(),
        )
        val userRef = Firebase.firestore.collection("Articles")
        userRef.document("$articleID").set(upDateLike, SetOptions.merge()).addOnCompleteListener { it
            when {
                it.isSuccessful -> {
                   // Toast.makeText(context,"UpDate lIEK ",Toast.LENGTH_SHORT).show()

                }
                else -> {

                   // Toast.makeText(context," no UpDate lIEK ",Toast.LENGTH_SHORT).show()


                }
            }
        }

    }


    fun articleData(userName:String,category:String,title: String,description:String, articlePhoto: String,articleDate:String){
        val article = Article()
        article.userName =userName.toString()
        article.category = category.toString()
        article.userId = userId.toString()
       article.date =articleDate
        article.description = description.toString()
        article.title = title.toString()
        article.articleImage = articlePhoto.toString()
        article.articleID=articlePhoto.toString()
        addUserFavorite(article)
    }

    fun addUserFavorite(article:Article) = CoroutineScope(Dispatchers.IO).launch {
        val addFavorite= hashMapOf(
            "articleID" to "${article.articleID}",
            "userId" to "${article.userId}",
        )
        val userId =FirebaseAuth.getInstance().currentUser?.uid
        try {
            val articleRef = Firebase.firestore.collection("Users")

            articleRef.document(userId.toString()).collection("Favorite")
                .document(args.articleData.articleImage.toString())
                .set(addFavorite).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {


                      //  Toast.makeText(context,"Done to add User Favorite",Toast.LENGTH_LONG).show()
                    }
                    else -> {

                    //    Toast.makeText(context, "is not Successful fire store ", Toast.LENGTH_LONG).show()

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
    //fireStore


    fun shareArticle(titleArticle:String,subjectArticle:String){

        val shareArticle = Intent(Intent.ACTION_SEND)
        shareArticle.type = "text/plain"
        shareArticle.putExtra(Intent.EXTRA_TEXT,subjectArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_TITLE,titleArticle.toString())
        shareArticle.putExtra(Intent.EXTRA_SUBJECT,subjectArticle)

        startActivity(shareArticle)
    }


    fun getArtciclePhoto(imagePath:String){


        val imageName = "${imagePath.toString()}"

        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/$imageName")


        val localFile = File.createTempFile("tempImage","*")//jpg

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            view?.articlePhotoInfo_xml?.load(bitmap)


        }.addOnFailureListener{


         //   Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }




}

