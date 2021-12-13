package com.albasil.finalprojectkotlinbootcamp.UI

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.MainActivity
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentProfileBinding
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.change_password_bottom_sheet.view.*
import kotlinx.android.synthetic.main.help_and_support.view.*
import java.util.*
import kotlin.system.exitProcess

class Setting : Fragment() {
    lateinit var binding: FragmentSettingBinding

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater,container,false)


        return binding.root

    }


    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

            } else {

                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }






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





            binding.tvChangePasswordXml.setOnClickListener {
                dialogChangePassword()
            }



            binding.aboutUsId.setOnClickListener {
                aboutUs()

            }


            binding.helpAndSupportId.setOnClickListener {
                support()
            }


            binding.changeLanguagId.setOnClickListener {

                changeLanguage()

            }

            //     binding.switchDarkMode.


        }





        //----------------------------------------------------------------------
        private fun changeLanguage() {

            spinner()
        }

        fun spinner() {


            val view: View = layoutInflater.inflate(R.layout.change_language, null)

            val builder = BottomSheetDialog(requireView()?.context as Context)
            builder.setTitle("Change Language")

            builder.setContentView(view)

            lateinit var spinner: Spinner

            builder.show()

            spinner = view.findViewById(R.id.spinner)
            val list = ArrayList<String>()
            list.add("Select Language")
            list.add("English")
            list.add("Español")
            list.add("Français")
            list.add("Hindi")
            list.add("Arabic")

/*
        val adapter = ArrayAdapter(requireContext(),spinner.dropDownHorizontalOffset, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                    }
                    1 -> setLocale("en")
                    2 -> setLocale("es")
                    3 -> setLocale("fr")
                    4 -> setLocale("hi")
                    5 -> setLocale("ar")
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    */

        }

        private fun setLocale(localeName: String) {
            lateinit var locale: Locale
            var currentLanguage = "en"
            var currentLang: String? = null
            var bundle = Bundle()
            currentLanguage = bundle.getString(currentLang).toString()
            if (localeName != currentLanguage) {
                locale = Locale(localeName)
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = locale
                res.updateConfiguration(conf, dm)
                val refresh = Intent(
                    context,
                    MainActivity::class.java
                )
                refresh.putExtra(currentLang, localeName)
                startActivity(refresh)
            } else {
                Toast.makeText(
                    this.context, "Language, , already, , selected)!", Toast.LENGTH_SHORT
                ).show();
            }
        }

        override fun onResume() {
            super.onResume()
            requireView().isFocusableInTouchMode = true
            requireView().requestFocus()
            requireView().setOnKeyListener { v, keyCode, event ->
                if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    activity?.finish()
                    exitProcess(0) // Add your code here
                    true
                } else false
            }
        }


        //--------------------------------------------------------------------------
        fun dialogChangePassword() {

            val view: View = layoutInflater.inflate(R.layout.change_password_bottom_sheet, null)

            val builder = BottomSheetDialog(requireView().context!!)
            builder.setTitle("Forgot Password")

            val oldPassword = view.etOldPassword_xml
            var etNewPassword = view.etNewPassword_xml
            var confirmNewPassword = view.etConfirmNewPassword_xml
            val btnChangePasswor = view.buttonChangePassword_xml

            builder.setContentView(view)


            btnChangePasswor.setOnClickListener {
                changePassword(
                    "${oldPassword.text.toString()}",
                    "${etNewPassword.text.toString()}",
                    "${confirmNewPassword.text.toString()}"
                )


                //Toast.makeText(context,"${etNewPassword.text.toString()}",Toast.LENGTH_SHORT).show()
            }
            builder.show()

        }

        private fun changePassword(
            oldPassword: String,
            newPassword: String,
            confirmNewPassword: String
        ) {

            if (oldPassword.toString().isNotEmpty() &&
                newPassword.toString().isNotEmpty() &&
                confirmNewPassword.toString().isNotEmpty()
            ) {
                Log.e("new password", "${newPassword.toString()}")
                Log.e("confirmNewPassword", "${confirmNewPassword.toString()}")

                if (newPassword.toString().equals(confirmNewPassword.toString())) {

                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    val userEmail = FirebaseAuth.getInstance().currentUser!!.email
                    if (user.toString() != null && userEmail.toString() != null) {

                        val credential: AuthCredential = EmailAuthProvider
                            .getCredential(
                                "${user?.email.toString()!!}",
                                "${oldPassword.toString()}"
                            )
                        user?.reauthenticate(credential)

                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(context, "Auth Successful ", Toast.LENGTH_SHORT)
                                        .show()

                                    Toast.makeText(
                                        context, "newPassword ${newPassword.toString()}",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                    //احط متغير
                                    user?.updatePassword("${newPassword.toString()}")
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    context, "isSuccessful , Update password",
                                                    Toast.LENGTH_SHORT
                                                ).show()


                                                view?.etOldPassword_xml?.setText("")
                                                view?.etConfirmNewPassword_xml?.setText("")
                                                view?.etNewPassword_xml?.setText("")

                                            }
                                        }

                                } else {

                                    Toast.makeText(context, "Auth Failed ", Toast.LENGTH_SHORT)
                                        .show()
                                    Toast.makeText(
                                        context, "newPassword ${newPassword.toString()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(
                                        context, "oldPassword ${oldPassword.toString()}",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }
                            }

                    } else {
                        Toast.makeText(
                            context,
                            "كلمة المرور القديمة غير صحيحه ",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {
                    Toast.makeText(
                        context,
                        "New Password is not equals Confirm New Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(context, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }

        }
        //--------------------------------------------------------------------------


        //--------------------------------------------------------------------------
        private fun aboutUs() {

            val view: View = layoutInflater.inflate(R.layout.about_us, null)

            val builder = BottomSheetDialog(requireView()?.context!!)
            builder.setTitle("About Us")

            builder.setContentView(view)

            builder.show()

        }

        private fun support() {

            val view: View = layoutInflater.inflate(R.layout.help_and_support, null)

            val builder = BottomSheetDialog(requireView()?.context!!)
            builder.setTitle("support")

            val tvPhoneNumber = view.callNumber_ID

            val tvEmailAddress = view.sendEmail




            tvPhoneNumber.setOnClickListener(View.OnClickListener() {


                val phone = "+966569861476"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)


            });


            tvEmailAddress.setOnClickListener {

                val email = "Basil_alluqmni@hotmail.com"

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "${email}")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")

                if (activity?.let { it -> intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(intent)
                }


            }

            builder.setContentView(view)

            builder.show()

        }

        //--------------------------------------------------------------------------



    }