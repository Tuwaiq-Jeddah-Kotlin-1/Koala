package com.albasil.finalprojectkotlinbootcamp.data

data class Article(


    var userName :String ="",
    var title :String = "",
    var category: String = "",
    var date :String = "",
    var userId :String = "",
    var description :String = "",
    var articleImage :String = "",//array
    var like :Int = 0,//array user ,and then get size
)
