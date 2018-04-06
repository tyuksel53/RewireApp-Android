package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taha.sigraylamcadele.Library.UserPortal

import com.example.taha.sigraylamcadele.R


/**
 * A simple [Fragment] subclass.
 */
class AnasayfaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_anasayfa, container, false)

        val textView = view.findViewById<TextView>(R.id.anasayfaText)

        textView.text = UserPortal.toString()

        return view
    }

}// Required empty public constructor
