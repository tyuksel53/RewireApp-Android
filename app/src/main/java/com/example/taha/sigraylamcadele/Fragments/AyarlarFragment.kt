package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.taha.sigraylamcadele.Dialogs.DilSecDialog

import com.example.taha.sigraylamcadele.R


class AyarlarFragment : android.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_ayarlar, container, false)

        var btn = view.findViewById<Button>(R.id.deneme)

        btn.setOnClickListener {
            val dialog = DilSecDialog()
            dialog.show(fragmentManager,"tag")
        }

        return view
    }


}// Required empty public constructor
