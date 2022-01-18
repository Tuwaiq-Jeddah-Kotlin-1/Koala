package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.Repo.fireStore
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FavoriteAdapter(internal val favoritesList: ArrayList<Article>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    val myID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return FavoriteViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteArticle = favoritesList[position]
        holder.articleID = favoriteArticle.articleID
        holder.userID = favoriteArticle.userId
        holder.image = favoriteArticle.articleImage


                holder.numberLike.visibility = View.GONE

        firestore.collection("Users").document(myID.toString()).collection("Favorite")
            .document(favoriteArticle.articleID).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {
                    holder.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }


        holder.userName.setOnClickListener {
            if (myID.toString() == holder.userID.toString()) {
                NavHostFragment.findNavController(holder.itemView.findFragment())
                    .navigate(R.id.profile)

            } else {
                val userInformation =
                    TabBarFragmentDirections.actionTabBarFragmentToUserProfile(holder.userID.toString())
                NavHostFragment.findNavController(holder.itemView.findFragment())
                    .navigate(userInformation)
            }
        }
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Articles").document(favoriteArticle.articleID).get()
            .addOnCompleteListener {
                it
                if (it.result?.exists()!!) {
                    var articleTitle = it.result!!.getString("title")
                    var date = it.result!!.getString("date")
                    var articleImage = it.result!!.getString("articleImage")
                    var articleUserId = it.result!!.getString("userId")
                    var category = it.result!!.getString("category")
                    var like = it.result!!.get("like")
                    var description = it.result!!.getString("description")
                    holder.articleTitle.text = articleTitle.toString()
                    holder.articleCategory.text = category.toString()
                    holder.dateArticle.text = date.toString()
                    holder.userID = articleUserId.toString()

                    holder.articleDescription = description.toString()
                    holder.image = articleImage.toString()
                    holder.numberLikes = like.toString()

                    //--------------------------------------------------------------------------

                    val storageRef = FirebaseStorage.getInstance().reference
                        .child("/imagesArticle/${holder.image}")
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        holder.image_article.load(localFile)
                    }.addOnFailureListener {}

                    //--------------------------------------------------------------------------


                    if (holder.image.isNullOrBlank()) {

                        holder.image_article.visibility = View.GONE
                    }


                } else {

                    fireStore.collection("Users").document(myID.toString())
                        .collection("Favorite").document(favoriteArticle.articleID).delete()
                }
            }

        //----------------get User Name--------------------------------------------------
        firestore.collection("Users").document(favoriteArticle.userId).get()
            .addOnCompleteListener {
                it
                if (it.result?.exists()!!) {
                    var userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {
                }
            }


    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val myID = FirebaseAuth.getInstance().currentUser?.uid
        lateinit var articleID: String
        lateinit var userID: String

        var numberLikes: String? = null
        var image: String? = null
        var articleDescription: String? = null


        val ivFavorite:ImageView= itemView.findViewById(R.id.ivFavorite)
        val articleTitle: TextView = itemView.findViewById(R.id.tvTitle_xml)
        val articleCategory: TextView = itemView.findViewById(R.id.tvCategoryItem_xml)
        val userName: TextView = itemView.findViewById(R.id.tvUserName_xml)
        val dateArticle: TextView = itemView.findViewById(R.id.articleDate)
        val image_article: ImageView = itemView.findViewById(R.id.imageItem_xml)
        val numberLike: TextView = itemView.findViewById(R.id.numberLike_xml)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val article_data = Article()

            article_data.title = articleTitle.text.toString()
            article_data.userName = userName.text.toString()
            article_data.date = dateArticle.text.toString()
            article_data.category = articleCategory.text.toString()
            article_data.description = articleDescription.toString()
            article_data.articleImage = image.toString()
            article_data.articleID = articleID

            val itemData =
                TabBarFragmentDirections.actionTabBarFragmentToArticleInformation(article_data)
            NavHostFragment.findNavController(itemView.findFragment()).navigate(itemData)

        }
    }


}
