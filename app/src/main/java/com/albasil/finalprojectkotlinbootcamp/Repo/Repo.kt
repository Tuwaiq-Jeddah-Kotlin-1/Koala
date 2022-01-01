package com.albasil.finalprojectkotlinbootcamp.Repo

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.albasil.finalprojectkotlinbootcamp.Adapter.firestore
import com.albasil.finalprojectkotlinbootcamp.data.Users
import com.albasil.finalprojectkotlinbootcamp.Firebase.FirebaseAuthentication
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.data.Article
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.change_password_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_article_information.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var fireStore :FirebaseFirestore= FirebaseFirestore.getInstance()
class AppRepo(val context: Context) {


    private var imageUrl: Uri? = null

    val firebase = FirebaseAuthentication()

    suspend fun insertUserToDB(users: Users) {


    }


    suspend fun addArticleToFirestore(article: Article) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val articleRef = Firebase.firestore.collection("Articles")
            articleRef.document(article.articleID).set(article).addOnCompleteListener {
                it
                when {
                    it.isSuccessful -> {
//                        upLoadImage("${article.articleImage.toString()}")
                        Log.e("Add Article", "Done ${article.title}")
                    }
                    else -> {
                        Log.e("Field Article", "Error ${article.title}")
                    }
                }
            }
//            withContext(Dispatchers.Main) { }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }


