package com.example.taha.sigraylamcadele.Fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    var result: Call<ArrayList<UserDate>>? = null
    var responseBody:ArrayList<UserDate>? = null
    lateinit var calendar:MaterialCalendarView
    val userNewSelections = ArrayList<UserDate>()
    val userUpdateselections = ArrayList<UserDate>()
    var barchart:BarChart?=null
    var combineChart:LineChart? = null
    var pieChart:PieChart? = null
    lateinit var btnSave:Button
    var today:Date? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        responseBody = UserPortal.userDates
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
        btnSave = view.findViewById<Button>(R.id.btnIstatistikSaveChanges)
        btnSave.visibility = View.INVISIBLE
        btnSave.setText(UserPortal.myLangResource!!.getString(R.string.degisiklikleri_kaydet))

        btnSave.setOnClickListener {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

            if(userNewSelections.size > 0)
            {
                val insertResult = apiInterface?.insertDates("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        dates = userNewSelections)

                insertResult?.clone()?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                                Toast.LENGTH_SHORT).show()
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
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                                Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    }

                })

            }
            for(i in 0 until userNewSelections.size)
            {
                responseBody!!.add(userNewSelections[i])
            }
            UserPortal.userDates = responseBody
            userNewSelections.clear()
            userUpdateselections.clear()
            UserPortal.datesHasChanged = true
            Toasty.success(activity,
                    UserPortal.myLangResource!!.getString(R.string.degisikler_kaydedildi)
                    ,Toast.LENGTH_SHORT)
                    .show()
            btnSave.visibility = View.INVISIBLE
            initBarChart()
        }
        // btnsave end
        barchart = view.findViewById(R.id.chartGun)
        combineChart = view.findViewById(R.id.chartSigara)
        pieChart = view.findViewById(R.id.chartAll)

        if(UserPortal.loggedInUser?.LastLoginTime != null)
        {
            var currentDay = CalendarDay.from(
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
            // today
            today = rangeCalander.getTime()
            initBarChart()
            calendar.addDecorator(CircleDecorator(activity,CalendarDay.from(today!!),
                    ContextCompat.getDrawable(activity, R.drawable.circle_blue)!!))

            for(i in 0 until responseBody!!.size)
            {
                val date  = CalendarDay.from(
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
            val c = Calendar.getInstance()
            c.setTime(currentDay.date)
            c.add(Calendar.DATE, 1)
            currentDay = CalendarDay.from(c.getTime())

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
            barchart!!.visibility = View.INVISIBLE
            calendar!!.visibility = View.INVISIBLE
            pieChart!!.visibility = View.INVISIBLE
            combineChart!!.visibility = View.INVISIBLE
            UserPortal.updateUserInfo()
        }

        return view
    }

    private fun initSigaraChart(listAy:ArrayList<GrafikAy>) {
        val entries =  ArrayList<Entry>()
        val isimler = ArrayList<String>()
        if(listAy.size == 0)
        {
            isimler.add(getMonthForInt(today!!.month))
            entries.add(Entry(0f, 0f))

        }else
        {

            for(i in 0 until listAy.size)
            {
                entries.add(Entry(i.toFloat(), listAy[i].SmokeCount))
                isimler.add(listAy[i].AyIsim)
            }

        }
        val setComp1 =  LineDataSet(entries, UserPortal.myLangResource!!.getString(R.string.Sigara_Sayisi))
        setComp1.setColor(ContextCompat.getColor(activity, R.color.myRed))
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setCircleColor(Color.WHITE)
        val dataSets =  ArrayList<ILineDataSet>()
        dataSets.add(setComp1)

        combineChart!!.setDescription(null)
        combineChart!!.setPinchZoom(false)
        combineChart!!.setScaleEnabled(false)
        combineChart!!.setDrawGridBackground(false)

        val data =  LineData(dataSets)
        data.setValueTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        data.setValueFormatter(LargeValueFormatter())
        combineChart!!.setData(data)
        combineChart!!.invalidate()

        val xAxis = combineChart!!.getXAxis()
        xAxis.setValueFormatter(IndexAxisValueFormatter(isimler))
        xAxis.setAxisLineColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        xAxis.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        xAxis.setAxisLineColor(R.color.textColorPrimary)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setAxisMaximum(6.toFloat())

        val l = combineChart!!.getLegend()
        l.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(true)
        l.setYOffset(20f)
        l.setXOffset(0f)
        l.setYEntrySpace(0f)
        l.setTextSize(8f)
        val rightAxis = combineChart!!.getAxisRight()
        rightAxis.setEnabled(false)


        val leftAxis = combineChart!!.getAxisLeft();
        leftAxis.setValueFormatter(LargeValueFormatter());
        leftAxis.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setAxisLineColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setSpaceTop(35f)
        leftAxis.setAxisMinimum(0f)
        leftAxis.setDrawAxisLine(true)
    }

    private fun initBarChart() {
        val listAy = ArrayList<GrafikAy>()
        val isimler = ArrayList<String>()
        val entriesGood =  ArrayList<BarEntry>()
        val entriesBad = ArrayList<BarEntry>()
        if(responseBody!!.size > 0)
        {

            var deneme = GrafikAy()

            responseBody!!.sortByDescending { x->Portal.textToDate(x.Date!!) }
            var ayNum = Portal.textToDate(responseBody!![0].Date!!).getMonth()
            deneme.AyNumara = ayNum
            deneme.AyIsim = getMonthForInt(ayNum)
            UserPortal.userDates = responseBody
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
                    deneme.SmokeCount = deneme.SmokeCount + responseBody!![i].SmokeCount!!
                }
            }
            val checkAy = listAy.find { x->x.AyNumara == deneme.AyNumara }
            if( (deneme.BadDay != 0  || deneme.GoodDay != 0) && checkAy == null )
            {
                listAy.add(deneme)
            }
            initSigaraChart(listAy)
            initPieChart(listAy)
        }else
        {
            initSigaraChart(ArrayList())
            initPieChart(ArrayList())
            isimler.add(getMonthForInt(today!!.month))

            entriesGood.add(BarEntry(0.toFloat(), 0.toFloat()))
            entriesBad.add(BarEntry(0.toFloat(),  0.toFloat()))

        }


        for(i in 0 until listAy.size)
        {
            entriesGood.add(BarEntry(i.toFloat(), listAy[i].GoodDay.toFloat()))
            entriesBad.add(BarEntry(i.toFloat(), listAy[i].BadDay.toFloat()))
            isimler.add(listAy[i].AyIsim)
        }

        val barWidth = 0.3f
        val barSpace = 0f
        val groupSpace = 0.4f

        barchart!!.setDescription(null)
        barchart!!.setPinchZoom(false)
        barchart!!.setScaleEnabled(false)
        barchart!!.setDrawBarShadow(false)
        barchart!!.setDrawGridBackground(false)

        val groupCount = isimler.size

        val xVals =  isimler

        val set1 =  BarDataSet(entriesGood, UserPortal.myLangResource!!.getString(R.string.ClearDay))
        set1.setColor(ContextCompat.getColor(activity, R.color.barGreen));
        val set2 = BarDataSet(entriesBad, UserPortal.myLangResource!!.getString(R.string.BadDay))
        set2.setColor(ContextCompat.getColor(activity, R.color.barRed));
        val data =  BarData(set1, set2)
        data.setValueTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        data.setValueFormatter(LargeValueFormatter());
        barchart!!.setData(data)
        barchart!!.getBarData().setBarWidth(barWidth);
        barchart!!.getXAxis().setAxisMinimum(0.toFloat());
        barchart!!.getXAxis().setAxisMaximum(0 + barchart!!.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barchart!!.groupBars(0.toFloat(), groupSpace, barSpace);
        barchart!!.getData().setHighlightEnabled(false);
        barchart!!.invalidate();

        val l = barchart!!.getLegend()
        l.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(true)
        l.setYOffset(20f)
        l.setXOffset(0f)
        l.setYEntrySpace(0f)
        l.setTextSize(8f)

        val xAxis = barchart!!.getXAxis();
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

        barchart!!.getAxisRight().setEnabled(false);
        val leftAxis = barchart!!.getAxisLeft();
        leftAxis.setValueFormatter(LargeValueFormatter());
        leftAxis.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setAxisLineColor(ContextCompat.getColor(activity, R.color.textColorPrimary))
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);
    }

    private fun initPieChart(listAy: ArrayList<GrafikAy>) {
        val entries =  ArrayList<PieEntry>()
        val isimler = ArrayList<String>()
        if(listAy.size == 0)
        {
            isimler.add(getMonthForInt(today!!.month))
            entries.add(PieEntry(0f, 0f))

        }else
        {
            var goodDayCount = 0
            var badDayCount = 0
            for(i in 0 until listAy.size)
            {
                goodDayCount+= listAy[i].GoodDay
                badDayCount += listAy[i].BadDay
            }
            entries.add(PieEntry(goodDayCount.toFloat()))
            entries.add(PieEntry(badDayCount.toFloat()))
        }
        val pieDataSet =  PieDataSet(entries, UserPortal.myLangResource!!.getString(R.string.ButunGunler))
        pieDataSet.setSliceSpace(2f)
        pieDataSet.setValueTextSize(12f)
        //add colors to dataset
        val colors =  ArrayList<Int>()
        colors.add(ContextCompat.getColor(activity, R.color.barGreen))
        colors.add(ContextCompat.getColor(activity, R.color.barRed))

        pieDataSet.setColors(colors)

        //add legend to chart
        var legend = pieChart!!.getLegend()
        legend.setForm(Legend.LegendForm.CIRCLE)
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART)
        legend.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary))

        val pieData =  PieData(pieDataSet)
        pieChart!!.setData(pieData)
        pieChart!!.setDescription(null)
        pieChart!!.setHoleColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
        pieChart!!.invalidate()
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
