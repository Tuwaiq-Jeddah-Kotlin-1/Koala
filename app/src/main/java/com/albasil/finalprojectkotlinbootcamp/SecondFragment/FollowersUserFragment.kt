package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.`FollowersAndFollowingAdapter`
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFollowersUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.firestore.*


class FollowersUserFragment : Fragment() {

    private val args by navArgs<FollowersUserFragmentArgs>()

    lateinit var binding: FragmentFollowersUserBinding

    private val fireStore = FirebaseFirestore.getInstance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var followersList: ArrayList<Users>
    private lateinit var aFollowersAndFollowingAdapter: `FollowersAndFollowingAdapter`


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFollowersUserBinding.inflate(inflater, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recyclerView = view.findViewById(R.id.followersUserFragmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        followersList = arrayListOf<Users>()

        aFollowersAndFollowingAdapter = `FollowersAndFollowingAdapter`(followersList)

        recyclerView.adapter = aFollowersAndFollowingAdapter

        Toast.makeText(context,"Good",Toast.LENGTH_LONG).show()
        Toast.makeText(context,args.followersOrFollowing.toString(),Toast.LENGTH_LONG).show()

        val type=args.followersOrFollowing

        if (type=="Followers"){

            getAllArticles("Followers")

        }else{
            getAllArticles("Following")

        }

    }


    private fun getAllArticles(type:String) {


        val uId = FirebaseAuth.getInstance().currentUser?.uid
        fireStore.collection("Users").document(uId.toString())
            .collection(type.toString())
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

                    aFollowersAndFollowingAdapter.notifyDataSetChanged()


                }

            })


    }



}
