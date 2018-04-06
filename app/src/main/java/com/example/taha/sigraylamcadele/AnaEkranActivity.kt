package com.example.taha.sigraylamcadele

import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.taha.sigraylamcadele.Fragments.*
import com.example.taha.sigraylamcadele.Library.UserPortal
import kotlinx.android.synthetic.main.ana_ekran_activity.*

class AnaEkranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_ekran_activity)

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,AnasayfaFragment())
        transaction.commit()

        val anasayfaFragment = AnasayfaFragment()
        val istatistikFragment = IstatistikFragment()
        val grupFragment = GrupFragment()
        val soruCevapFragment = SoruCevapFragment()
        val ayarlarFragment = AyarlarFragment()

        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment:android.app.Fragment? = null

            when (item.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = anasayfaFragment

                }
                R.id.navigation_statistics -> {

                    selectedFragment = istatistikFragment

                }
                R.id.navigation_group -> {
                    selectedFragment = grupFragment

                }

                R.id.navigation_questions -> {
                    selectedFragment = soruCevapFragment

                }

                R.id.navigation_settings -> {
                    selectedFragment = ayarlarFragment

                }
            }
            var transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer,selectedFragment)
            transaction.commit()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.ana_ekran_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.appLogOut ->{
                if(UserPortal.deleteLoggedInUser(this@AnaEkranActivity))
                {
                    val intent = Intent(this@AnaEkranActivity,LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(this@AnaEkranActivity,"Çıkış başarılı",Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }

        return super.onOptionsItemSelected(item)
    }
}
