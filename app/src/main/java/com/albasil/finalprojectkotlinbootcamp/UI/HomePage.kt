package com.albasil.finalprojectkotlinbootcamp.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentEditArticleBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.firestore.*


class HomePage : Fragment() {

    lateinit var binding: FragmentHomePageBinding


    private lateinit var recyclerView :RecyclerView
    private lateinit var articleList :ArrayList<Article>
    private lateinit var userList :ArrayList<Users>
    private lateinit var articleAdapter :ArticleAdapter
    private lateinit var fireStore :FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomePageBinding.inflate(inflater,container,false)




        return binding.root
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



     //   EventChangeListener()

        typeCategory()
        //sort()



    }


    fun typeCategory(){
        //---------------------------------------------------------------------
        lateinit var categorySelected:String


        val category = resources.getStringArray(R.array.Category)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)

        binding.spinnerCategoryXml.setAdapter(arrayAdapter)
        binding.spinnerCategoryXml.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                categorySelected = "${category[position]}"

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                categorySelected = "${category[position]}"
                Toast.makeText(context,"you selected :  ${category[position]}", Toast.LENGTH_SHORT).show()

              categoryArticle(categorySelected.toString())

            }
        }



        //----------------------------------------------------------------------


    }

    fun sort(){
        //---------------------------------------------------------------------
        lateinit var categorySelected:String


        val category = resources.getStringArray(R.array.sortArticle)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)

        binding.spinnerSortItemXml.setAdapter(arrayAdapter)
        binding.spinnerSortItemXml.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                categorySelected = "${category[position]}"

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                categorySelected = "${category[position]}"
                Toast.makeText(context,"you selected :  ${category[position]}", Toast.LENGTH_SHORT).show()
            }
        }



        //----------------------------------------------------------------------


    }

    private fun EventChangeListener(sortArticle:String?="date",){


        Toast.makeText(context,sortArticle.toString(),Toast.LENGTH_SHORT).show()
        /* if (sortArticle.isNullOrEmpty()){
            sortArticle.toString()="data"
        }*/
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").orderBy("${sortArticle.toString()}").addSnapshotListener(object :EventListener<QuerySnapshot>{
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


    private fun categoryArticle(typeCategory:String){


        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles")
            .whereEqualTo("category",typeCategory.toString())
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
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