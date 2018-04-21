package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Library.CircleDecorator
import com.example.taha.sigraylamcadele.Library.UserPortal

import com.example.taha.sigraylamcadele.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*
import kotlin.collections.HashMap

class IstatistikFragment : android.app.Fragment() {

    var hashMap:HashMap<CalendarDay,Int> = HashMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_istatistik, container, false)

        var calendar = view.findViewById<MaterialCalendarView>(R.id.userCalandarView)

        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2020, 0, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
        calendar.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
        calendar.setDateTextAppearance(R.color.textColorPrimary)
        if(UserPortal.loggedInUser?.LastLoginTime != null)
        {
            val currentDay = UserPortal.simpleDate(UserPortal.loggedInUser?.LastLoginTime!!).split('-')
            val registeredDate = UserPortal.loggedInUser!!.RegisteredDate!!.split(' ')[0].split('-')
            val registedTime = CalendarDay.from(registeredDate[2].toInt(),
                    registeredDate[1].toInt(),
                    registeredDate[0].toInt())

            calendar.addDecorator(CircleDecorator(activity,registedTime,
                    ContextCompat.getDrawable(activity, R.drawable.circle_purple)!!))

            val today = CalendarDay.from(currentDay[2].toInt(),
                    currentDay[1].toInt(),
                    currentDay[0].toInt())

            calendar.addDecorator(CircleDecorator(activity,today,
                    ContextCompat.getDrawable(activity, R.drawable.circle_gray)!!))
        }


        calendar.setOnDateChangedListener { widget, date, selected ->

            if(hashMap.get(date) != null)
            {
                widget.addDecorator(CircleDecorator(activity,date,
                        ContextCompat.getDrawable(activity, R.drawable.circle_negative)!!))
                hashMap.remove(date)
            }else
            {
                hashMap.put(date,0)
                widget.addDecorator(CircleDecorator(activity,date,
                        ContextCompat.getDrawable(activity, R.drawable.circle_positive)!!))

            }
        }

        return view
    }

}// Required empty public constructor
