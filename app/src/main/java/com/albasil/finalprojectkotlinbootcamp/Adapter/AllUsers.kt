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
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AllUsers(internal val usersList: MutableList<Users>) :
    RecyclerView.Adapter<AllUsers.FollowersArticlesViewHolder>(), Filterable {
    //--------------------------------------------------------------------------------------------------
    var filterUsersList = mutableListOf<Users>()

    init {
        usersList.forEach {
            filterUsersList.add(it)
        }
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<Users> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(filterUsersList)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in filterUsersList) {
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
            usersList.clear()
            usersList.addAll(results.values as List<Users>)
            notifyDataSetChanged()
        }
    }


    //---------------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersArticlesViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return FollowersArticlesViewHolder(itemView)

    }


    override fun onBindViewHolder(holder: FollowersArticlesViewHolder, position: Int) {
        val followersArticle = usersList[position]
        holder.userID = followersArticle.userId
        holder.image = followersArticle.userPhone



        holder.userName2.visibility = View.GONE
        holder.articleCategory.visibility = View.GONE
        holder.dateArticle.visibility = View.GONE
        holder.linearLayOutFavorite.visibility = View.GONE

        val storageRef = FirebaseStorage.getInstance().reference
            .child("//imagesUsers/${followersArticle.userId.toString()}")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.userImage.load(bitmap)
        }.addOnFailureListener {}
        //--------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------
        firestore.collection("Users").document(holder.userID).get()
            .addOnCompleteListener { it
                if (it.result?.exists()!!) {
                    val userName = it.result!!.getString("userName")
                    holder.userName.text = userName.toString()
                } else {

                }
            }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class FollowersArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        lateinit var userID: String

        lateinit var image: String
        lateinit var articleDescription: String


        val userName: TextView = itemView.findViewById(R.id.tvTitle_xml)
        val articleCategory: TextView = itemView.findViewById(R.id.tvCategoryItem_xml)

        val userName2: TextView = itemView.findViewById(R.id.tvUserName_xml)
        val dateArticle: TextView = itemView.findViewById(R.id.articleDate)
        val userImage: ImageView = itemView.findViewById(R.id.imageItem_xml)
        val linearLayOutFavorite: LinearLayout = itemView.findViewById(R.id.linearLayOutFavorite)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View?) {
            if (uId.toString() == userID.toString()) {
                NavHostFragment.findNavController(itemView.findFragment()).navigate(R.id.profile)

            } else {
                val userInformation =
                    TabBarFragmentDirections.actionTabBarFragmentToUserProfile(userID.toString())
                NavHostFragment.findNavController(itemView.findFragment()).navigate(userInformation)

            }

        }
    }


}
