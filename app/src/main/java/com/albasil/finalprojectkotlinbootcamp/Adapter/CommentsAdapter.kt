package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.R
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.CommentHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentsAdapter.CommentHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentsAdapter.CommentHolder, position: Int) {

        val comments =commentList[position]

        holder.contentText.text = comments.textContent
        holder.userName.text = comments.userName
        holder.dateComment.text = comments.dateFormat


        //----------------check User Name ----------------------------------------------
        firestore.collection("Users").document(comments.userID).get()
            .addOnCompleteListener {it
                if (it.result?.exists()!!) {
                    var userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {
                }
            }

    }

    override fun getItemCount()=commentList.size



    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val contentText:TextView=itemView.findViewById(R.id.contentText)
        val userName:TextView=itemView.findViewById(R.id.userName)
        val dateComment:TextView=itemView.findViewById(R.id.dateComment)
    }

}
