package com.albasil.finalprojectkotlinbootcamp.UI

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.fragment.findNavController
import com.albasil.finalprojectkotlinbootcamp.MainActivity
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.change_password_bottom_sheet.view.*
import kotlinx.android.synthetic.main.help_and_support.view.*
import java.util.*

@Suppress("DEPRECATION")



class Setting : Fragment() {
    lateinit var binding: FragmentSettingBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var settings :SharedPreferences


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


        //loadLocate()


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
                findNavController().popBackStack()


            }





            binding.tvChangePasswordXml.setOnClickListener {
                dialogChangePassword()

                //showChangeLanguage()

            }



            binding.aboutUsId.setOnClickListener {
                aboutUs()

            }


            binding.helpAndSupportId.setOnClickListener {
                support()
            }


            binding.changeLanguagId.setOnClickListener {

                //changeLanguage()

                showChangeLanguage()

            }



        }

    private fun showChangeLanguage(){

        val listItmes = arrayOf("عربي","English")


        val mBuilder =AlertDialog.Builder(this.requireContext())

        mBuilder.setTitle("Choose Language")

        mBuilder.setSingleChoiceItems(listItmes,-1){
                 dialog, which ->
            if (which ==0){

                //setLocate("ar")
                setLocaleKoala("ar")

            }else if (which==1){
                setLocaleKoala("en")

            }

            dialog.dismiss()


        }
        val mDialog =mBuilder.create()

        mDialog.show()


    }

    private fun setLocaleKoala(localeName: String) {

        val locale =Locale(localeName.toString())

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale

        //---------------------------------------------------------------
        context?.resources?.updateConfiguration(config, requireContext().resources.displayMetrics)


        settings = this.requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = settings.edit()
        editor.putString("Settings", "${locale.toString()}")
        editor.apply()


        val refresh = Intent(context, MainActivity::class.java)
    //    refresh.putExtra("currentLang", localeName)
        startActivity(refresh)
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
                                    user.updatePassword("${newPassword.toString()}")
                                        .addOnCompleteListener { task ->
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





    }

