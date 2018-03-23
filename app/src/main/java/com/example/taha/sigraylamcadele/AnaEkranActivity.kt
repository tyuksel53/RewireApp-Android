package com.example.taha.sigraylamcadele

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.ana_ekran_activity.*

class AnaEkranActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_group -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_questions -> {
                message.text = "Soru&Cavap"
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_settings -> {
                message.text = "Ayarlar"
                return@OnNavigationItemSelectedListener true
            }


        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_ekran_activity)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
