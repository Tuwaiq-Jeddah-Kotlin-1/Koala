package com.albasil.finalprojectkotlinbootcamp.data

import java.text.SimpleDateFormat

data class Comment(
    var userID: String = "",
    var dateFormat: String = "",//SimpleDateFormat
    var userName: String = "",
    var textContent: String = "",
    var articleID:String=""

)
