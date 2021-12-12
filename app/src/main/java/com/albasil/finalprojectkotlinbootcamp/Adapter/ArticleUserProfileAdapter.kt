package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.up_date_user_information.view.*

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






    }

    override fun getItemCount(): Int {

        return articleList.size

    }




    class UserViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val titleArticle : TextView =itemView.findViewById(R.id.titleArticle)
        val date : TextView =itemView.findViewById(R.id.dateArticle)
        val articleCategory : TextView =itemView.findViewById(R.id.articleCategory)

        val editLinear :LinearLayout =itemView.findViewById(R.id.editLinear)

        val editArticle :ImageView = itemView.findViewById(R.id.imEditArticle_xml)
        val deleteArticle :ImageView = itemView.findViewById(R.id.imDeleteArticle_xml)

       lateinit var userID :String
       lateinit var articleID:String





        //description
        init {
            itemView.setOnClickListener(this)
        }



        private fun deleteArticle() {
            AlertDialog.Builder(itemView.context)
                .setTitle("Delete Aricle")
                .setIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setMessage("Are sure to delete this article ?!!!")
                .setPositiveButton("yes") { dialog, _ ->


                    //-------------------------------

                    /*** delete fun */


                    val deleteArticle=Firebase.firestore.collection("Articles")
                        .document("1DVexs5RhquJcVi4WXNU").delete()


                    deleteArticle


                    //----------------------------


                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }



        override fun onClick(v: View?) {

            deleteArticle.setOnClickListener {
                deleteArticle()
                Toast.makeText(itemView.context,"Delete Article",Toast.LENGTH_SHORT).show()
            }


            editArticle.setOnClickListener {
                findNavController(itemView.findFragment()).navigate(R.id.apiFragment)

            }


            Toast.makeText(itemView.context,"${titleArticle.text.toString()} , ${articleCategory.text.toString()}",Toast.LENGTH_SHORT).show()


        }


    }









}