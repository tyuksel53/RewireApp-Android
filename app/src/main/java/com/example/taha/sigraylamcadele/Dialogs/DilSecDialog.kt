package com.example.taha.sigraylamcadele.Dialogs


import android.content.Context
import android.content.res.Resources
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
    lateinit var myLangChanceInterface:onLanguageChanged
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.dialog_dil_sec, container, false)

        val ingilizce = v.findViewById<TextView>(R.id.tvIngilizce)
        val turkce = v.findViewById<TextView>(R.id.tvTurkce)
        val kapat = v.findViewById<Button>(R.id.btnDilSecKapat)
        val pb = v.findViewById<ProgressBar>(R.id.pbDilSec)
        dilsec = v.findViewById(R.id.tvDilSec)

        pb.visibility = View.INVISIBLE

        kapat.setOnClickListener {
            if(isUserCanClick)
            {
                dialog.dismiss()
            }
        }

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)


        Paper.init(activity)
        updateView(Paper.book().read<String>("language"))

        ingilizce.setOnClickListener {
            if(isUserCanClick)
            {
                pb.visibility = View.VISIBLE
                isUserCanClick = false
                val result = apiInterface?.updateUserLang("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        "english")

                result!!.enqueue(object: Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        isUserCanClick =  true
                        pb.visibility = View.INVISIBLE
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        isUserCanClick =  true
                        pb.visibility = View.INVISIBLE
                        if(response?.code() == 200)
                        {
                            Paper.book().write("language","en")
                            LocaleHelper.setLocale(activity!!,Paper.book().read<String>("language"))
                            updateView(Paper.book().read<String>("language"))
                            UserPortal.shares = null
                            Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                    Toast.LENGTH_SHORT).show()
                            myLangChanceInterface.languageChanged()
                            dialog.dismiss()
                        }else
                        {
                            Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    }

                })
            }


        }

        turkce.setOnClickListener {
            if(isUserCanClick)
            {
                pb.visibility = View.VISIBLE
                isUserCanClick = false
                val result = apiInterface?.updateUserLang("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        "türkçe")
                result?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        isUserCanClick = true
                        pb.visibility = View.INVISIBLE
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        isUserCanClick = true
                        if(response?.code() == 200)
                        {
                            Paper.book().write("language","tr")
                            LocaleHelper.setLocale(activity!!,Paper.book().read<String>("language"))
                            UserPortal.shares = null
                            updateView(Paper.book().read<String>("language"))
                            Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                    Toast.LENGTH_SHORT).show()
                            myLangChanceInterface.languageChanged()
                            dialog.dismiss()
                        }else
                        {
                            Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }

                })
            }


        }

        return v
    }

    override fun onResume() {
        super.onResume()
        updateView(Paper.book().read<String>("language"))
    }

    override fun onAttach(context: Context?) {

        myLangChanceInterface = targetFragment as onLanguageChanged

        super.onAttach(context)
    }

    private fun updateView(lang: String?) {
        val context = LocaleHelper.setLocale(activity,lang)
        UserPortal.myLangResource = context.resources

        dilsec?.setText(UserPortal.myLangResource?.getString(R.string.dil_secin))
    }


}
