package com.albasil.finalprojectkotlinbootcamp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.FollowersUserFragmentDirections
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class `FollowersAndFollowingAdapter`(private val followersList:ArrayList<Users>): RecyclerView.Adapter<`FollowersAndFollowingAdapter`.FollowersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_folllowers, parent, false)
        return FollowersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val followers = followersList[position]
        holder.followersId = followers.userId
            val db = FirebaseFirestore.getInstance()
            db.collection("Users").document(followers.userId)
                .get().addOnCompleteListener {it

                    if (it.result?.exists()!!) {
                        var name = it.result!!.getString("userName")
                        holder.followersName.text = name
                    } else {
                    }
                }

        holder.followersName.setOnClickListener {
            val userInformation =FollowersUserFragmentDirections.actionFollowersUserFragmentToUserProfile(holder.followersId.toString())
            NavHostFragment.findNavController(holder.itemView.findFragment()).navigate(userInformation)
        }
    }


    override fun getItemCount(): Int {

        return followersList.size

    }

    class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val userID = FirebaseAuth.getInstance().currentUser?.uid

        lateinit var followersId:String
        val followersName: TextView = itemView.findViewById(R.id.followersName)
    }

}