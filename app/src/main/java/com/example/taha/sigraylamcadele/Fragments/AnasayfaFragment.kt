package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.taha.sigraylamcadele.R


/**
 * A simple [Fragment] subclass.
 */
class AnasayfaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_anasayfa, container, false)
    }

}// Required empty public constructor