    fun upLoadImage(uIdCategory: String) {
        val storageReference =
            FirebaseStorage.getInstance().getReference("imagesArticle/${uIdCategory}")

        imageUrl?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {

                    Log.e("addOnSuccessListener", "addOnSuccessListener")

                }.addOnFailureListener {
                    Log.e("Failed", "Failed")

                }
        }
    }


    fun getUserArticles(userID: String, articleList: MutableList<Article>): LiveData<MutableList<Article>> {
        val article = MutableLiveData<MutableList<Article>>()

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").whereEqualTo("userId", userID)
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
                    article.value = articleList
                }

            })
        return article
    }

    fun addUserInformation(myID: String, userInformation: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val userInfo = hashMapOf("moreInfo" to "${userInformation}",)
            val userRef = Firebase.firestore.collection("Users")
            userRef.document("$myID").set(userInfo, SetOptions.merge()).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {
                    }
                    else -> {
                    }
                }
            }
        }


    fun upDateUserInfo(upDateName: String, upDatePhoneNumber: String) {
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        val upDateUserData = Firebase.firestore.collection("Users")
        upDateUserData.document(uId.toString()).update(
            "userName", "${upDateName.toString()}", "userPhone", "${upDatePhoneNumber.toString()}"
        )

    }


    fun getUserInfo(userID: String,userInfo :Users): LiveData<Users>{
        val user = MutableLiveData<Users>()
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$userID")
            .get().addOnCompleteListener { it

                if (it.result?.exists()!!) {
                    userInfo.userName = it.result!!.getString("userName").toString()
                    userInfo.userPhone = it.result!!.getString("userPhone").toString()
                    userInfo.userEmail = it.result!!.getString("userEmail").toString()
                    userInfo.moreInfo =it.result!!.getString("moreInfo").toString()
                } else {
                }
                user.value= userInfo
            }
        return user
    }

    fun deleteArticle(articleID:String){
        val deleteArticle = Firebase.firestore.collection("Articles")
            .document(articleID).delete()
        deleteArticle.addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    Log.d("Delete", "Delete Article")
                }
            }
        }
    }


    //-------------------------Settings----------------------------------
     fun changePassword(oldPassword: String, newPassword: String,confirmNewPassword: String,view:View) {

        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.toString().isNotEmpty()) { Log.e("new password", "${newPassword.toString()}")
            Log.e("confirmNewPassword", "${confirmNewPassword.toString()}")
            if (newPassword.toString().equals(confirmNewPassword.toString())) {

                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                val userEmail = FirebaseAuth.getInstance().currentUser!!.email

                if (user.toString() != null && userEmail.toString() != null) {
                    val credential: AuthCredential = EmailAuthProvider.getCredential("${user?.email.toString()!!}",
                        "${oldPassword.toString()}")
                    user?.reauthenticate(credential)

                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(view.context, "Auth Successful ", Toast.LENGTH_SHORT)
                                    .show()
                                //احط متغير
                                user.updatePassword("${newPassword.toString()}")
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(view.context, "isSuccessful , Update password", Toast.LENGTH_SHORT).show()

                                            view.etOldPassword_xml?.setText("")
                                            view?.etConfirmNewPassword_xml?.setText("")
                                            view?.etNewPassword_xml?.setText("")

                                        }
                                    }

                            } else {

                                Toast.makeText( view.context, " Failed Change Password ", Toast.LENGTH_SHORT).show()
                            }
                        }

                }

                else {
                  /*  Toast.makeText(
                        view.context,
              "كلمة المرور القديمة غير صحيحه ",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }

            } else {
               Toast.makeText(
                    context,
                    "New Password is not equals Confirm New Password.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
//            Toast.makeText(view.context, "Please enter all the fields", Toast.LENGTH_LONG).show()
//
//            Toast.makeText( view.context, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }

    }


    //------------------------------------------------------------------------

    fun favoriteArticles(myID: String, articleList: MutableList<Article>
    ): LiveData<MutableList<Article>> {
        val article = MutableLiveData<MutableList<Article>>()
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users").document(myID).collection("Favorite")
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
                    article.value = articleList
                }

            })
        return article
    }

    //--------------------editArticleData--------------------------------------------------------
    fun editArticleData(articleID:String,titleArticle:String,descraptaionArticle:String,category:String,imageArticleID: String,view: View){
//
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formatted = current.format(formatter)
        val userRef = Firebase.firestore.collection("Articles")
        userRef.document("${articleID}")
            .update("title",titleArticle,"description",descraptaionArticle,
                "date",formatted,"category",category,"articleImage",imageArticleID).addOnCompleteListener { it
                when {
                    it.isSuccessful -> {

                           Toast.makeText(view.context,"UpDate ",Toast.LENGTH_SHORT).show()

                    }
                    else -> {
                        Toast.makeText(view.context,"Error to Update ",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun editImageArticles(imageArticleID:String,view: View){
        /* val progressDialog = ProgressDialog(context)
          progressDialog.setMessage("Uploading File ...")
          progressDialog.setCancelable(false)
          progressDialog.show()*/
        val storageReference = FirebaseStorage.getInstance().getReference("imagesArticle/${imageArticleID}")
        imageUrl?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    //   userImage.setImageURI(null)
                    //  Toast.makeText(context,"uploading image",Toast.LENGTH_SHORT).show()

                    //    if (progressDialog.isShowing)progressDialog.dismiss()

                    Toast.makeText(view.context,"Its upload image to firestorage",Toast.LENGTH_SHORT).show()


                }.addOnFailureListener{
                    //   if (progressDialog.isShowing)progressDialog.dismiss()
                    Toast.makeText(view.context,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }

    //--------------------Article Favorite--------------------------------------------------------------------
     fun checkIfFavorite(myID: String,articleID: String,view:View) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(myID)
            .collection("Favorite").document(articleID).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {
                    view.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    view.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)
                }
            }
    }
    //---------------------------------------
     fun upDateFavorite(myID:String,articleID: String,userID: String,view: View) {
        //check in the fireStore
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(myID)
            .collection("Favorite").document(articleID).get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {

                    deleteFavorite(myID,articleID)
                    view.favoriteArticle_xml?.setImageResource(R.drawable.ic_favorite_border)

                } else {

                    addFavorite(articleID,userID)
                    view?.favoriteArticle_xml?.setImageResource(R.drawable.ic_baseline_favorite_24)


                }
            }
    }

    //---------deleteFavorite------------------------------------------------------------------------------------------
    fun deleteFavorite(myID: String,articleID: String) {
        val deleteFavoriteArticle = FirebaseFirestore.getInstance()
        deleteFavoriteArticle.collection("Articles").document(articleID)
            .collection("Favorite").document("${myID.toString()}").delete()
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        Log.e("Delete Article ", "Delete From Articles Favorite")
                    }
                }
            }

        //-------------deleteFavoriteArticleUser----------------------------------------------------------
        val deleteFavoriteArticleUser = FirebaseFirestore.getInstance()
        deleteFavoriteArticleUser.collection("Users").document(myID.toString())
            .collection("Favorite").document("${articleID.toString()}").delete()
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        Log.e("Delete Article ", "Delete From User Favorite")
                    }
                }
            }
        numberOfFavorite(articleID)
    }

    //---------------addFavorite-------------------------------------------------------------------------------------------
    private fun addFavorite(articleID: String, userID:String) {
        val addFavorite = hashMapOf(
            "articleID" to articleID,
            "userId" to userID,
        )
        //---------------------------------------------------------------------------------
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val articleRef = Firebase.firestore.collection("Users")
        articleRef.document(userId.toString()).collection("Favorite")
            .document("${articleID.toString()}")
            .set(addFavorite).addOnCompleteListener {
                it
                when {
                    it.isSuccessful -> {
                        Log.d("Add Article", "Done to add User Favorite")
                    }
                    else -> {
                        Log.d("Error", "is not Successful fire store")
                    }
                }

                //---------------------------------------------------------------------------------
                val addToArticle = Firebase.firestore.collection("Articles")
                addToArticle.document(articleID.toString()).collection("Favorite")
                    .document("${userId.toString()}").set(addFavorite)
                //---------------------------------------------------------------------------------

                //delete ...
                numberOfFavorite(articleID)

            }
    }

    private fun numberOfFavorite(articleID: String) {
        firestore.collection("Articles").document(articleID)
            .collection("Favorite").get()
            .addOnSuccessListener {
                var numberOfFavorite = it.size()
                val userRef = Firebase.firestore.collection("Articles")
                userRef.document("$articleID").update("like", numberOfFavorite)

            }
    }



