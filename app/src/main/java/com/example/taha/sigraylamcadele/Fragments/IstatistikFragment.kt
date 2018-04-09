package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Library.CircleNegativeDecorator
import com.example.taha.sigraylamcadele.Library.CirclePositiveDecorator

import com.example.taha.sigraylamcadele.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*
import kotlin.collections.HashMap

class IstatistikFragment : android.app.Fragment() {

    var hashMap:HashMap<CalendarDay,Int> = HashMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_istatistik, container, false)

        var calendar = view.findViewById<MaterialCalendarView>(R.id.userCalandarView)
        calendar.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
        calendar.setDateTextAppearance(R.color.textColorPrimary)
        var calendar1 = Calendar.getInstance()
            calendar1.add(Calendar.MONTH, -2)

        calendar.setOnDateChangedListener { widget, date, selected ->

            if(hashMap.get(date) != null)
            {
                widget.addDecorator(CircleNegativeDecorator(activity,date))
                hashMap.remove(date)
            }else
            {
                hashMap.put(date,0)
                widget.addDecorator(CirclePositiveDecorator(activity,date))

            }
        }

        return view
    }

}// Required empty public constructor
