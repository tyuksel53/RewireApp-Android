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

class SortByDialog : DialogFragment(),TimeOptionsDialog.onSortSelectedListener {
    interface sortSelected
    {
        fun sortSelected(selectedTime: String)
    }

    override fun sortFirstSelected(selectedTime: String) {
        myInterface.sortSelected(selectedTime)
    }


    lateinit var myInterface:sortSelected
    var new:TextView? = null
    var likes:TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.dialog_sort_by_dialog, container, false)

        new = view.findViewById<TextView>(R.id.tvSortByNew)
        likes = view.findViewById<TextView>(R.id.tvSortByLikes)

        val context = LocaleHelper.setLocale(activity, Paper.book().read<String>("language"))
        UserPortal.myLangResource = context.resources

        new?.setText(UserPortal.myLangResource?.getString(R.string.yeni))
        likes?.setText(UserPortal.myLangResource?.getString(R.string.top_likes))

        new?.setOnClickListener {
            myInterface.sortSelected("yeni-Tum-${UserPortal.myLangResource?.getString(R.string.yeni_gonderiler)}")
            dismiss()
        }

        likes?.setOnClickListener {
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
        myInterface = targetFragment as sortSelected
    }


}
