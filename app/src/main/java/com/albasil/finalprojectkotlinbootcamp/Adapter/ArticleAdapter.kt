package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.albasil.finalprojectkotlinbootcamp.UI.HomePageDirections
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragment
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

val db = FirebaseFirestore.getInstance()

class ArticleAdapter(private val articleList: MutableList<Article>) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>(),Filterable {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    //----------------------------------------------------------------------------------------------------


    var storeList = mutableListOf<Article>()

    init {
        //Log.d(TAG, "$storeFilterList: ")
        articleList.forEach {
            storeList.add(it)
        //    Log.d(TAG, "$it: ")
        }
    }

    override fun getFilter(): Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<Article> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(storeList)
                //Log.d(TAG, "performFiltering: $storeList")
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in storeList) {
                    if (item.userName.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            articleList.clear()
            articleList.addAll(results.values as List<Article>)
            notifyDataSetChanged()
        }
    }




    //----------------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
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

        holder.upDateFavorite("${article.articleID}", article)



        //-------------------------------------------------------------------------------
        holder.userName.setOnClickListener {
            if (currentUserUid.toString() == holder.userId.toString()) {
                findNavController(holder.itemView.findFragment()).navigate(R.id.profile)

            } else {
                val userInformation =
                    TabBarFragmentDirections.actionTabBarFragmentToUserProfile(holder.userId.toString())
                findNavController(holder.itemView.findFragment()).navigate(userInformation)
            }
        }


        //-----------------------------------------------------------------------

        db.collection("Users").document(article.userId).get()
            .addOnCompleteListener { it
                if (it.result?.exists()!!) {

                    var userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {}
            }


        //-------------------------------------------------------------------------
        val storageRef = FirebaseStorage.getInstance().reference
            .child("/imagesArticle/${article.articleImage.toString()}")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageArticle.load(bitmap)
        }.addOnFailureListener {}
        //------------------------------------------------------------------------------------

    }


    override fun getItemCount(): Int {

        return articleList.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val titleArticle: TextView = itemView.findViewById(R.id.tvTitle_xml)
        val userName: TextView = itemView.findViewById(R.id.tvUserName_xml)
        val category: TextView = itemView.findViewById(R.id.tvCategoryItem_xml)
        val imageArticle: ImageView = itemView.findViewById(R.id.imageItem_xml)
        val numberLikes: TextView = itemView.findViewById(R.id.numberLike_xml)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        val tvDateArticle: TextView = itemView.findViewById(R.id.articleDate)

        lateinit var articleCategory: String
        lateinit var articleDate: String
        lateinit var articleDescription: String
        lateinit var image: String
        lateinit var userId: String


        fun upDateFavorite(articleID: String, article: Article) {

            val db = FirebaseFirestore.getInstance()
            db.collection("Users").document("$userID")
                .collection("Favorite").document(articleID.toString()).get()
                .addOnCompleteListener {

                    if (it.result?.exists()!!) {

                        ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)

                        ivFavorite.setOnClickListener {

                            //Fun
                            val deleteFavoriteArticle = FirebaseFirestore.getInstance()
                            deleteFavoriteArticle.collection("Users")
                                .document(userID.toString())
                                .collection("Favorite")
                                .document("${articleID.toString()}").delete()
                            article.like--
                            upDateArticleLike(articleID, article.like)

                            ivFavorite.setImageResource(R.drawable.ic_favorite_border)

                        }

                    } else {

                        ivFavorite.setOnClickListener {

                            ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)

                            //Fun
                            addFavorite("${articleID}", article)

                            article.like++
                            upDateArticleLike(articleID, article.like)

                        }
                    }
                }

        }

        private fun upDateArticleLike(articleID: String, upDateLike: Int) {
            val upDateLike = hashMapOf(
                "like" to upDateLike.toInt(),
            )
            val userRef = Firebase.firestore.collection("Articles")
            userRef.document("$articleID").set(upDateLike, SetOptions.merge())
                .addOnCompleteListener {
                    it
                    when {
                        it.isSuccessful -> {
                            Log.d("UpDate Like", "${upDateLike.toString()}")
                        }
                        else -> {
                            Log.d("UpDate Like", "${upDateLike.toString()}")
                        }
                    }
                }

        }

        private fun addFavorite(articleID: String, article: Article) {
            val addFavorite= hashMapOf(
                "articleID" to "${article.articleID}",
                "userId" to "${article.userId}",
            )
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
                }
        }

        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {

            val article_data = Article()

            article_data.title = titleArticle.text.toString()
            article_data.userName = userName.text.toString()
            article_data.date = articleDate.toString()
            article_data.category = articleCategory.toString()
            article_data.description = articleDescription.toString()
            article_data.articleImage = image.toString()
            article_data.like = numberLikes.text.toString().toInt()


            val itemData = TabBarFragmentDirections.actionTabBarFragmentToArticleInformation(article_data)
            findNavController(itemView.findFragment()).navigate(itemData)


        }

    }

}