package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.albasil.finalprojectkotlinbootcamp.Adapter.AllUsers
import com.albasil.finalprojectkotlinbootcamp.ViewModels.AllUsersViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.AllUsersFragmentBinding

import com.google.firebase.firestore.*


class AllUsersFragment : Fragment() {

    lateinit var binding: AllUsersFragmentBinding

    private lateinit var allUsersViewModel: AllUsersViewModel


    private lateinit var usersList: MutableList<Users>
    private lateinit var followersAdapter: AllUsers
    private lateinit var fireStore: FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = AllUsersFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allUsersViewModel = ViewModelProvider(this).get(AllUsersViewModel::class.java)


        binding.allUsersRecyclerView.layoutManager = GridLayoutManager(context,1)
        binding.allUsersRecyclerView.setHasFixedSize(true)

        usersList = mutableListOf()

        followersAdapter = AllUsers(usersList)
        binding.allUsersRecyclerView.adapter = followersAdapter



       getAllUsers()//getAllUsers

        binding.searchUser.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                followersAdapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun getAllUsers() {

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }

                    if (value != null) {
                        followersAdapter= AllUsers(value.toObjects(Users::class.java))
                        binding.allUsersRecyclerView.adapter = followersAdapter
                    }

                }

            })


    }

}