package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.FavoriteAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.FollowersArticlesAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FavoriteViewModel
import com.albasil.finalprojectkotlinbootcamp.ViewModels.ProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFavoriteBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class FavoriteFragment : Fragment() {
    val myID = FirebaseAuth.getInstance().currentUser?.uid

    //lateinit var binding: FragmentFavoriteBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleAdapter: FavoriteAdapter
    private lateinit var fireStore: FirebaseFirestore

    private lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.favoriteArticleRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)

        articleList = arrayListOf<Article>()

        articleAdapter = FavoriteAdapter(articleList)

        recyclerView.adapter = articleAdapter



        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        favoriteViewModel.favoriteArticles(myID.toString(), articleList, viewLifecycleOwner)
            .observe(viewLifecycleOwner, {
                recyclerView.adapter = FavoriteAdapter(articleList)
                articleAdapter.notifyDataSetChanged()
            })

    }



}