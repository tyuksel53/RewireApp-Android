package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.Library.Portal
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater



class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val inflator = this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflator.inflate(R.layout.register_toolbar, null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setCustomView(v)

        btnRegister.setOnClickListener {

            var registerControl:Boolean = true
            if(edRegisterMail.text.isNullOrEmpty() || edRegisterMail.text.isBlank())
            {
                edRegisterMail.error = "Mail adresi boş olamaz"
                registerControl = false
            }else
            {
                if(!Portal.isEmailValid(edRegisterMail.text.toString()))
                {
                    registerControl = false
                    edRegisterMail.error = "Lütfen düzgün formatta mail adresi giriniz"
                }
            }

            if(edRegisterUserName.text.isNullOrEmpty() || edRegisterUserName.text.isBlank())
            {
                edRegisterUserName.error = "Kullanici adi boş olamaz"
                registerControl = false
            }

            if(edRegisterPassConfirm.text.isNullOrEmpty() || edRegisterPassConfirm.text.isBlank())
            {
                edRegisterPassConfirm.error = "Şifre boş olamaz"
                registerControl = false
            }

            if(edRegisterPass.text.isNullOrEmpty() || edRegisterPass.text.isBlank())
            {
                edRegisterPass.error = "Şifre boş olamaz"
                registerControl = false
            }else
            {
                if(edRegisterPassConfirm.text.isNotBlank() && edRegisterPassConfirm.text.isNotEmpty())
                {
                    if(edRegisterPassConfirm.text.toString() != edRegisterPass.text.toString())
                    {
                        edRegisterPassConfirm.error = "Şifreler eşleşmiyor"
                        registerControl = false
                    }
                }
            }

            if(registerControl)
            {
                Toast.makeText(this@RegisterActivity,"Control doğru",Toast.LENGTH_SHORT).show()
            }

        }

    }
}
