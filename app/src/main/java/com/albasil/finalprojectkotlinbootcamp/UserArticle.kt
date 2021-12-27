package com.albasil.finalprojectkotlinbootcamp

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.ArticleInformationArgs
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserArticleBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import java.io.File


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



        binding.tvArticleCategory.setText(args.articleData.category.toString())
        binding.articleDescraptionXml.setText(args.articleData.description.toString())
        binding.tvArticleUserXml.setText(args.articleData.userName.toString())
        binding.articleTitleXml.setText(args.articleData.title.toString())
        binding.articleDateXml.setText(args.articleData.date)


        getUserPhoto(args.articleData.articleImage)
    }


    fun getUserPhoto(imagePath:String) {

        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesArticle/$imagePath")

        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {


            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            //  binding.userImageProfileXml.load(bitmap)
            binding.imageArticleUserXml.load(localFile)


        }.addOnFailureListener {

            binding.imageArticleUserXml.setImageResource(R.drawable.ic_category)

            // Toast.makeText(context, "Failed image ", Toast.LENGTH_SHORT).show()

        }
    }

}