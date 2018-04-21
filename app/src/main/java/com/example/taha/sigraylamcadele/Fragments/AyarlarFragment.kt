package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.example.taha.sigraylamcadele.Dialogs.DilSecDialog
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import io.paperdb.Paper


class AyarlarFragment : android.app.Fragment(),DilSecDialog.onLanguageChanged {
    override fun languageChanged() {
        updateLanguage()
    }


    var language:TextView? = null
    var tvGenel:TextView?=null
    var switchNotifiy: Switch? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_ayarlar, container, false)
        updateLanguage()

        language = view.findViewById(R.id.tvSettingsLanguage)
        tvGenel = view.findViewById(R.id.tvAyarlarGenel)
        switchNotifiy = view.findViewById(R.id.swBildirim)

        updateLanguage()
        language?.setOnClickListener {
            val dialog = DilSecDialog()
            dialog.setTargetFragment(this,5)
            dialog.show(fragmentManager,"tag")
        }

        return view
    }

    fun updateLanguage()
    {
        val context = LocaleHelper.setLocale(activity, Paper.book().read<String>("language"))
        UserPortal.myLangResource = context.resources

        language?.setText(UserPortal.myLangResource?.getString(R.string.Dil_degistirin))
        tvGenel?.setText(UserPortal.myLangResource?.getString(R.string.genel))
        switchNotifiy?.setText(UserPortal.myLangResource?.getString(R.string.bildirim))
    }


}// Required empty public constructor
