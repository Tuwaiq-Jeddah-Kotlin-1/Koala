package com.albasil.finalprojectkotlinbootcamp.SecondFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albasil.finalprojectkotlinbootcamp.Adapter.ArticleUserProfileAdapter
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.ViewModels.ProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.ViewModels.UserProfileViewModel
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UserProfile : Fragment() {

    lateinit var binding: FragmentUserProfileBinding
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleAdapter: ArticleUserProfileAdapter

    private lateinit var userInfo : Users

    private  var fireStore = FirebaseFirestore.getInstance()

    val myId = FirebaseAuth.getInstance().currentUser?.uid


    private lateinit var userProfileViewModel: UserProfileViewModel
    private val args by navArgs<UserProfileArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)


        //----------------user RecyclerView-----------------------------------------
        binding.userRecyclerViewXml.layoutManager = GridLayoutManager(context, 2)
        binding.userRecyclerViewXml.setHasFixedSize(true)
        articleList = arrayListOf()
        articleAdapter = ArticleUserProfileAdapter(articleList)
        binding.userRecyclerViewXml.adapter = articleAdapter


        //----------------------getUserPhoto-----------------------------------
        getUserPhoto(args.userID.toUri())

        //----------------------getArticlesUser-----------------------------------

        userProfileViewModel.getUserArticles(args.userID.toString(), articleList, viewLifecycleOwner)
            .observe(viewLifecycleOwner, {
                binding.userRecyclerViewXml.adapter = ArticleUserProfileAdapter(articleList)
                articleAdapter.notifyDataSetChanged()
            })

        //----------------------------getUserInformation-----------------------------------------------------------------
        userInfo= Users()
        userProfileViewModel.getUserInformation(args.userID.toString(),userInfo,viewLifecycleOwner).observe(viewLifecycleOwner,{
            binding.userNameXml.text = userInfo.userName
            binding.userInfoXml.text= userInfo.moreInfo.toString()

        })

        //--------------------------------------------------------------------------------------------
        binding.userCallXml.setOnClickListener {
            callUser("${userInfo.userPhone.toString()}")
        }

        binding.userEmailXml.setOnClickListener {
            userEmail(userInfo.userEmail.toString())
        }
        //--------------------------------------------------------------------------------------------

        binding.userInforCallXml.setOnClickListener {

            expandedCallUser()

        }//linearLayOutUserInfo_xml
        binding.showUserInfoXml.setOnClickListener {

            expandedUserInfo()

        }

        //---------------------------------------------------------
        followingOrNot(args.userID.toString())

        //---------------------------------------------------------
        countNumberOfFollowers(args.userID)



        //button following....*********************************
        binding.btnFollowXml.setOnClickListener {
            followersUser(args.userID)
        }

    }

    @SuppressLint("SetTextI18n")
    fun followingOrNot(userId: String) = CoroutineScope(Dispatchers.IO).launch {
        fireStore.collection("Users").document("$userId")
            .collection("Followers").document(myId.toString())
            .get().addOnCompleteListener {it
                if (it.result?.exists()!!) {

                    binding.btnFollowXml.setText("Following")
                    binding.btnFollowXml.setBackgroundColor(Color.GRAY)


                } else {
                    binding.btnFollowXml.setText("Follow")
                    binding.btnFollowXml.setBackgroundColor(Color.BLUE)

                }

            }
    }

    //------------------------------------------------------------------------------------------------

    fun followersUser(userId: String) = CoroutineScope(Dispatchers.IO).launch {
        fireStore.collection("Users").document("$userId")
            .collection("Followers").document(myId.toString())
            .get().addOnCompleteListener { it
                if (it.result?.exists()!!) {

                    //To Unfollow user
                    unfollowDialog(myId.toString(), userId.toString())
                } else {
                    binding.btnFollowXml.setText("Following")
                    binding.btnFollowXml.setBackgroundColor(Color.GRAY)

                    //To Add Followers
                    userProfileViewModel.addFollowersAndFollowing(myId.toString(), userId.toString())

                    countNumberOfFollowers(args.userID)

                }
            }
    }

    fun unfollowDialog(myId: String, userId: String) {

        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Unfollow User")
        builder.setMessage("Are you sure to unfollow?")
        builder.setIcon(R.drawable.ic_baseline_cancel_24)

        builder.setPositiveButton("Yes") { _, _ ->
            binding.btnFollowXml.setText("Follow")
            binding.btnFollowXml.setBackgroundColor(Color.CYAN)

            userProfileViewModel.deleteFollowersAndFollowing(myId.toString(), userId.toString())
            countNumberOfFollowers(args.userID)
        }

        builder.setNegativeButton("Cancel", { _, _ -> })
        builder.show()
    }

     fun countNumberOfFollowers(userId: String){

        //Count Number FOLLOWERS
        fireStore.collection("Users").document(userId)
            .collection("Followers").get()
            .addOnSuccessListener {
                var numberOfFollowers = it.size()
                binding.userFollowersXml.text = numberOfFollowers.toString()
            }

//------------------------------------------------------------------------------------

        //Count Number FOLLOWING
        fireStore.collection("Users").document("${userId}")
            .collection("Following").get().addOnSuccessListener {
                var numberOfFollowing = it.size()
               binding.userFollowingXml.text = numberOfFollowing.toString()

            }
    }



    //------------------------------------------------------------------------------------------------
    fun getUserPhoto(userPhoto: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesUsers/$userPhoto")
        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            binding.userImageXml.load(bitmap)


        }.addOnFailureListener {
            /*if (progressDialog.isShowing)
                progressDialog.dismiss()*/

            //   Toast.makeText(context,"Failed image ",Toast.LENGTH_SHORT).show()

        }
    }


    //---------------------------------------------------------------------------------------------
    fun callUser(userNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${userNumber.toString()}")
        startActivity(intent)
    }
    fun userEmail(userEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "${userEmail.toString()}")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")

        if (activity?.let { it -> intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    fun expandedCallUser() {

        if (binding.linearLayoutCallUserXml.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(
                binding.linearLayoutCallUserXml,
                AutoTransition()
            )
            binding.linearLayoutCallUserXml.visibility = View.VISIBLE

        } else {
            TransitionManager.beginDelayedTransition(
                binding.linearLayoutCallUserXml,
                AutoTransition()
            )
            binding.linearLayoutCallUserXml.visibility = View.GONE
        }
    }

    fun expandedUserInfo() {
        if (binding.linearLayOutUserInfoXml.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(
                binding.linearLayOutUserInfoXml,
                AutoTransition()
            )
            binding.linearLayOutUserInfoXml.visibility = View.VISIBLE

        } else {
            TransitionManager.beginDelayedTransition(
                binding.linearLayOutUserInfoXml,
                AutoTransition()
            )
            binding.linearLayOutUserInfoXml.visibility = View.GONE
        }

    }


}