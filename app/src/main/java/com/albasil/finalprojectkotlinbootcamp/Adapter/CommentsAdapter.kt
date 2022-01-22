package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.renderscript.Float4
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.CommentsDirections
import com.albasil.finalprojectkotlinbootcamp.UI.TabBarFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Comment
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class CommentsAdapter( val commentList: MutableList<Comment>):
    RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    val myID=FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.CommentHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentsAdapter.CommentHolder, position: Int) {

        val comments =commentList[position]

        holder.contentText.text = comments.textContent
        holder.userName.text = comments.userName
        holder.dateComment.text = comments.dateFormat

        if (comments.userID == myID.toString()){
            holder.cardComment.setBackgroundColor(Color.GRAY)
            holder.cardComment.radius
        }else{
            holder.cardComment.setBackgroundColor(Color.WHITE)

        }


        //----------------check User Name ----------------------------------------------
        firestore.collection("Users").document(comments.userID).get()
            .addOnCompleteListener {it
                if (it.result?.exists()!!) {
                    val userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {
                }
            }


         holder.userName.setOnClickListener {
         if (comments.userID.equals(myID.toString())){
             findNavController(holder.itemView.findFragment()).navigate(R.id.profile)
         }else{
             val userInformation =
                 CommentsDirections.actionCommentsToUserProfile(comments.userID.toString())
             findNavController(holder.itemView.findFragment()).navigate(userInformation)

         }


   }


    }

    override fun getItemCount()=commentList.size



    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val contentText:TextView=itemView.findViewById(R.id.contentText)
        val userName:TextView=itemView.findViewById(R.id.userName)
        val dateComment:TextView=itemView.findViewById(R.id.dateComment)

        val cardComment:CardView=itemView.findViewById(R.id.cardComment)

    }

}
