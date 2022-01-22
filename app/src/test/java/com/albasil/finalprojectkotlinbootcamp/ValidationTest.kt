package com.albasil.finalprojectkotlinbootcamp

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidationTest {
    private lateinit var validation: Validation
    @Before
    fun setUp(){
        validation= Validation()
    }
    @Test
    fun checkEmail(){
        val result=validation.email("test123@gmail.com")
        val result2=validation.email("test123gmail.com")

        //Test
        assertTrue(result)
        assertFalse(result2)
    }
}