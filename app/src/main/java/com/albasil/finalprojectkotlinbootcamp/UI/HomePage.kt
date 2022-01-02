package com.albasil.finalprojectkotlinbootcamp.UI

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.MainActivity
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.FeatherViewModel
import com.albasil.finalprojectkotlinbootcamp.ViewModels.HomePageViewModel
import com.albasil.finalprojectkotlinbootcamp.ViewModels.ProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.firestore.*


class HomePage : Fragment() {

    lateinit var binding: FragmentHomePageBinding

    var categorySelected: String? = null

    private lateinit var homePageViewModel: HomePageViewModel


    private lateinit var articleList: MutableList<Article>
    private lateinit var articleAdapter: ArticleAdapter
    private  var  fireStore = FirebaseFirestore.getInstance()
    lateinit var viewModel: FeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)


        //----------------user RecyclerView-----------------------------------------
        binding.recyclerViewArticleXml.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerViewArticleXml.setHasFixedSize(true)
        articleList = mutableListOf()
        articleAdapter = ArticleAdapter(articleList)
        binding.recyclerViewArticleXml.adapter = articleAdapter





        //----------------------select category-----------------------------------
        val category = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)
        binding.spinnerCategoryXml.setAdapter(arrayAdapter)


        binding.spinnerCategoryXml.onItemClickListener =
            object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemClick(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    categorySelected = "${category[position]}"

                    Toast.makeText(context, " selected :  ${category[position]}", Toast.LENGTH_SHORT).show()

                    binding.recyclerViewArticleXml.swapAdapter(articleAdapter, false)

                    //**********************************
                    viewModel = (activity as MainActivity).viewModel
                    if (viewModel.hasInternetConnection()){
                       loadArticle(categorySelected)
                        binding.imageView6.visibility = View.GONE
                    }else{
                        binding.imageView6.visibility = View.VISIBLE
                    }

                }
            }

//***********************************************************************************************************************

        viewModel = (activity as MainActivity).viewModel

        if (viewModel.hasInternetConnection()){

           homePageViewModel.getAllArticles(articleList, viewLifecycleOwner)
                .observe(viewLifecycleOwner, {
                    binding.recyclerViewArticleXml.adapter = ArticleAdapter(articleList)
                    articleAdapter.notifyDataSetChanged()
                })

            Toast.makeText(context, "hasInternetConnection() ${ viewModel.hasInternetConnection()}", Toast.LENGTH_SHORT).show()

            binding.imageView6.visibility = View.GONE
        }else{
            binding.imageView6.visibility = View.VISIBLE
            binding.imageView6.setOnClickListener {


                Toast.makeText(context, "hasInternetConnection() ${ viewModel.hasInternetConnection()}", Toast.LENGTH_SHORT).show()
            }
        }


        binding.addArticleFlotButton.setOnClickListener{

            findNavController().navigate(R.id.addArticle)
        }

    }





    fun loadArticle(typeCategory: String? = null) {

        if (typeCategory.isNullOrEmpty() || typeCategory=="All") {

            binding.recyclerViewArticleXml.adapter = articleAdapter

            //GET all DATA
            binding.recyclerViewArticleXml.swapAdapter(articleAdapter, false)

            //---------------Remove All Articles--------------------------------------------------------
            homePageViewModel.removeAllArticles(articleList, viewLifecycleOwner)
                .observe(viewLifecycleOwner, {
                    binding.recyclerViewArticleXml.adapter = ArticleAdapter(articleList)
                    articleAdapter.notifyDataSetChanged()
                })

            //----------------------getAllMyArticles-----------------------------------
            homePageViewModel.getAllArticles(articleList, viewLifecycleOwner)
                .observe(viewLifecycleOwner, {
                    binding.recyclerViewArticleXml.adapter = ArticleAdapter(articleList)
                    articleAdapter.notifyDataSetChanged()
                })

        } else {
            binding.recyclerViewArticleXml.swapAdapter(articleAdapter, false)

            //---------------Remove All Articles--------------------------------------------------------
            homePageViewModel.removeAllArticles(articleList, viewLifecycleOwner)
                .observe(viewLifecycleOwner, {
                    binding.recyclerViewArticleXml.adapter = ArticleAdapter(articleList)
                    articleAdapter.notifyDataSetChanged()
                })
            articleCategory(typeCategory.toString())
        }

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