package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleAdapter
import com.albasil.finalprojectkotlinbootcamp.Adapter.CommentsAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.CommentsViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Comment
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentCommentsBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Comments : Fragment() {


    private lateinit var articleList: MutableList<Comment>
    private lateinit var articleAdapter: CommentsAdapter

    private val commentViewModel  by activityViewModels<CommentsViewModel>()

    private val args by navArgs<CommentsArgs>()

    lateinit var binding: FragmentCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment


        binding = FragmentCommentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //----------------user RecyclerView-----------------------------------------
        binding.recyclerViewComment.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerViewComment.setHasFixedSize(true)
        articleList = mutableListOf()
        articleAdapter = CommentsAdapter(articleList)
        binding.recyclerViewComment.adapter = articleAdapter




        commentViewModel.getAllComment(args.artcileID,articleList, viewLifecycleOwner)
            .observe(viewLifecycleOwner, {
                binding.recyclerViewComment.adapter = CommentsAdapter(articleList)
                articleAdapter.notifyDataSetChanged()
            })



        binding.buttonSendComment.setOnClickListener {

            // Date  object
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")
            val formatted = current.format(formatter)


            val comm=Comment()
                comm.userID= FirebaseAuth.getInstance().currentUser!!.uid
            comm.textContent= binding.editTextComment.text.toString()
            comm.articleID=args.artcileID
            comm.dateFormat=formatted



            view.let { commentViewModel.addComments(comm, it) }

           // binding.editTextComment.text = null

        }

    }

}