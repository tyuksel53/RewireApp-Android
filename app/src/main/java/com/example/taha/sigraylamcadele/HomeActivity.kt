package com.example.taha.sigraylamcadele

import android.content.Context
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.example.taha.sigraylamcadele.Fragments.*
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import io.paperdb.Paper
import kotlinx.android.synthetic.main.ana_ekran_activity.*
import java.util.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_ekran_activity)
        val settings = Portal.getSettings(this,UserPortal.loggedInUser!!.Username!!)
        if(settings == null)
        {
            Portal.insertUserSettings(this, TimeZone.getDefault().id,"20:00",
                    UserPortal.loggedInUser!!.Username!!)
        }
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.app_header)

        updateView(Paper.book().read("language"))
        UserPortal.getLikes()
        if(UserPortal.isUserNotUpdated)
        {
            UserPortal.updateUserInfo()
        }
        val anasayfaFragment = AnasayfaFragment()
        val istatistikFragment = IstatistikFragment()
        val grupFragment = LeaderBoardFragment()
        val soruCevapFragment = SoruCevapFragment()
        val ayarlarFragment = AyarlarFragment()
        val menu = navigation.getMenu().findItem(R.id.navigation_home)
        menu.setTitle(UserPortal.myLangResource!!.getString(R.string.anasayfa))
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,anasayfaFragment)
        transaction.commit()


        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment:android.app.Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setTitle(UserPortal.myLangResource!!.getString(R.string.anasayfa))
                    selectedFragment = anasayfaFragment

                }
                R.id.navigation_statistics -> {
                    item.setTitle(UserPortal.myLangResource!!.getString(R.string.statistikler))
                    selectedFragment = istatistikFragment
                }
                R.id.navigation_group -> {
                    item.setTitle(UserPortal.myLangResource!!.getString(R.string.grup))
                    selectedFragment = grupFragment

                }

                R.id.navigation_questions -> {
                    item.setTitle(UserPortal.myLangResource!!.getString(R.string.sorucevap))
                    selectedFragment = soruCevapFragment

                }

                R.id.navigation_settings -> {
                    item.setTitle(UserPortal.myLangResource!!.getString(R.string.ayarlar))
                    selectedFragment = ayarlarFragment

                }
            }
            var transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer,selectedFragment)
            transaction.commit()
            true
        }
    }

    private fun updateView(lang: String) {
        val context = LocaleHelper.setLocale(this@HomeActivity,lang)
        UserPortal.myLangResource = context.resources
    }


    override fun onResume() {
        updateView(Paper.book().read<String>("language"))
        super.onResume()
    }
}
