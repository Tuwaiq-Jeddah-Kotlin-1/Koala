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
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserArticleBinding
import com.google.firebase.storage.FirebaseStorage
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
        binding.tvArticleUserXml.text=args.articleData.userName


        if(args.articleData.articleImage.isNullOrEmpty()){
            binding.imageArticleUserXml.visibility=View.GONE
        }else{
            getArticleImage(args.articleData.articleImage)

        }

    }


    fun getArticleImage(imagePath:String) {

        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesArticle/$imagePath")

        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            //  binding.userImageProfileXml.load(bitmap)
            binding.imageArticleUserXml.load(localFile)


        }.addOnFailureListener {

            binding.imageArticleUserXml.setImageResource(R.drawable.ic_category)


        }
    }

}