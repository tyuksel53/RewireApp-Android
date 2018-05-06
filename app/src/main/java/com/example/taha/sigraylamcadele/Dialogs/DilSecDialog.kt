package com.example.taha.sigraylamcadele.Dialogs


import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.dialog_dil_sec.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DilSecDialog : android.app.DialogFragment() {

    interface onLanguageChanged
    {
        fun languageChanged()

    }
    var isUserCanClick = true
    var dilsec:TextView? = null
    var pb:ProgressBar? = null
    lateinit var myLangChanceInterface:onLanguageChanged
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.dialog_dil_sec, container, false)

        val ingilizce = v.findViewById<TextView>(R.id.tvIngilizce)
        val turkce = v.findViewById<TextView>(R.id.tvTurkce)
        val kapat = v.findViewById<Button>(R.id.btnDilSecKapat)
        pb = v.findViewById<ProgressBar>(R.id.pbDilSec)
        val almanca = v.findViewById<TextView>(R.id.tvAlmanca)
        val isponyalca = v.findViewById<TextView>(R.id.tvIspanyolca)
        val fransizca = v.findViewById<TextView>(R.id.tvFransızca)
        val italyanca = v.findViewById<TextView>(R.id.tvItalyanca)
        val rusca = v.findViewById<TextView>(R.id.tvRusca)
        dilsec = v.findViewById(R.id.tvDilSec)

        pb?.visibility = View.INVISIBLE

        kapat.setOnClickListener {
            if(isUserCanClick)
            {
                dialog.dismiss()
            }
        }

        updateView(Paper.book().read<String>("language"))

        rusca.setOnClickListener {
            updateLang("русский","ru")
        }

        italyanca.setOnClickListener {
            updateLang("italiano","it")
        }

        fransizca.setOnClickListener {
            updateLang("français","fr")
        }

        isponyalca.setOnClickListener {
            updateLang("español","es")
        }

        almanca.setOnClickListener {
            updateLang("deutsch","de")
        }

        ingilizce.setOnClickListener {

            updateLang("english","en")
        }

        turkce.setOnClickListener {
            updateLang("türkçe","tr")
        }



        return v
    }

    override fun onResume() {
        super.onResume()
        updateView(Paper.book().read<String>("language"))
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
        myLangChanceInterface = targetFragment as onLanguageChanged
    }

    private fun updateView(lang: String?) {
        val context = LocaleHelper.setLocale(activity,lang)
        UserPortal.myLangResource = context.resources

        dilsec?.setText(UserPortal.myLangResource?.getString(R.string.dil_secin))
    }

    fun updateLang(lang:String,lancode:String)
    {
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        if(isUserCanClick)
        {
            pb?.visibility = View.VISIBLE
            isUserCanClick = false
            val result = apiInterface?.updateUserLang("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                    lang)

            result!!.enqueue(object: Callback<String>{
                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    if(activity != null)
                    {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG).show()
                        if(dialog!=null)
                        {
                            dialog.dismiss()
                        }
                        isUserCanClick =  true
                        pb?.visibility = View.INVISIBLE
                    }

                }

                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    isUserCanClick =  true
                    pb?.visibility = View.INVISIBLE
                    if(response?.code() == 200)
                    {
                        if(activity != null)
                        {
                            Paper.book().write("language",lancode)
                            LocaleHelper.setLocale(activity!!,Paper.book().read<String>("language"))
                            updateView(Paper.book().read<String>("language"))
                            UserPortal.shares = null
                            Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                    Toast.LENGTH_SHORT).show()
                            myLangChanceInterface.languageChanged()
                            if(dialog!=null)
                            {
                                dialog.dismiss()
                            }
                        }

                    }else
                    {
                        if(activity != null)
                        {
                            Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_LONG).show()
                            if(dialog!=null)
                            {
                                dialog.dismiss()
                            }
                        }

                    }
                }

            })
        }
    }

}
