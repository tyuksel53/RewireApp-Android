package com.example.taha.sigraylamcadele

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.taha.sigraylamcadele.Fragments.*
import kotlinx.android.synthetic.main.ana_ekran_activity.*

class AnaEkranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_ekran_activity)
        title = "Sigarayla Mücadele"

        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content,AnasayfaFragment())
        transaction.commit()

        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment:Fragment? = null

            when (item.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = AnasayfaFragment()

                }
                R.id.navigation_statistics -> {

                    selectedFragment = IstatistikFragment()

                }
                R.id.navigation_group -> {
                    selectedFragment = GrupFragment()

                }

                R.id.navigation_questions -> {
                    selectedFragment = SoruCevapFragment()

                }

                R.id.navigation_settings -> {
                    selectedFragment = AyarlarFragment()

                }
            }
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(android.R.id.content,selectedFragment)
            transaction.commit()
            true
        }
    }
}
