package com.example.taha.sigraylamcadele.Library

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.example.taha.sigraylamcadele.R

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CircleDecorator(context: Context,day:CalendarDay,drawable: Drawable) : DayViewDecorator {
    private var date: CalendarDay

    private val drawable: Drawable?

    init {
        this.drawable = drawable
        date = day
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {

        view.setSelectionDrawable(drawable!!)
    }
}