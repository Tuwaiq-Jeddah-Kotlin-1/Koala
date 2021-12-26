package com.albasil.finalprojectkotlinbootcamp.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// Date
val current = LocalDateTime.now()

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

val formatted = current.format(formatter)
data class Users(

    var userId :String = "",
    var userEmail: String = "",
    var userName: String = "",
    var userPhone: String = "",
    var creationAccount: String = "${formatted.toString()}",
    var followers :Int =0,
    var following :Int =0,
)