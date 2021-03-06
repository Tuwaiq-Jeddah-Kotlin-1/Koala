package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import kotlinx.coroutines.*
import java.util.*

@SuppressLint("StaticFieldLeak")
val firestore = FirebaseFirestore.getInstance()


class ArticleAdapter(private val articleList: MutableList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {
    val myID = FirebaseAuth.getInstance().currentUser?.uid

    //----------------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articleList[position]
        holder.titleArticle.text = article.title
        holder.articleDate = article.date
        holder.articleCategory = article.category
        holder.category.text = article.category
        holder.articleDescription = article.description
        holder.image = article.articleImage
        holder.userId = article.userId
        holder.numberLikes.text = article.like.toString()
        holder.tvDateArticle.text = article.date
        holder.articleID = article.articleID


        //------------------------------------------------------------------------------------------

        if (article.articleImage.isNullOrBlank()){

            holder.imageArticle.visibility= View.GONE
        }


        firestore.collection("Users").document(myID.toString()).collection("Favorite")
            .document(article.articleID).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {
                    holder.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        holder.ivFavorite.setOnClickListener {
            holder.upDateFavorite("${article.articleID}", article)
        }

        holder.imageArticle(article.articleImage)
        holder.imageArticle.load(article.articleImage)

            holder.imageUser(article.userId)
            holder.userImageView.load(article.userId)





        //******************************************************************************************
        //count number likes
       firestore.collection("Articles").document(article.articleID).collection("Favorite").get()
            .addOnSuccessListener {
                val numberOfFavorite = it.size()
                holder.numberLikes.text =numberOfFavorite.toString()
            }

        //---------------userName----------------------------------------------------------------
       holder.userLinearLayout.setOnClickListener {
            if (myID.toString() == holder.userId.toString()) {
                findNavController(holder.itemView.findFragment()).navigate(R.id.profile)

            } else {
                val userInformation =
                    TabBarFragmentDirections.actionTabBarFragmentToUserProfile(holder.userId.toString())
                findNavController(holder.itemView.findFragment()).navigate(userInformation)
            }
        }

        //----------------check User Name ----------------------------------------------
        firestore.collection("Users").document(article.userId).get()
            .addOnCompleteListener {it
                if (it.result?.exists()!!) {
                    var userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {
                }
            }
    }
    override fun getItemCount(): Int = articleList.size


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val myID = FirebaseAuth.getInstance().currentUser?.uid
        val titleArticle: TextView = itemView.findViewById(R.id.tvTitle_xml)
        val userName: TextView = itemView.findViewById(R.id.tvUserName_xml)
        val category: TextView = itemView.findViewById(R.id.tvCategoryItem_xml)
        val imageArticle: ImageView = itemView.findViewById(R.id.imageItem_xml)
        val userImageView: ImageView = itemView.findViewById(R.id.userImage)
        val userLinearLayout: LinearLayout = itemView.findViewById(R.id.userLinearLayout)

        val numberLikes: TextView = itemView.findViewById(R.id.numberLike_xml)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        val tvDateArticle: TextView = itemView.findViewById(R.id.articleDate)
        lateinit var articleCategory: String
        lateinit var articleDate: String
        lateinit var articleDescription: String
        var image: String?=null
        lateinit var userId: String
        lateinit var articleID:String

        fun imageUser(userID:String){
            val storageRef = FirebaseStorage.getInstance().reference
                .child("//imagesUsers/${userID}")
            val localFile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                userImageView.load(localFile)
            }.addOnFailureListener {}
        }


        //-------------------------------------------------------------------------
        fun imageArticle(articleImage:String){
            val storageRef = FirebaseStorage.getInstance().reference
                .child("/imagesArticle/${articleImage}")
            val localFile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                imageArticle.load(localFile)
            }.addOnFailureListener {}
        }
        //------------------------------------------------------------------------------------


        //---------upDateFavorite------------------------------------------------------------------------------------------
        fun upDateFavorite(articleID: String, article: Article) {
            firestore.collection("Users").document(myID.toString()).collection("Favorite")
                .document(articleID).get()
                .addOnCompleteListener {
                    if (it.result?.exists()!!) {

                        deleteFavorite("${articleID}")
                        ivFavorite.setImageResource(R.drawable.ic_favorite_border)

                    } else {

                        ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                        addFavorite("${articleID}", article)

                    }
                }
        }

        //---------deleteFavorite------------------------------------------------------------------------------------------
        fun deleteFavorite(articleID: String) {
            val deleteFavoriteArticle = FirebaseFirestore.getInstance()
            deleteFavoriteArticle.collection("Articles").document(articleID)
                .collection("Favorite").document(myID.toString()).delete()
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            Log.e("Delete Article ", "Delete From Articles Favorite")
                        }
                    }
                }
            //-------------deleteFavoriteArticleUser----------------------------------------------------------
            val deleteFavoriteArticleUser = FirebaseFirestore.getInstance()
            deleteFavoriteArticleUser.collection("Users").document(myID.toString())
                .collection("Favorite").document("${articleID.toString()}").delete()
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            Log.e("Delete Article ", "Delete From User Favorite")
                        }
                    }
                }
         //   numberOfFavorite(articleID)
        }

        //---------------addFavorite-------------------------------------------------------------------------------------------
        fun addFavorite(articleID: String, article: Article) {
            val addFavorite = hashMapOf(
                "articleID" to "${article.articleID}",
                "userId" to "${article.userId}",
            )
            //---------------------------------------------------------------------------------
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val articleRef = Firebase.firestore.collection("Users")
            articleRef.document(userId.toString()).collection("Favorite")
                .document("${articleID.toString()}")
                .set(addFavorite).addOnCompleteListener {it
                    when {it.isSuccessful -> {
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
                }
        }

        //----------------------------------------------------------------------------------------------------------------
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            val article_data = Article()
            article_data.userId =userId
            article_data.title = titleArticle.text.toString()
            article_data.userName = userName.text.toString()
            article_data.date = articleDate.toString()
            article_data.category = articleCategory.toString()
            article_data.description = articleDescription.toString()
            article_data.articleImage = image.toString()
            article_data.like = numberLikes.text.toString().toInt()
            article_data.articleID =articleID.toString()


            val itemData =
                TabBarFragmentDirections.actionTabBarFragmentToArticleInformation(article_data)
            findNavController(itemView.findFragment()).navigate(itemData)
        }

    }

}