package com.albasil.finalprojectkotlinbootcamp.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.firestore.*


class HomePage : Fragment() {

    lateinit var binding: FragmentHomePageBinding


    var categorySelected: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var newArticleList: ArrayList<Article>
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var fireStore: FirebaseFirestore

    //  private  lateinit var addEventBar: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recyclerViewArticle_xml)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)

        articleList = arrayListOf<Article>()
        newArticleList = arrayListOf<Article>()
        articleAdapter = ArticleAdapter(articleList)
        recyclerView.adapter = articleAdapter



        //*************************************************************************************
        val category = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)
        binding.spinnerCategoryXml.setAdapter(arrayAdapter)
        binding.spinnerCategoryXml.onItemClickListener =
            object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    // loadArticle(categorySelected.toString())
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemClick(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    categorySelected = "${category[position]}"

                    Toast.makeText(context, " selected :  ${category[position]}", Toast.LENGTH_SHORT).show()

                    recyclerView.swapAdapter(articleAdapter, false)

                    loadArticle(categorySelected)

                }
            }

//***********************************************************************************************************************
        //getAllArticles()

        Toast.makeText(context, "you selected :  ${categorySelected}", Toast.LENGTH_SHORT).show()

        loadArticle(categorySelected)




    }


    fun loadArticle(typeCategory: String? = null) {

        if (typeCategory.isNullOrEmpty()) {
          //  articleAdapter = ArticleAdapter(articleList)

            recyclerView.adapter = articleAdapter

            //GET all DATA
            recyclerView.swapAdapter(articleAdapter, false)

            getAllArticles()

        } else {
           // articleAdapter = ArticleAdapter(articleList)
           recyclerView.swapAdapter(articleAdapter, false)
            removeAllArticles()

            articleCategory(typeCategory.toString())

        }

    }

    private fun removeAllArticles() {


        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.remove(dc.document.toObject(Article::class.java))


                        }
                    }

                    articleAdapter.notifyDataSetChanged()


                }

            })    }


    private fun getAllArticles() {


        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles")
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


    private fun articleCategory(typeCategory:String) {
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").whereEqualTo("category", typeCategory.toString())
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