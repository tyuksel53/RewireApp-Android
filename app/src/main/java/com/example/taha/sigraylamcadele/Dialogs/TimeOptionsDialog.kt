package com.example.taha.sigraylamcadele.Dialogs


import android.app.DialogFragment
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.taha.sigraylamcadele.R


class TimeOptionsDialog : DialogFragment() {

    interface onSortSelectedListener{

        fun sortFirstSelected(selectedTime:String)

    }

    lateinit var myOnSortSelectedListener:onSortSelectedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_time_options_dialog, container, false)

        val type = arguments.getString("type")

        val lastHour = view.findViewById<TextView>(R.id.tvLastHour)
        val lastDay = view.findViewById<TextView>(R.id.tvLastDay)
        val lastWeek = view.findViewById<TextView>(R.id.tvLastWeek)
        val lastMonth = view.findViewById<TextView>(R.id.tvLastmonth)
        val lastYear = view.findViewById<TextView>(R.id.tvLastYear)
        val alltime = view.findViewById<TextView>(R.id.tvAllTime)

        lastDay.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Gun-${lastDay.text}")
            dismiss()
        }

        lastHour.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Saat-${lastHour.text}")
            dismiss()
        }

        lastWeek.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Hafta-${lastWeek.text}")
            dismiss()
        }

        lastMonth.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Ay-${lastMonth.text}")
            dismiss()
        }

        lastYear.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Yil-${lastYear.text}")
            dismiss()
        }

        alltime.setOnClickListener {
            myOnSortSelectedListener.sortFirstSelected("$type-Tum-${alltime.text}")
            dismiss()
        }
        return view
    }

    override fun onAttach(context: Context?) {
        myOnSortSelectedListener = targetFragment as onSortSelectedListener
        super.onAttach(context)
    }


}
