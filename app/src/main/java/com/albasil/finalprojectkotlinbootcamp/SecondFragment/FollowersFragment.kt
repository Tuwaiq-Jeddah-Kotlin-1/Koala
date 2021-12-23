package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.FollowersAdapter
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFollowersBinding
import com.google.firebase.firestore.*

class FollowersFragment : Fragment() {
    lateinit var binding: FragmentFollowersBinding

    private val fireStore =FirebaseFirestore.getInstance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var followersList: ArrayList<Users>
    private lateinit var followersAdapter: FollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.actions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        followersList = arrayListOf<Users>()

        followersAdapter = FollowersAdapter(followersList)

        recyclerView.adapter = followersAdapter


       // getAllArticles()



    }






/*

    private fun getAllArticles() {


        fireStore.collection("Articles").orderBy("username")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            followersList.add(dc.document.toObject(Users::class.java))


                        }
                    }

                    followersAdapter.notifyDataSetChanged()


                }

            })


    }


    */
}