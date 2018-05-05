package com.example.taha.sigraylamcadele.Dialogs


import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal

import com.example.taha.sigraylamcadele.R
import com.rengwuxian.materialedittext.MaterialEditText

class ForgetPasswordDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.dialog_forget_password, container, false)
        val mail = view.findViewById<MaterialEditText>(R.id.edForgetPasswordMail)
        val btn = view.findViewById<Button>(R.id.btnForgetPass)
        val header = view.findViewById<TextView>(R.id.tvForgetPassHeader)
        header.setText(UserPortal.myLangResource!!.getString(R.string.sifremi_unuttum))
        btn.setText(UserPortal.myLangResource!!.getString(R.string.gonder))
        mail.setHint(UserPortal.myLangResource!!.getString(R.string.mail_adresiniz))

        btn.setOnClickListener {

            var registerControl = true
            if(mail.text.isNullOrEmpty() || mail.text.isBlank())
            {
                mail.error = UserPortal.myLangResource!!.getString(R.string.mailHataBos)
                registerControl = false
            }else
            {
                if(!Portal.isEmailValid(mail.text.toString()))
                {
                    registerControl = false
                    mail.error = UserPortal.myLangResource!!.getString(R.string.mailHataFormat)
                }else if(mail.text.toString().length > 254)
                {
                    registerControl = false
                    mail.error = UserPortal.myLangResource!!.getString(R.string.mailHataUzunluk)
                }
            }

            if(registerControl)
            {
                
            }

        }


        return view
    }


}
