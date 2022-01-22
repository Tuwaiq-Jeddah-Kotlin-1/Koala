package com.albasil.finalprojectkotlinbootcamp


class Validation {

    private val emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun email(email:String):Boolean{
        if (email.matches(emailPattern.toRegex()))
            return true
        return false
    }
}