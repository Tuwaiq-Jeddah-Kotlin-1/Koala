package com.albasil.finalprojectkotlinbootcamp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.ArticleInformationArgs
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserArticleBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserProfileBinding
import kotlinx.android.synthetic.main.fragment_article_information.view.*


class UserArticle : Fragment() {
    private val args by navArgs<UserArticleArgs>()

    lateinit var binding: FragmentUserArticleBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserArticleBinding.inflate(inflater,container,false)




        return binding.root





    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvTitleArticleUserXml.setText("${args.articleData.title.toString()}\n" +
                "${args.articleData.userName.toString()}\n" +
                "${args.articleData.date.toString()}\n" +
                "${args.articleData.category.toString()}")


    }
}