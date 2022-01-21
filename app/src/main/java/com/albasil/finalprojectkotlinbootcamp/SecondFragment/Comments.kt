package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.albasil.finalprojectkotlinbootcamp.Adapter.CommentsAdapter
import com.albasil.finalprojectkotlinbootcamp.ViewModels.CommentsViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Comment
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentCommentsBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Comments : Fragment() {


    private lateinit var commentsList: MutableList<Comment>
    private lateinit var commentsAdapter: CommentsAdapter

    private val commentViewModel by activityViewModels<CommentsViewModel>()

    private val args by navArgs<CommentsArgs>()

    lateinit var binding: FragmentCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        commentsList = mutableListOf()
        commentsAdapter = CommentsAdapter(commentsList)
        binding.recyclerViewComment.adapter = commentsAdapter




        commentViewModel.getAllComment(args.artcileID, commentsList, viewLifecycleOwner)
            .observe(viewLifecycleOwner, {
                binding.recyclerViewComment.adapter = CommentsAdapter(commentsList)
                commentsAdapter.notifyDataSetChanged()
            })

        binding.buttonSendComment.setOnClickListener {

            // Date  object
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")
            val formatted = current.format(formatter)


            val comment = Comment()
            comment.userID = FirebaseAuth.getInstance().currentUser!!.uid
            comment.textContent = binding.editTextComment.text.toString()
            comment.articleID = args.artcileID
            comment.dateFormat = formatted



            view.let { commentViewModel.addComments(comment, it) }

            binding.editTextComment.text = null

        }

    }

}