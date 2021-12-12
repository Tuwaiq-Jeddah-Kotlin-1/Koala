package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.UI.HomePageDirections
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File

//class ArticleUserProfileAdapter(private val articleList:ArrayList<Article>) RecyclerView.Adapter<ArticleAdapter.UserViewHolder>() {

    class ArticleUserProfileAdapter(private val articleList:ArrayList<Article>): RecyclerView.Adapter<ArticleUserProfileAdapter.UserViewHolder>() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {




        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_user_profile,parent,false)

        return UserViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val article= articleList[position]
        holder.titleArticle.text = article.title
        holder.date.text = article.date
        holder.articleCategory.text = article.category
        holder.userID = article.userId.toString()




        if (currentUserUid == holder.userID.toString()) {


                TransitionManager.beginDelayedTransition(holder.editLinear, AutoTransition())
                holder.editLinear.visibility = View.VISIBLE

        }
      /*  if (currentUserUid == holder.userID.toString()){

           // holder.editLinear.isVisible
            //holder.articleCategory.setTextColor(Color.GREEN)

            holder.editLinear.isVisible

            if (holder.editLinear.visibility == View.GONE){
                holder.editLinear.visibility == View.VISIBLE

            }else{
                holder.editLinear.visibility == View.GONE

            }

        }else{
            holder.editLinear.isGone

        }*/






    }

    override fun getItemCount(): Int {

        return articleList.size

    }




    class UserViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val titleArticle : TextView =itemView.findViewById(R.id.titleArticle)
        val date : TextView =itemView.findViewById(R.id.dateArticle)
        val articleCategory : TextView =itemView.findViewById(R.id.articleCategory)

        val editLinear :LinearLayout =itemView.findViewById(R.id.editLinear)
       lateinit var userID :String



        //description
        init {
            itemView.setOnClickListener(this)
        }



        override fun onClick(v: View?) {

            date.setOnClickListener {
                if (editLinear.visibility == View.GONE){
                    editLinear.visibility == View.VISIBLE

                }else{
                    editLinear.visibility == View.GONE

                }
            }


            Toast.makeText(itemView.context,"${titleArticle.text.toString()} , ${articleCategory.text.toString()}",Toast.LENGTH_SHORT).show()
          /*  val article_data = Article()

            article_data.title = titleArticle.text.toString()
            article_data.userName = userName.text.toString()
            article_data.date = date.text.toString()
            article_data.category = articleCategory.toString()
            article_data.description = articleDescription.toString()
            article_data.articleImage = image.toString()


            val itemData = HomePageDirections.actionHomePageToArticleInformation(article_data)
            NavHostFragment.findNavController(itemView.findFragment()).navigate(itemData)
//          findNavController(itemView.findFragment()).navigate(R.id.action_homePage_to_articleInformation)
*/


        }


    }









}