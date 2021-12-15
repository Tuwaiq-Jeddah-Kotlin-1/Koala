package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
class ArticleInformation : Fragment() {

    private lateinit var favorite :SharedPreferences
    var favoriteClicked =false

    var articleIsFavorte :Boolean =false

    private val args by navArgs<ArticleInformationArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_article_information, container, false)
        getArticlePhoto("${args.articleData.articleImage.toString()}")
        //------------------------------------------------------------------------
        view.titleArticleInfo_xml.text = " ${args.articleData.title.toString()}"
      view.userNameInfo_xml.text = " ${args.articleData.userName.toString()}"
       view.articleDateInfo_xml.text = " ${args.articleData.date.toString()}"
        view.articleCategoryInfo_xml.text = " ${args.articleData.category.toString()}"
        view.articleDescraptionInfo_xml.text = " ${args.articleData.description.toString()}"

        getUserPhoto2(args.articleData.articleImage.toString())


        cheackIsFavoirte(args.articleData.articleImage.toString())



        view.shearArticle_xml.setOnClickListener {

            shareArticle("${view.titleArticleInfo_xml.text}",
                "${view.articleDescraptionInfo_xml.text}")

        }

        view.favoriteArticle_xml.setOnClickListener {


            //fun
            if (articleIsFavorte){

                Log.e("Firestore"," delete from firestore")

                deleteFavoiret(args.articleData.articleImage.toString())
                view.favoriteArticle_xml.setBackgroundColor(Color.YELLOW)

            }else{

                Log.e("Firestore"," Add to firestore")


                articleData("${view.articleCategoryInfo_xml.text.toString()}",
                    "${view.titleArticleInfo_xml.text.toString()}",
                    "${view.articleDescraptionInfo_xml.text}",
                    "${args.articleData.articleImage.toString()}")

                view.favoriteArticle_xml.setBackgroundColor(Color.RED)


            }

        }


        Log.d("image",args.articleData.articleImage.toString().toUri().toString())

        return view
    }

    private fun deleteFavoiret(articleID:String) {
        val userId =FirebaseAuth.getInstance().currentUser?.uid
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .document(userId.toString())
            .collection("Favorite").document("${articleID.toString()}").delete()

        Toast.makeText(context,"delete article ",Toast.LENGTH_LONG).show()

    }

    private fun cheackIsFavoirte(articleID:String){
        val userId =FirebaseAuth.getInstance().currentUser?.uid
        val fireStore = FirebaseFirestore.getInstance()

        fireStore.collection("Users")
            .document(userId.toString())
            .collection("Favorite")
            .whereEqualTo("articleID","${articleID.toString()}")

          .addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                if (error != null){
                    Log.e("Firestore",error.message.toString())
                    return
                }

                for (dc : DocumentChange in value?.documentChanges!!){

                    if (dc.type == DocumentChange.Type.ADDED){

                        articleIsFavorte=true

                        favoirteIsTrue(articleIsFavorte)


                    }else{
                        articleIsFavorte=false
                        favoirteIsTrue(articleIsFavorte)

                    }
                }

               // articleAdapter.notifyDataSetChanged()

            }
        })


    }


    fun favoirteIsTrue(articleIsFavorte:Boolean){


        if (articleIsFavorte){
            view?.favoriteArticle_xml?.setBackgroundColor(Color.RED)
            Toast.makeText(context,"article Is Favorte ${articleIsFavorte.toString()}",
                Toast.LENGTH_LONG).show()
        }else{
            view?.favoriteArticle_xml?.setBackgroundColor(Color.GRAY)
        //    Toast.makeText(context,"article Is Favorte ${articleIsFavorte.toString()}", Toast.LENGTH_LONG).show()
        }

    }




    fun articleData(category:String,title: String,description:String, articlePhoto: String){


        val userId = FirebaseAuth.getInstance().currentUser?.uid


        val article = Article()
      //  article.userName =userNameGlobl.toString()
        article.category = category.toString()
        article.userId = userId.toString()
      //  article.date =
        article.description = description.toString()
        article.title = title.toString()
        article.articleImage = articlePhoto.toString()

        article.articleID=articlePhoto.toString()

        addUserFavorite(article)
    }

    fun addUserFavorite(article:Article) = CoroutineScope(Dispatchers.IO).launch {

        val userId =FirebaseAuth.getInstance().currentUser?.uid
        try {
            val articleRef = Firebase.firestore.collection("Users")

            articleRef.document(userId.toString()).collection("Favorite")
                .document(args.articleData.articleImage.toString())
                .set(article).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {


                        Toast.makeText(context,"Done to add User Favorite",Toast.LENGTH_LONG).show()
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
    //fireStore


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


}

