package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_insert_share.*
import kotlinx.android.synthetic.main.insert_share_toolbar.*

class InsertShare : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_share)

        val inflator = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.insert_share_toolbar,null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.customView = view
        pbInsertShare.visibility = View.INVISIBLE
        ivInsertShareCancel.setOnClickListener {
            this@InsertShare.finish()
        }

        ivInsertShareOK.setOnClickListener {

            var control = true
            if(edShareHeader.text.toString().isNullOrBlank() || edShareHeader.text.toString().isNullOrEmpty())
            {
                control = false
                edShareHeader.error = "Başlık boş olamaz"
            }else if( edShareHeader.text.toString().length < 3)
            {
                control = false
                edShareHeader.error = "Başlık 3 karakterden küçük olmamalıdır"
            }else if(edShareHeader.text.toString().length > 200)
            {
                control = false
                edShareHeader.error = "Başlık 200 karakterden büyük olmamalıdır"
            }

            if(edShareDescription.text.toString().isNullOrBlank() || edShareDescription.text.toString().isNullOrEmpty())
            {
                control = false
                edShareDescription.error = "Açıklama boş olamaz"
            }else if( edShareDescription.text.toString().length < 3)
            {
                control = false
                edShareDescription.error = "Açıklama 3 karakterden küçük olmamalıdır"
            }else if(edShareDescription.text.toString().length > 40000)
            {
                control = false
                edShareDescription.error = "Açıklama 40000 karakterden büyük olmamalıdır"
            }

            if(control)
            {
                pbInsertShare.visibility = View.VISIBLE
            }



        }
    }
}
