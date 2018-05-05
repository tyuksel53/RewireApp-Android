package com.example.taha.sigraylamcadele.Library

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CircleDecorator(var context: Context,day:CalendarDay,drawable: Drawable,var smokeCount:String) : DayViewDecorator {
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
        if(smokeCount != "")
        {
            view.setSelectionDrawable(drawable!!)
            view.addSpan( TextSpan(5.toFloat(),
                    Color.GRAY,smokeCount) )
        }else
        {
            view.setSelectionDrawable(drawable!!)
        }

    }
}