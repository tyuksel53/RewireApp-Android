package com.example.taha.sigraylamcadele.Fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Dialogs.SmokeDialog
import com.example.taha.sigraylamcadele.Library.CircleDecorator
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.GrafikAy
import com.example.taha.sigraylamcadele.Model.UserDate

import com.example.taha.sigraylamcadele.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class IstatistikFragment : android.app.Fragment(),SmokeDialog.onSmokeCountEntered {
    override fun smokeCountChanged(cigarecigaretteCount: Int,check:UserDate?,type:String?,date: String?) {
        val value = cigarecigaretteCount
        if(value == 0)
        {
            if( userNewSelections.size <= 0 && userUpdateselections.size <= 0)
            {
                btnSave.visibility = View.INVISIBLE
            }
            return
        }

        if(type == "yeni")
        {
            userNewSelections.remove(check)
            check!!.Type = 2
            check.SmokeCount = value
            calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(Portal.textToDate(date!!)),
                    ContextCompat.getDrawable(activity,
                            getDrawable(check!!.Type!!))!!))
            userNewSelections.add(check)
        }else if(type == "yeniUpdate")
        {
            responseBody?.remove(check)
            userUpdateselections.remove(check)
            check!!.Type = 2
            check.SmokeCount = value
            calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(Portal.textToDate(date!!)),
                    ContextCompat.getDrawable(activity,
                            getDrawable(check.Type!!))!!))

            responseBody?.add(check)
            userUpdateselections.add(check)
        }else
        {
            responseBody?.remove(check)
            userUpdateselections.remove(check)
            check!!.Type = 2
            check.SmokeCount = value
            calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(Portal.textToDate(date!!)),
                    ContextCompat.getDrawable(activity,
                            getDrawable(check.Type!!))!!))
            responseBody?.add(check)
            userUpdateselections.add(check)
        }
    }
    var progressBar:ProgressBar? = null
    var result: Call<ArrayList<UserDate>>? = null
    var responseBody:ArrayList<UserDate>? = null
    lateinit var calendar:MaterialCalendarView
    val userNewSelections = ArrayList<UserDate>()
    val userUpdateselections = ArrayList<UserDate>()
    var barchart:BarChart?=null
    lateinit var btnSave:Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =  inflater!!.inflate(R.layout.fragment_istatistik, container, false)
        var flag = false
        barchart = null
        userNewSelections.clear()
        userUpdateselections.clear()
        calendar = view.findViewById(R.id.userCalandarView)

        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2020, 0, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        calendar.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
        calendar.setDateTextAppearance(R.color.textColorPrimary)

        progressBar = view.findViewById(R.id.pbIstatistik)
        btnSave = view.findViewById<Button>(R.id.btnIstatistikSaveChanges)
        btnSave.visibility = View.INVISIBLE


        btnSave.setOnClickListener {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

            if(userNewSelections.size > 0)
            {
                val insertResult = apiInterface?.insertDates("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        dates = userNewSelections)

                insertResult?.clone()?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    }

                })
            }

            if(userUpdateselections.size > 0)
            {
                val updateResult = apiInterface?.updateDates("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        dates = userUpdateselections)

                updateResult?.clone()?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    }

                })

            }
            for(i in 0 until userNewSelections.size)
            {
                responseBody!!.add(userNewSelections[i])
            }
            userNewSelections.clear()
            userUpdateselections.clear()

            Toasty.success(activity,
                    UserPortal.myLangResource!!.getString(R.string.degisikler_kaydedildi)
                    ,Toast.LENGTH_SHORT)
                    .show()
            btnSave.visibility = View.INVISIBLE
        }

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        result = apiInterface?.getDates("Bearer ${UserPortal.loggedInUser!!.AccessToken}")

        result?.clone()?.enqueue(object:Callback<ArrayList<UserDate>>{
            override fun onFailure(call: Call<ArrayList<UserDate>>?, t: Throwable?) {
                progressBar?.visibility = View.INVISIBLE
                Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers), Toast.LENGTH_LONG)
                        .show()
            }

            override fun onResponse(call: Call<ArrayList<UserDate>>?, response: Response<ArrayList<UserDate>>?) {
                progressBar?.visibility = View.INVISIBLE
                if(response?.code() == 200)
                {
                    responseBody = response?.body()


                    if(responseBody!!.size > 0)
                    {
                        barchart = view.findViewById(R.id.chartGun)
                        barchart!!.visibility = View.VISIBLE
                        initBarChart()
                    }
                    if(UserPortal.loggedInUser?.LastLoginTime != null)
                    {
                        val currentDay = CalendarDay.from(
                                Portal.textToDate(UserPortal.loggedInUser?.LastLoginTime!!))
                        val registeredDate = CalendarDay.from(
                                Portal.textToDate(UserPortal.loggedInUser?.RegisteredDate!!))

                        val rangeCalander = GregorianCalendar()
                        rangeCalander.setTime(registeredDate.date)

                        val endCalendar =  GregorianCalendar()
                        endCalendar.setTime(currentDay.date)

                        while (rangeCalander.before(endCalendar)) {
                            val result = rangeCalander.getTime()
                            calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(result),
                                    ContextCompat.getDrawable(activity, R.drawable.circle_gray)!!))
                            rangeCalander.add(Calendar.DATE, 1);
                        }
                        val result = rangeCalander.getTime()
                        calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(result),
                                ContextCompat.getDrawable(activity, R.drawable.circle_blue)!!))
                        for(i in 0 until responseBody!!.size)
                        {
                            var date  = CalendarDay.from(
                                    Portal.textToDate(responseBody!![i].Date!!))
                            if(responseBody!![i].Type == 1)
                            {
                                calendar.addDecorator(CircleDecorator(activity,date,
                                        ContextCompat.getDrawable(activity, R.drawable.circle_positive)!!))
                            }else if(responseBody!![i].Type == 2)
                            {
                                calendar.addDecorator(CircleDecorator(activity,date,
                                        ContextCompat.getDrawable(activity, R.drawable.circle_negative)!!))
                            }

                        }

                        calendar.setOnDateChangedListener { widget, date, selected ->
                            if(date.isBefore(currentDay))
                            {
                                val dateString = Portal.dateToText(date.date) + "T00:00:00"
                                var check  = responseBody!!.find { x->x.Date == dateString }
                                if(check == null)
                                {
                                    check = userNewSelections.find { x->x.Date == dateString }
                                    if(check == null) // temiz
                                    {
                                        val newDate = UserDate()
                                        newDate.Type = 1
                                        widget.addDecorator(CircleDecorator(activity,date,
                                                ContextCompat.getDrawable(activity,
                                                        getDrawable(1))!!))
                                        newDate.Date = dateString
                                        userNewSelections.add(newDate)
                                    }else if(check.Type == 1) // içmiş
                                    {
                                        val smokeDialog = SmokeDialog()
                                        smokeDialog.setTargetFragment(this@IstatistikFragment,23)
                                        smokeDialog.show(fragmentManager,"smokeDialog")

                                        val args = Bundle()
                                        args.putString("date", dateString)
                                        args.putSerializable("check",check)
                                        args.putString("type","yeni")
                                        smokeDialog.setArguments(args)
                                        flag = true
                                    }else // nötr
                                    {widget.addDecorator(CircleDecorator(activity,date,
                                            ContextCompat.getDrawable(activity,
                                                    getDrawable(R.color.colorBackgroundDark))!!))
                                        userNewSelections.remove(check)
                                    }

                                }else
                                {
                                    val updateCheck = userUpdateselections.find { x->x.Date == dateString }
                                    if(updateCheck == null)
                                    {
                                        if(check.Type == 1)
                                        {
                                            val smokeDialog = SmokeDialog()
                                            smokeDialog.setTargetFragment(this@IstatistikFragment,23)
                                            smokeDialog.show(fragmentManager,"smokeDialog")

                                            val args = Bundle()
                                            args.putString("date", dateString)
                                            args.putSerializable("check",check)
                                            args.putString("type","yeniUpdate")
                                            smokeDialog.setArguments(args)
                                            flag = true
                                        }else if(check.Type == 0)
                                        {
                                            responseBody?.remove(check)
                                            userUpdateselections.remove(check)
                                            check.SmokeCount = 0
                                            check.Type = 1
                                            widget.addDecorator(CircleDecorator(activity,date,
                                                    ContextCompat.getDrawable(activity,
                                                            getDrawable(check.Type!!))!!))
                                            responseBody?.add(check)
                                            userUpdateselections.add(check)
                                        }else if(check.Type == 2)
                                        {
                                            responseBody?.remove(check)
                                            userUpdateselections.remove(check)
                                            check.Type = 0
                                            check.SmokeCount = 0
                                            widget.addDecorator(CircleDecorator(activity,date,
                                                    ContextCompat.getDrawable(activity,
                                                            getDrawable(check.Type!!))!!))
                                            responseBody?.add(check)
                                            userUpdateselections.add(check)
                                        }
                                    }else if(updateCheck.Type == 1)
                                    {
                                        val smokeDialog = SmokeDialog()
                                        smokeDialog.setTargetFragment(this@IstatistikFragment,23)
                                        smokeDialog.show(fragmentManager,"smokeDialog")

                                        val args = Bundle()
                                        args.putString("date", dateString)
                                        args.putSerializable("check",check)
                                        args.putString("type","update")

                                        smokeDialog.setArguments(args)
                                        flag = true
                                    }else if(updateCheck.Type == 0)
                                    {
                                        responseBody?.remove(check)
                                        userUpdateselections.remove(check)
                                        check.Type = 1
                                        check.SmokeCount = 0
                                        widget.addDecorator(CircleDecorator(activity,date,
                                                ContextCompat.getDrawable(activity,
                                                        getDrawable(check.Type!!))!!))
                                        responseBody?.add(check)
                                        userUpdateselections.add(check)
                                    }else if(updateCheck.Type == 2)
                                    {
                                        responseBody?.remove(check)
                                        userUpdateselections.remove(check)
                                        check.Type =  0
                                        check.SmokeCount = 0
                                        widget.addDecorator(CircleDecorator(activity,date,
                                                ContextCompat.getDrawable(activity,
                                                        getDrawable(check.Type!!))!!))
                                        responseBody?.add(check)
                                        userUpdateselections.add(check)
                                    }
                                }

                                if( userNewSelections.size > 0 || userUpdateselections.size > 0 || flag)
                                {
                                    flag = false
                                    btnSave.visibility = View.VISIBLE
                                }else
                                {
                                    btnSave.visibility = View.INVISIBLE
                                }
                            }else
                            {
                                calendar.addDecorator(CircleDecorator(activity,date,
                                        ContextCompat.getDrawable(activity, R.color.colorPrimaryDark)!!))
                            }

                        }

                        calendar.visibility = View.VISIBLE

                    }else
                    {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG)
                                .show()
                        return
                    }
                }else
                {
                    Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers), Toast.LENGTH_LONG)
                            .show()
                }
            }

        })

        return view
    }

    private fun initBarChart() {
        var listAy = ArrayList<GrafikAy>()
        var ayNum = Portal.textToDate(responseBody!![0].Date!!).getMonth()
        var deneme = GrafikAy()
        deneme.AyNumara = ayNum
        deneme.AyIsim = getMonthForInt(ayNum)
        for(i in 0 until responseBody!!.size)
        {
            if(ayNum != Portal.textToDate(responseBody!![i].Date!!).getMonth())
            {
                listAy.add(deneme)
                ayNum = Portal.textToDate(responseBody!![i].Date!!).getMonth()
                deneme = GrafikAy()
                deneme.AyNumara = ayNum
                deneme.AyIsim = getMonthForInt(ayNum)

            }
            if(responseBody!![i].Type == 1)
            {
                deneme.GoodDay  = deneme.GoodDay + 1
            }

            if(responseBody!![i].Type == 2)
            {
                deneme.BadDay = deneme.BadDay + 1
            }
        }
        val checkAy = listAy.find { x->x.AyNumara == deneme.AyNumara }
        if( (deneme.BadDay != 0  || deneme.GoodDay != 0) && checkAy == null )
        {
            listAy.add(deneme)
        }
        val entriesGood =  ArrayList<BarEntry>()
        val entriesBad = ArrayList<BarEntry>()
        val isimler = ArrayList<String>()
        for(i in 0 until listAy.size)
        {
            entriesGood.add(BarEntry(i.toFloat(), listAy[i].GoodDay.toFloat()))
            entriesBad.add(BarEntry(i.toFloat(), listAy[i].BadDay.toFloat()))
            isimler.add(listAy[i].AyIsim)
        }

        val barWidth: Float
        val barSpace: Float
        val groupSpace: Float

        barWidth = 0.3f
        barSpace = 0f
        groupSpace = 0.4f
        barchart!!.setDescription(null)
        barchart!!.setPinchZoom(false)
        barchart!!.setScaleEnabled(false)
        barchart!!.setDrawBarShadow(false)
        barchart!!.setDrawGridBackground(false)

        val groupCount = isimler.size

        val xVals =  isimler

        var set1 =  BarDataSet(entriesGood, "Temiz Gün");
        set1.setColor(ContextCompat.getColor(activity, R.color.barGreen));
        var set2 = BarDataSet(entriesBad, "Kötü Gün");
        set2.setColor(ContextCompat.getColor(activity, R.color.barRed));
        var data =  BarData(set1, set2);
        data.setValueTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        data.setValueFormatter(LargeValueFormatter());
        barchart!!.setData(data)
        barchart!!.getBarData().setBarWidth(barWidth);
        barchart!!.getXAxis().setAxisMinimum(0.toFloat());
        barchart!!.getXAxis().setAxisMaximum(0 + barchart!!.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barchart!!.groupBars(0.toFloat(), groupSpace, barSpace);
        barchart!!.getData().setHighlightEnabled(false);
        barchart!!.invalidate();

        var l = barchart!!.getLegend();
        l.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        var xAxis = barchart!!.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        xAxis.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        xAxis.setAxisLineColor(R.color.textColorPrimary)
        xAxis.setAxisMaximum(6.toFloat());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(IndexAxisValueFormatter(xVals));
        //Y-axis
        barchart!!.getAxisRight().setEnabled(false);
        var leftAxis = barchart!!.getAxisLeft();
        leftAxis.setValueFormatter(LargeValueFormatter());
        leftAxis.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setAxisLineColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);
    }

    fun getDrawable(type:Int):Int
    {
        when(type)
        {
            0 -> return R.drawable.circle_gray
            1 -> return R.drawable.circle_positive
            2 -> return R.drawable.circle_negative
            else -> return R.drawable.circle_gray
        }
    }

    fun getMonthForInt(num: Int): String {
        var month = "wrong"
        val dfs = DateFormatSymbols()
        val months = dfs.getMonths()
        if (num >= 0 && num <= 11) {
            month = months[num]
        }
        return month
    }

}// Required empty public constructor
