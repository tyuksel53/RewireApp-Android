package com.rewire.mobile.app.Dialogs


import android.annotation.TargetApi
import android.app.Activity
import android.app.DialogFragment
import android.os.Bundle
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.rewire.mobile.app.Library.Portal
import com.rewire.mobile.app.Library.UserPortal
import com.rewire.mobile.app.Model.UserDate

import com.rewire.mobile.app.R
import com.rengwuxian.materialedittext.MaterialEditText

class SmokeDialog : DialogFragment() {

    interface onSmokeCountEntered
    {
        fun smokeCountChanged(cigarecigaretteCount:Int,check:UserDate?,type:String?,date:String?)
    }
    lateinit var mySomeCountInterface: onSmokeCountEntered
    var userHasEnteredValue = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.dialog_smoke, container, false)
        val btnSave = view.findViewById<Button>(R.id.btnSmokeCountSave)
        btnSave.setText(UserPortal.myLangResource!!.getString(R.string.kaydet))
        val smokeCountTxt = view.findViewById<MaterialEditText>(R.id.edSmokeCountDialog)
        smokeCountTxt.setHint(UserPortal.myLangResource!!.getString(R.string.sigara_sayis))
        val header = view.findViewById<TextView>(R.id.tvSmokeCountHeader)
        val type = arguments.getString("type")
        val check = arguments.getSerializable("check") as UserDate
        val date = arguments.getString("date")
        header.text = Portal.textDateToFormatted(date)  + "\n\n"+  UserPortal.myLangResource!!.getString(R.string.tarihinde_ne_kadar_sigara_ictin)

        btnSave.setOnClickListener {

            if(smokeCountTxt.text.isNullOrBlank() || smokeCountTxt.text.isNullOrEmpty())
            {
                smokeCountTxt.setError(UserPortal.myLangResource!!.getString(R.string.smokeCountBos))
                return@setOnClickListener
            }

            if(smokeCountTxt.text.toString().toDouble() < 0 )
            {
                smokeCountTxt.setError(UserPortal.myLangResource!!.getString(R.string.smokeCountNegatif))
                return@setOnClickListener
            }

            if(smokeCountTxt.text.toString().toDouble() == 0.0)
            {
                smokeCountTxt.setError(UserPortal.myLangResource!!.getString(R.string.hata_sifir))
                return@setOnClickListener
            }

            if(smokeCountTxt.text.toString().toDouble() > 2147483646)
            {
                smokeCountTxt.setError(UserPortal.myLangResource!!.getString(R.string.Sigara_hata_max))
                return@setOnClickListener
            }

            mySomeCountInterface.smokeCountChanged(smokeCountTxt.text.toString().toInt(),check,type,date)

            userHasEnteredValue = true
            dismiss()
        }

        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if(userHasEnteredValue == false)
        {
            mySomeCountInterface.smokeCountChanged(0,null,null,null)
        }
        super.onDismiss(dialog)
    }

    @TargetApi(23)
    override fun onAttach(context:Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }
    /*
    * Deprecated on API 23
    * Use onAttachToContext instead
    */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            onAttachToContext(activity)
        }
    }

    protected fun onAttachToContext(context:Context) {
        mySomeCountInterface = targetFragment as onSmokeCountEntered
    }


}
