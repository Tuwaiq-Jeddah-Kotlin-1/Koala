package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.UserProfileDirections
import com.albasil.finalprojectkotlinbootcamp.UI.HomePageDirections
import com.albasil.finalprojectkotlinbootcamp.UI.ProfileDirections
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.up_date_user_information.view.*
import java.io.File


class ArticleUserProfileAdapter(private val articleList:List<Article>): RecyclerView.Adapter<ArticleUserProfileAdapter.UserViewHolder>() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {




        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_article_user_profile,parent,false)

        return UserViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val article= articleList[position]
        holder.titleArticle.text = article.title
        holder.articleDate = article.date
        holder.articleCategory.text = article.category
        holder.userID = article.userId.toString()
        holder.articleID = article.articleID.toString()
        holder.userName = article.userName
        holder.imageArticleId = article.articleImage
        holder.articleDescription =article.description



        holder.deleteArticle.setOnClickListener {
            deleteArticle("${holder.articleID}",holder.itemView)
        }

        holder.editArticle.setOnClickListener {

            val article_data =Article()
            article_data.title = holder.titleArticle.text.toString()
            article_data.date = holder.articleDate.toString()
            article_data.category = article.category.toString()
            article_data.description = article.description.toString()
            article.articleImage =article.articleImage.toString()

           article_data.articleID = article.articleID.toString()

            val itemData = ProfileDirections.actionProfileToEditArticle(article_data,)

            findNavController(holder.itemView.findFragment()).navigate(itemData)

        }

        if (currentUserUid == holder.userID.toString()) {

                TransitionManager.beginDelayedTransition(holder.editLinear, AutoTransition())
                holder.editLinear.visibility = View.VISIBLE
        }




        //------------------------------------------------------------------

                val storageRef = FirebaseStorage.getInstance().reference
                .child("/imagesArticle/${article.articleImage.toString()}")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.image_article.load(bitmap)
        }.addOnFailureListener {}

//-----------------------------------------------------------------------





    }



    @SuppressLint("NotifyDataSetChanged")
        private fun deleteArticle(articleID:String, view: View) {
            AlertDialog.Builder(view.context)
                .setTitle("Delete Aricle")
                .setIcon(R.drawable.ic_delete_24)
                .setMessage("Are sure to delete this article ?!!!")
                .setPositiveButton("yes") { dialog, _ ->


                    //-------------------------------

                    /*** delete fun */
                    val deleteArticle=Firebase.firestore.collection("Articles")
                        .document("${articleID.toString()}").delete()

                    deleteArticle.addOnCompleteListener { notifyDataSetChanged()                    }

                    //----------------------------

                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.create().show()        }

    //--------------------------------------------------------------------------



        override fun getItemCount(): Int {

        return articleList.size

    }




    class UserViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

//        carsItem
        val titleArticle : TextView =itemView.findViewById(R.id.titleArticle)
//        val date : TextView =itemView.findViewById(R.id.dateArticle)
        val articleCategory : TextView =itemView.findViewById(R.id.articleCategory)

        val image_article :ImageView = itemView.findViewById(R.id.image_articleProfile)
        val editLinear :LinearLayout =itemView.findViewById(R.id.editLinear)

        val editArticle :ImageView = itemView.findViewById(R.id.imEditArticle_xml)
        val deleteArticle :ImageView = itemView.findViewById(R.id.imDeleteArticle_xml)


        lateinit var articleDate : String
       lateinit var userID :String
       lateinit var articleID:String
       lateinit var userName :String
       lateinit var imageArticleId:String
       lateinit var articleDescription:String





        //description
        init {
            itemView.setOnClickListener(this)
        }




        override fun onClick(v: View?) {

            val article_data =Article()

            article_data.title = titleArticle.text.toString()
            article_data.userName = userName.toString()
            article_data.date = articleDate.toString()
            article_data.category = articleCategory.text.toString()
            article_data.description = articleDescription.toString()
            article_data.articleImage = imageArticleId.toString()

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            if(userID.toString()==currentUserUid){
                val articleData = ProfileDirections.actionProfileToUserArticle(article_data)

                findNavController(itemView.findFragment()).navigate(articleData)

            }else{
                val articleData = UserProfileDirections.actionUserProfileToUserArticle(article_data)
                findNavController(itemView.findFragment()).navigate(articleData)

            }


            Toast.makeText(itemView.context,"${article_data.title.toString()} , ${articleCategory.text.toString()}",Toast.LENGTH_SHORT).show()


        }



    }









}