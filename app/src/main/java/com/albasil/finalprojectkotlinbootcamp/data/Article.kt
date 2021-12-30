package com.albasil.finalprojectkotlinbootcamp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(


    var articleID:String="",
    var userName :String ="",
    var title :String = "",
    var category: String = "",
    var date :String = "",
    var userId :String = "",
    var description :String = "",
    var articleImage :String ="",
    var like :Int = 0,//array user ,and then get size
) : Parcelable
