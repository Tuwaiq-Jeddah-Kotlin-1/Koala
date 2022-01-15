package com.albasil.finalprojectkotlinbootcamp.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.ProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_article.*
import kotlinx.android.synthetic.main.item_article_user_profile.*
import kotlinx.android.synthetic.main.up_date_user_information.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Profile : Fragment() {

    val myID = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var binding: FragmentProfileBinding
    private lateinit var imageUrl: Uri
    private lateinit var userPhoneNumber: String
    private lateinit var articleList: MutableList<Article>
    private lateinit var articleAdapter: ArticleUserProfileAdapter
    private lateinit var userInfo : Users

    var fireStore = FirebaseFirestore.getInstance()

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        //--------------User Photo-------------------------------------
        getUserPhoto()

        //----------------------------getUserInformation-----------------------------------------------------------------
        userInfo= Users()
        profileViewModel.getUserInformation(myID.toString(),userInfo,viewLifecycleOwner).observe(viewLifecycleOwner,{
            binding.userNameXml.text = userInfo.userName
            binding.userInfoXml.text= userInfo.moreInfo.toString()
            userPhoneNumber = userInfo.userPhone

        })

        //----------------user RecyclerView-----------------------------------------
        binding.userProfileRecyclerViewXml.layoutManager = GridLayoutManager(context, 1)
        binding.userProfileRecyclerViewXml.setHasFixedSize(true)
        articleList = mutableListOf()
        articleAdapter = ArticleUserProfileAdapter(articleList)
        binding.userProfileRecyclerViewXml.adapter = articleAdapter

        //----------------------getAllMyArticles-----------------------------------
        profileViewModel.getAllMyArticles(myID.toString(), articleList, viewLifecycleOwner)
            .observe(viewLifecycleOwner, {
                binding.userProfileRecyclerViewXml.adapter = ArticleUserProfileAdapter(articleList)
                binding.numberOrArticle.setText(articleList.size.toString())
                articleAdapter.notifyDataSetChanged()
            })

        //-----------------------------------------------------
        binding.tvFollowersXml.setOnClickListener {
            val check = ProfileDirections.actionProfileToFollowersUserFragment("Followers")
            NavHostFragment.findNavController(this).navigate(check) }
        binding.tvFollowingXml.setOnClickListener {
            val check = ProfileDirections.actionProfileToFollowersUserFragment("Following")
            NavHostFragment.findNavController(this).navigate(check)
        }

        binding.userImageProfileXml.setOnClickListener {
            selectImage()

        }

        binding.addInformationXml.setOnClickListener {
            showDialogUserInfo()
        }

        binding.upDateButtonXml.setOnClickListener {
            profileViewModel.getUserInformation(myID.toString(),userInfo,viewLifecycleOwner).observe(viewLifecycleOwner,{
                userPhoneNumber = userInfo.userPhone
                upDateUserInfoBottomSheet(userPhoneNumber)
            })
        }

        val deleteArticle = ItemTouchHelper(itemTouchHelper)
        deleteArticle.attachToRecyclerView( binding.userProfileRecyclerViewXml)

        countNumberOfFollowers(myID.toString())

    }


    // Swipe
    val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return true }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val article = articleList[position]
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    articleList.remove(article)
                    articleAdapter.notifyItemRemoved(position)


                    profileViewModel.deleteArticle(article.articleID)
                    view?.let {
                        Snackbar.make(it, "Article Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {

                                view.let { profileViewModel.addArticle(article,it) }

                            }
                            show()

                        }}
                    }
                ItemTouchHelper.RIGHT -> {
                    upDateUserArticle(article)
                }
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
             //   .addSwipeLeftBackgroundColor(android.graphics.Color.parseColor("#FF0000"))
            //    .addSwipeRightBackgroundColor(android.graphics.Color.parseColor("#3EFD4C"))
                .addSwipeRightActionIcon(R.drawable.ic_edit_24)
                .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
                .create().decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }


    private fun upDateUserArticle(article: Article) {
        val article_data = Article()
        article_data.title = article.title
        article_data.userName = article.userName
        article_data.date = article.date.toString()
        article_data.category = article.category
        article_data.description = article.description
        article_data.articleImage = article.articleImage.toString()
        article_data.articleID = article.articleID.toString()
        val itemData = ProfileDirections.actionProfileToEditArticle(article_data)
        NavHostFragment.findNavController(this@Profile).navigate(itemData)
    }


    //REPO
    private fun countNumberOfFollowers(myID: String){
        //Count Number FOLLOWERS
        fireStore.collection("Users").document(myID)
            .collection("Followers").get()
            .addOnSuccessListener {
                var numberOfFollowers = it.size()
                binding.userFollowersXml.text = numberOfFollowers.toString()
            }
//------------------------------------------------------------------------------------
        //Count Number FOLLOWING
        fireStore.collection("Users").document(myID)
            .collection("Following").get().addOnSuccessListener {
                var numberOfFollowing = it.size()
                binding.userFollowingXml.text = numberOfFollowing.toString()
            }
    }

    fun showDialogUserInfo() {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.user_information))
        val view: View = layoutInflater.inflate(R.layout.add_user_information_dialog, null)
        val editTextExperience: EditText = view.findViewById(R.id.editTextAddExperience)
        builder.setView(view)
        editTextExperience.setText(binding.userInfoXml.text.toString())
        builder.setPositiveButton(getString(R.string.save_changed)) { _, _ ->

            profileViewModel.addUserInfo(myID.toString(), editTextExperience.text.toString())
            binding.userInfoXml.setText(editTextExperience.text.toString())

        }
        builder.setNegativeButton(getString(R.string.cancel), { _, _ -> })

        builder.show()
    }

    private fun upDateUserInfoBottomSheet(userPhoneNumber:String) {
        val view: View = layoutInflater.inflate(R.layout.up_date_user_information, null)
        val builder = BottomSheetDialog(requireView().context as Context)
        builder.setTitle(getString(R.string.upDateInformation))

        val upDateInfoButton = view.upDateInfoButton_xml

        view.editTextTextUserName.setText(binding.userNameXml.text)
        view.editTextPhone.setText(userPhoneNumber)

        upDateInfoButton.setOnClickListener {
            if (view.editTextTextUserName.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().isNotEmpty() &&
                view.editTextPhone.text.toString().length == 10
            ) {
                binding.userNameXml.setText(view.editTextTextUserName.text.toString())
                profileViewModel.upDateUserInformation(view.editTextTextUserName.text.toString(), view.editTextPhone.text.toString())
                builder.dismiss()
            } else {
                Toast.makeText(context, "Please enter correct Information!!! ", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setContentView(view)
        builder.show()
    }

    //--------------------------------------------------------------------------------------
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUrl = data?.data!!

            binding.userImageProfileXml.setImageURI(imageUrl)

            upLoadImage()
        }

    }

    //repo or fire storage
    fun upLoadImage() {


        val storageReference = FirebaseStorage.getInstance().getReference("imagesUsers/${myID.toString()}")

        storageReference.putFile(imageUrl)
            .addOnSuccessListener {

                getUserPhoto()
            }.addOnFailureListener {
            }
    }

    fun getUserPhoto() {
        val imageName = "${FirebaseAuth.getInstance().currentUser?.uid}"
        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesUsers/$imageName")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            binding.userImageProfileXml.load(localFile)

        }.addOnFailureListener {
        }
    }
    //--------------------------------------------------------------------------------------


}