//-----------------Profile--------------------------------------
    fun getUserPhoto(): File {

        val imageName = "${FirebaseAuth.getInstance().currentUser?.uid}"

        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesUsers/$imageName")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

//            binding.userImageProfileXml.load(bitmap)
            //  binding.userImageProfileXml.load(localFile)

        }.addOnFailureListener {
            Toast.makeText(context, "Failed image ", Toast.LENGTH_SHORT).show()
        }
        return localFile
    }



    //-----------------Home Page------------------------------------------------------------

    //----------------------getAllMyArticles-----------------------------------
    fun getAllArticles(articleList: MutableList<Article>): LiveData<MutableList<Article>> {
        val article = MutableLiveData<MutableList<Article>>()

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").orderBy("date", Query.Direction.DESCENDING)
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
                    article.value = articleList
                }
            })

        return article
    }


    fun removeAllArticles(articleList: MutableList<Article>): LiveData<MutableList<Article>> {
        val article = MutableLiveData<MutableList<Article>>()
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Articles").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.remove(dc.document.toObject(Article::class.java))
                        }
                    }
                    article.value = articleList
                }
            })

        return article
    }






    //-----------------------get Followers And Following------------------------------------------------------------------------
    fun getFollowersAndFollowing(type:String, articleList: MutableList<Users>
    ): LiveData<MutableList<Users>> {
        val article = MutableLiveData<MutableList<Users>>()
        val myID = FirebaseAuth.getInstance().currentUser?.uid

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users").document(myID.toString())
            .collection(type.toString())
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.add(dc.document.toObject(Users::class.java))
                        }
                    }
                    article.value = articleList

                }

            })
        return article
    }


        //---------------------------User Profile--------------------------------------------------------------------------

    //-------------------Add------------------------

    fun addFollowers(myId: String, userId: String) {
        val upDateFollowers = hashMapOf(
            "userId" to "${myId}",
        )
        fireStore.collection("Users").document("${userId}")
            .collection("Followers").document("${myId}").set(upDateFollowers)

       // countNumberOfFollowers(userId)
    }
    fun addFollowing(myId: String, userId: String) {
        val upDateFollowing = hashMapOf("userId" to "${userId}")
        fireStore.collection("Users").document("${myId}")
            .collection("Following").document("${userId}").set(upDateFollowing)

       // countNumberOfFollowers(userId)
    }


    //----------------Delete---------------------------------------------------------
    fun deleteFollowers(myId: String, userId: String) {
        fireStore.collection("Users").document("${userId}")
            .collection("Followers").document("${myId}").delete()
    }

    fun deleteFollowing(myId: String, userId: String) {
        fireStore.collection("Users").document("${myId}")
            .collection("Following").document("${userId}").delete()

    }

}

