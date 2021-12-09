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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ArticleAdapter(private val articleList:ArrayList<Article>):RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val article :Article = articleList[position]

        holder.titleArticle.text = article.title
        holder.date.text = article.date


        //-------------------------------------------------------------------------
        val storageRef= FirebaseStorage.getInstance().reference
            .child("/imagesArticle/${article.articleImage.toString()}")

        val localFile = File.createTempFile("tempImage","jpg")

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            holder.imageArticle.load(bitmap)

            Log.d("Tag ","https://firebasestorage.googleapis.com/v0/b/final-project-kotlin-bootcamp.appspot.com/o/imagesArticle%2F${article.articleImage.toString()}?alt=media&token=17a7fbb0-29df-4220-973a-5acb5040a0a5")
        //    holder.imageArticle.setImageBitmap(bitmap)


        }.addOnFailureListener{

        }

        //------------------------------------------------------------------------------------

    }

    override fun getItemCount(): Int {

        return articleList.size

    }




   public class MyViewHolder(itemView :View):RecyclerView.ViewHolder(itemView) {

       val titleArticle :TextView =itemView.findViewById(R.id.tvTitle_xml)
       val date :TextView =itemView.findViewById(R.id.tvDateItem_xml)
       val imageArticle :ImageView =itemView.findViewById(R.id.imageItem_xml)



    }

}

