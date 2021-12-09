package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.ArticleInformation



class ArticleAdapter(private val articleList:ArrayList<Article>):RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


       /* val user :Users = userList[position]

        holder.userName.text = user.userNamae*/


        val article= articleList[position]
        holder.titleArticle.text = article.title
        holder.date.text = article.date

        holder.userName.text = article.userName


        holder.itemView.setOnClickListener { view->

            val activity:AppCompatActivity = view.context as AppCompatActivity
            val bundle = Bundle()

            val fragment = ArticleInformation.newInstance()
            fragment.arguments = bundle
            bundle.putParcelable("taskKey",article)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment,fragment)
                //.addToBackStack("Test")
                .commit()
        }



       /* holder.titleArticle.setOnClickListener { view->

            //findNavController().navigate(R.id.action_sign_in_to_signUP)


            val activity:AppCompatActivity = view.context as AppCompatActivity
            val bundle = Bundle()

            val fragment = ArticleInformation.newInstance()
            fragment.arguments = bundle
            bundle.putParcelable("taskKey",article)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment,fragment)
                .addToBackStack("Test").commit()
        }*/





        //-------------------------------------------------------------------------
        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/${article.articleImage.toString()}")

        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            holder.imageArticle.load(bitmap)


        }.addOnFailureListener{

        }

        //------------------------------------------------------------------------------------

    }

    override fun getItemCount(): Int {

        return articleList.size

    }




   public class MyViewHolder(itemView :View):RecyclerView.ViewHolder(itemView),View.OnClickListener{

       val titleArticle :TextView =itemView.findViewById(R.id.tvTitle_xml)
       val userName :TextView =itemView.findViewById(R.id.tvUserName_xml)
       val date :TextView =itemView.findViewById(R.id.tvDateItem_xml)
       val imageArticle :ImageView =itemView.findViewById(R.id.imageItem_xml)





       init {
           itemView.setOnClickListener(this)
       }
      override fun onClick(v: View?) {

          imageArticle.setOnClickListener {



          }

          Toast.makeText(itemView.context,"Name ${userName.text.toString()}",Toast.LENGTH_SHORT).show()
           Toast.makeText(itemView.context,"Title ${titleArticle.text.toString()}",Toast.LENGTH_SHORT).show()


       }




   }



}

