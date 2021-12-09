package com.albasil.finalprojectkotlinbootcamp.UI

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentProfileBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSettingBinding

class Setting : Fragment() {
    lateinit var binding: FragmentSettingBinding

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        binding = FragmentSettingBinding.inflate(inflater,container,false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        //tvProfileShow
        binding.buttonLogOutXml.setOnClickListener { 


            preferences =
                this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
            val emailPref = preferences.getString("EMAIL", "")
            // userEmail.text = emailPref
            val passwordPref = preferences.getString("PASSWORD", "")



            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
            findNavController().navigate(R.id.action_setting_to_sign_in)



        }
    }

}