package com.albasil.finalprojectkotlinbootcamp.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.google.firebase.firestore.*


class HomePage : Fragment() {

    private lateinit var recyclerView :RecyclerView
    private lateinit var articleList :ArrayList<Article>
    private lateinit var userList :ArrayList<Users>
    private lateinit var articleAdapter :ArticleAdapter
    private lateinit var fireStore :FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewArticle_xml)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        articleList = arrayListOf()
        userList = arrayListOf()

        articleAdapter = ArticleAdapter(articleList)

        recyclerView.adapter = articleAdapter

        EventChangeListener()



    }

    private fun EventChangeListener(){

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles")
            .whereEqualTo("category","C1")
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null){
                        Log.e("Firestore",error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!){

                        if (dc.type == DocumentChange.Type.ADDED){
                            articleList.add(dc.document.toObject(Article::class.java))

                        }
                    }

                    articleAdapter.notifyDataSetChanged()


                }

            })







    }





}