package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.FollowersArticlesAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentFollowersArticlesBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class FollowersArticlesFragment : Fragment() {

    lateinit var binding: FragmentFollowersArticlesBinding


    private lateinit var recyclerView: RecyclerView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleAdapter: FollowersArticlesAdapter
    private lateinit var fireStore: FirebaseFirestore



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
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.setHasFixedSize(true)

        articleList = arrayListOf<Article>()

        articleAdapter = FollowersArticlesAdapter(articleList)
        recyclerView.adapter = articleAdapter


        getAllArticles()
    }




    private fun getAllArticles() {

        val userId= FirebaseAuth.getInstance().currentUser?.uid

        fireStore = FirebaseFirestore.getInstance()
        // fireStore.collection("Users").document(userId.toString()).collection("Favorite")Following
        fireStore.collection("Users")//.document(userId.toString()).collection("Following")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.add(dc.document.toObject(Article::class.java))

                        }
                    }
                    articleAdapter.notifyDataSetChanged()

                }

            })


    }

}