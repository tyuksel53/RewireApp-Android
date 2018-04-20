package com.example.taha.sigraylamcadele.Dialogs


import android.app.DialogFragment
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taha.sigraylamcadele.Fragments.SoruCevapFragment
import com.example.taha.sigraylamcadele.Library.UserPortal

import com.example.taha.sigraylamcadele.R

class SortByDialog : DialogFragment(),TimeOptionsDialog.onSortSelectedListener {
    interface sortSelected
    {
        fun sortSelected(selectedTime: String)
    }

    override fun sortFirstSelected(selectedTime: String) {
        myInterface.sortSelected(selectedTime)
    }


    lateinit var myInterface:sortSelected
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_sort_by_dialog, container, false)

        val new = view.findViewById<TextView>(R.id.tvSortByNew)
        val likes = view.findViewById<TextView>(R.id.tvSortByLikes)

        new.setOnClickListener {
            myInterface.sortSelected("yeni-Tum-${UserPortal.myLangResource?.getString(R.string.yeni_gonderiler)}")
            dismiss()
        }

        likes.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("type","like")
            var dialog = TimeOptionsDialog()
            dialog.setArguments(bundle)
            dialog.show(fragmentManager,"time")
            dialog.setTargetFragment(this,1)
            dismiss()
        }

        return view
    }

    override fun onAttach(context: Context?) {
        myInterface = targetFragment as sortSelected
        super.onAttach(context)


    }


}
