package com.example.taha.sigraylamcadele.Library

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.example.taha.sigraylamcadele.R

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CirclePositiveDecorator(context: Context,day:CalendarDay) : DayViewDecorator {
    private var date: CalendarDay

    private val drawable: Drawable?

    init {
        drawable = ContextCompat.getDrawable(context, R.drawable.circle_positive)
        date = day
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {

        view.setSelectionDrawable(drawable!!)
    }
}

class CircleNegativeDecorator(context: Context,day:CalendarDay) : DayViewDecorator {
    private var date: CalendarDay

    private val drawable: Drawable?

    init {
        drawable = ContextCompat.getDrawable(context, R.drawable.circle_negative)
        date = day
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {

        view.setSelectionDrawable(drawable!!)
    }
}