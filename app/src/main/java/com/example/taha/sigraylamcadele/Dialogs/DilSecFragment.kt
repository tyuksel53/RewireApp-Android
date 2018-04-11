package com.example.taha.sigraylamcadele.Dialogs


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import io.paperdb.Paper


class DilSecFragment : android.app.DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dil_sec, container, false)

        val ingilizce = v.findViewById<TextView>(R.id.tvIngilizce)
        val turkce = v.findViewById<TextView>(R.id.tvTurkce)


        Paper.init(activity)

        ingilizce.setOnClickListener {
            Paper.book().write("language","en")
            LocaleHelper.setLocale(activity!!,Paper.book().read<String>("language"))
        }

        turkce.setOnClickListener {
            Paper.book().write("language","tr")
            LocaleHelper.setLocale(activity!!,Paper.book().read<String>("language"))
        }

        return v
    }


}
