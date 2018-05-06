package com.example.taha.sigraylamcadele.Dialogs


import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal

import com.example.taha.sigraylamcadele.R
import com.rengwuxian.materialedittext.MaterialEditText
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        mail.setHint(UserPortal.myLangResource!!.getString(R.string.kullaniciAdi))

        btn.setOnClickListener {

            var registerControl = true
            if(mail.text.isNullOrEmpty() || mail.text.isBlank())
            {
                mail.error = UserPortal.myLangResource!!.getString(R.string.hataGirisKullanıcıadi)
                registerControl = false
            }else
            {
                if(mail.text.toString().length < 4)
                {
                    mail.error = UserPortal.myLangResource!!.getString(R.string.hataKullaniciAdiMinUzunluk)
                    registerControl = false
                }else if(mail.text.toString().length > 50)
                {
                    mail.error = UserPortal.myLangResource!!.getString(R.string.hataKullaniciAdiMaxUzunluk)
                    registerControl = false
                }
            }

            if(registerControl)
            {
                val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
                val result = apiInterface?.forgetPass(mail.text.toString())
                val enqueue = result?.clone()?.enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        var test = ""
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        var test = ""
                    }

                })
                Toasty.success(activity,UserPortal.myLangResource!!.
                        getString(R.string.mail_gonderildi),
                        Toast.LENGTH_LONG).show()
                dismiss()
            }

        }


        return view
    }


}
