package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.FavoriteAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.`FollowersAndFollowingAdapter`
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FollowersUserViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFollowersUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.firestore.*


class FollowersUserFragment : Fragment() {

    private val args by navArgs<FollowersUserFragmentArgs>()

    lateinit var binding: FragmentFollowersUserBinding
    private lateinit var followersAndFollowingViewModel: FollowersUserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var followersList: ArrayList<Users>
    private lateinit var FollowersAndFollowingAdapter: `FollowersAndFollowingAdapter`


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{


        binding = FragmentFollowersUserBinding.inflate(inflater, container, false)

        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.followersUserFragmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        followersList = ArrayList()

        FollowersAndFollowingAdapter = `FollowersAndFollowingAdapter`(followersList)

        recyclerView.adapter = FollowersAndFollowingAdapter


        val type = args.followersOrFollowing

        followersAndFollowingViewModel =
            ViewModelProvider(this).get(FollowersUserViewModel::class.java)

        if (type == "Followers") {
            followersAndFollowingViewModel.getFollowersAndFollowing(
                "Followers",
                followersList,
                viewLifecycleOwner
            )
                .observe(viewLifecycleOwner, {
                    recyclerView.adapter = FollowersAndFollowingAdapter(followersList)
                    FollowersAndFollowingAdapter.notifyDataSetChanged()
                })


        } else {

            followersAndFollowingViewModel.getFollowersAndFollowing(
                "Following",
                followersList,
                viewLifecycleOwner
            )
                .observe(viewLifecycleOwner, {
                    recyclerView.adapter = FollowersAndFollowingAdapter(followersList)
                    FollowersAndFollowingAdapter.notifyDataSetChanged()
                })


        }

    }

}
