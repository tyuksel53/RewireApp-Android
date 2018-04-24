package com.example.taha.sigraylamcadele.Dialogs


import android.annotation.TargetApi
import android.app.Activity
import android.app.DialogFragment
import android.os.Bundle
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import io.paperdb.Paper


class TimeOptionsDialog : DialogFragment() {

    interface onSortSelectedListener{

        fun sortFirstSelected(selectedTime:String)

    }

    lateinit var myOnSortSelectedListener:onSortSelectedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.dialog_time_options_dialog, container, false)

        val type = arguments.getString("type")

        val lastHour = view.findViewById<TextView>(R.id.tvLastHour)
        val lastDay = view.findViewById<TextView>(R.id.tvLastDay)
        val lastWeek = view.findViewById<TextView>(R.id.tvLastWeek)
        val lastMonth = view.findViewById<TextView>(R.id.tvLastmonth)
        val lastYear = view.findViewById<TextView>(R.id.tvLastYear)
        val alltime = view.findViewById<TextView>(R.id.tvAllTime)
        val sortby = view.findViewById<TextView>(R.id.tvSortByLikesHeader)
        val context = LocaleHelper.setLocale(activity, Paper.book().read<String>("language"))
        UserPortal.myLangResource = context.resources

        lastHour.setText(UserPortal.myLangResource?.getString(R.string.bir_saat_once))
        lastDay.setText(UserPortal.myLangResource?.getString(R.string.son_bir_gun))
        lastWeek.setText(UserPortal.myLangResource?.getString(R.string.son_bir_hafta))
        lastMonth.setText(UserPortal.myLangResource?.getString(R.string.son_bir_ay))
        lastYear.setText(UserPortal.myLangResource?.getString(R.string.son_bir_yil))
        alltime.setText(UserPortal.myLangResource?.getString(R.string.tum_zamanlar))
        sortby.setText(UserPortal.myLangResource?.getString(R.string.sirala))

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

    @TargetApi(23)
    override fun onAttach(context:Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            onAttachToContext(activity)
        }
    }

    protected fun onAttachToContext(context:Context) {
        myOnSortSelectedListener = targetFragment as onSortSelectedListener
    }



}
