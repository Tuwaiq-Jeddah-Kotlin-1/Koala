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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.firestore.*


class HomePage : Fragment() {

    lateinit var binding: FragmentHomePageBinding


     var categorySelected:String?=null
    var categorySelected2:String?=null

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







       Log.e("categorySelected",categorySelected.toString())

        //loadArticle(categorySelected.toString())

        getAllArticles()


        Log.e("categorySelected22222222222222222222",categorySelected.toString())



       typeCategory()





    }

    fun loadArticle(typeCategory:String?=null){

        if(typeCategory.isNullOrEmpty()) {
            articleAdapter = ArticleAdapter(articleList)

            recyclerView.adapter = articleAdapter

           //GET all DATA
            getAllArticles()

        }else{


            articleAdapter = ArticleAdapter(articleList)

            recyclerView.swapAdapter(articleAdapter,false)

            typeCategory()
            //typeCategory()

        }

    }





    fun typeCategory(){

        val category = resources.getStringArray(R.array.Category)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)

        binding.spinnerCategoryXml.setAdapter(arrayAdapter)
        binding.spinnerCategoryXml.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                categorySelected = "${category[position]}"
               // loadArticle(categorySelected.toString())


            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                categorySelected = "${category[position]}"
                Toast.makeText(context,"you selected :  ${category[position]}", Toast.LENGTH_SHORT).show()



                /*if (categorySelected.toString()==categorySelected2.toString()){
                    Toast.makeText(context,"  categorySelected==categorySelected${categorySelected==categorySelected} :  ${category[position]}", Toast.LENGTH_SHORT).show()

                }else{




                    if(categorySelected == "All") {


                        articleAdapter = ArticleAdapter(articleList)

                        recyclerView.swapAdapter(articleAdapter,false)


                        //GET all DATA
                        getAllArticles()

                    }else{
                        articleAdapter = ArticleAdapter(articleList)
                        recyclerView.swapAdapter(articleAdapter,false)


                        Toast.makeText(context,"Workinnnnnnnnnnnng :  ${category[position]}", Toast.LENGTH_SHORT).show()
                        //typeCategory()

                        //categoryArticle("C1")
                        articleCategory("C1")


                    }
                }*/


                categorySelected2=categorySelected.toString()


              // articleCategory(categorySelected.toString())
            }
        }


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




    private fun getAllArticles(){

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").addSnapshotListener(object :EventListener<QuerySnapshot>{
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


    private fun articleCategory(categorySelected:String){

        fireStore = FirebaseFirestore.getInstance()

        fireStore.collection("Articles").whereEqualTo("category",categorySelected.toString()).addSnapshotListener(object :EventListener<QuerySnapshot>{
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
        fireStore.collection("Articles").whereEqualTo("category",typeCategory.toString())
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