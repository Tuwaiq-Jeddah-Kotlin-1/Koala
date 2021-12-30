package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.FollowersArticlesAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFollowersArticlesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class FollowersArticlesFragment : Fragment() {

   // lateinit var binding: FragmentFollowersArticlesBinding


    private lateinit var recyclerView: RecyclerView
    private lateinit var usersList: MutableList<Users>
    private lateinit var FollowersAdapter: FollowersArticlesAdapter
    private lateinit var fireStore: FirebaseFirestore

    private lateinit var searchView:SearchView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers_articles, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        recyclerView = view.findViewById(R.id.followersArticlesRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)

        usersList = mutableListOf()

        FollowersAdapter = FollowersArticlesAdapter(usersList)
        recyclerView.adapter = FollowersAdapter


        getAllUsers()
        searchView = view.findViewById(R.id.searchUser)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                FollowersAdapter.filter.filter(newText)
                return false
            }

        })
    }




    private fun getAllUsers() {

        val userId= FirebaseAuth.getInstance().currentUser?.uid

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }


                    if (value != null) {
                        FollowersAdapter= FollowersArticlesAdapter(value.toObjects(Users::class.java))
                        recyclerView.adapter = FollowersAdapter
                    }
//                    for (dc: DocumentChange in value?.documentChanges!!) {
//                        if (dc.type == DocumentChange.Type.ADDED) {
//                            usersList.add(dc.document.toObject(Users::class.java))
//                        }
//                    }
//                    FollowersAdapter.notifyDataSetChanged()

                }

            })


    }

}