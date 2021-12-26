package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.FavoriteFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragment
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FavoriteAdapter(internal val favoritesList: ArrayList<Article>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return FavoriteViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteArticle = favoritesList[position]
        holder.articleID = favoriteArticle.articleID
        holder.userID = favoriteArticle.userId

        holder.linearLayOutFavorite.visibility = View.GONE

        holder.userName.setOnClickListener {
            if (currentUserUid.toString() == holder.userID.toString()) {
                NavHostFragment.findNavController(holder.itemView.findFragment())
                    .navigate(R.id.profile)

            } else {
                val userInformation =
                    TabBarFragmentDirections.actionTabBarFragmentToUserProfile(holder.userID.toString())
                NavHostFragment.findNavController(holder.itemView.findFragment())
                    .navigate(userInformation)
            }
        }

        //--------------------------------------------------------------------------

        val storageRef = FirebaseStorage.getInstance().reference
            .child("/imagesArticle/${favoriteArticle.articleID.toString()}")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.image_article.load(bitmap)
        }.addOnFailureListener {}


        //--------------------------------------------------------------------------

        val db = FirebaseFirestore.getInstance()
        db.collection("Articles").document(favoriteArticle.articleID).get()
            .addOnCompleteListener {it

                if (it.result?.exists()!!) {
                    var articleTitle = it.result!!.getString("title")
                    var date = it.result!!.getString("date")
                    var articleImage = it.result!!.getString("articleImage")
                    var articleUserId = it.result!!.getString("userId")
                    var category = it.result!!.getString("category")

                    var like = it.result!!.get("like")
                   var description = it.result!!.getString("description")
//                    var category = it.result!!.getString("articleImage")


                    holder.articleTitle.text = articleTitle.toString()
                    holder.articleCategory.text = category.toString()
                    holder.dateArticle.text = date.toString()
                    holder.userID = articleUserId.toString()

                    holder.articleDescription =description.toString()
                    holder.image =articleImage.toString()
                    holder.numberLikes = like.toString()
                    Log.e("articleImage", "${articleImage.toString()}")

                } else {
                }
            }

        db.collection("Users").document(favoriteArticle.userId).get()
            .addOnCompleteListener { it
                if (it.result?.exists()!!) {
                    var userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {}
            }
    }

    override fun getItemCount(): Int {

        return favoritesList.size

    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        lateinit var articleID: String
        lateinit var userID: String

        lateinit var numberLikes: String
        lateinit var image: String
        lateinit var articleDescription: String


        val articleTitle: TextView = itemView.findViewById(R.id.tvTitle_xml)
        val articleCategory: TextView = itemView.findViewById(R.id.tvCategoryItem_xml)
        val userName: TextView = itemView.findViewById(R.id.tvUserName_xml)
        val dateArticle: TextView = itemView.findViewById(R.id.articleDate)
        val image_article: ImageView = itemView.findViewById(R.id.imageItem_xml)
        val linearLayOutFavorite: LinearLayout = itemView.findViewById(R.id.linearLayOutFavorite)

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
            article_data.like = numberLikes.toInt().toString().toInt()

            Toast.makeText(itemView.context, "${article_data.like}", Toast.LENGTH_LONG).show()
            Toast.makeText(itemView.context, "${article_data.description}", Toast.LENGTH_LONG)
                .show()


            val itemData = TabBarFragmentDirections.actionTabBarFragmentToArticleInformation(article_data)
            NavHostFragment.findNavController(itemView.findFragment()).navigate(itemData)

        }
    }


}
