package com.example.taha.sigraylamcadele.Fragments



import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Dialogs.DilSecDialog
import com.example.taha.sigraylamcadele.Dialogs.ThanksDialog
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.LoginActivity
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import com.example.taha.sigraylamcadele.SifreDegisActivity
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AyarlarFragment : android.app.Fragment(),DilSecDialog.onLanguageChanged {
    override fun languageChanged() {
        updateLanguage()
    }


    var language:TextView? = null
    var tvGenel:TextView?=null
    var switchNotifiy: Switch? = null
    var tvHesap:TextView? = null
    var tvSifreDegistir:TextView? = null
    var tvDateReset:TextView? = null
    var tvUygulama:TextView? = null
    var tvOpenSource:TextView? =null
    var tvThanks:TextView? = null
    var tvSupport:TextView? = null
    var tvSupportAction:TextView? = null
    var tvCheckUp:TextView? = null
    var tvExit:TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_ayarlar, container, false)
        updateLanguage()

        language = view.findViewById(R.id.tvSettingsLanguage)
        switchNotifiy = view.findViewById(R.id.swBildirim)
        tvGenel = view.findViewById(R.id.tvAyarlarGenel)
        tvHesap = view.findViewById(R.id.tvAyarlarHesap)
        tvSifreDegistir = view.findViewById(R.id.tvAyarlarSifreDegis)
        tvDateReset = view.findViewById(R.id.tvAyarlarDateReset)
        tvUygulama = view.findViewById(R.id.tvAyarlarUygulama)
        tvOpenSource = view.findViewById(R.id.tvAyarlarOpenSource)
        tvThanks = view.findViewById(R.id.tvAyarlarThanks)
        tvSupport = view.findViewById(R.id.tvAyarlarSupport)
        tvSupportAction = view.findViewById(R.id.tvayarlarSupportAction)
        tvCheckUp = view.findViewById(R.id.tvAyarlarCheckUpTime)
        tvExit = view.findViewById(R.id.tvExitApp)

        tvExit?.setOnClickListener {
            if(UserPortal.deleteLoggedInUser(activity))
            {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Toast.makeText(activity,UserPortal.myLangResource!!.getString(R.string.cikis_basarili),Toast.LENGTH_SHORT).show()
                UserPortal.reset()
                activity.finish()
            }
        }

        val list = ArrayList<String>()
        val times = resources.getStringArray(R.array.time)
        for(i in 0 until times.size)
        {
            list.add(times[i])
        }

        val spinner = view.findViewById<Spinner>(R.id.spAyarlarTime)
        val adp= ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1,
                list)
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adp)

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)


        updateLanguage()
        language?.setOnClickListener {
            val dialog = DilSecDialog()
            dialog.setTargetFragment(this,5)
            dialog.show(fragmentManager,"tag")
        }

        tvDateReset?.setOnClickListener {
            val dialog = AlertDialog.Builder(it.context)
            dialog.setTitle(UserPortal.myLangResource!!.getString(R.string.Emin_Misin))
            dialog.setMessage(UserPortal.myLangResource!!.getString(R.string.Butun_tarihler_silinecek))
            dialog.setCancelable(true)
            dialog.setPositiveButton(UserPortal.myLangResource!!.getString(R.string.Evet)) { dialog, which ->
                val result = apiInterface?.deleteAllDates("Bearer ${UserPortal.loggedInUser!!.AccessToken}")
                result?.clone()?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {

                    }

                })
                UserPortal.userDates = ArrayList()
                if(activity != null)
                {
                    Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                            Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton(UserPortal.myLangResource!!.getString(R.string.Hayır)) { dialog, which ->

            }
            dialog.show()
        }

        tvSifreDegistir?.setOnClickListener {

            if(activity != null)
            {
                val intent = Intent(activity,SifreDegisActivity::class.java)
                startActivity(intent)
            }

        }

        tvThanks?.setOnClickListener{
            val dialog = ThanksDialog()
            dialog.show(fragmentManager,"Thanks")
        }

        return view
    }

    fun updateLanguage()
    {
        val context = LocaleHelper.setLocale(activity, Paper.book().read<String>("language"))
        UserPortal.myLangResource = context.resources

        language?.setText(UserPortal.myLangResource?.getString(R.string.Dil_degistirin))
        tvGenel?.setText(UserPortal.myLangResource?.getString(R.string.genel))
        switchNotifiy?.setText(UserPortal.myLangResource?.getString(R.string.bildirim))
        tvSupportAction?.setText(UserPortal.myLangResource?.getString(R.string.bize_yildiz_vererek_destek_olun))
        tvSupport?.setText(UserPortal.myLangResource?.getString(R.string.destek))
        tvOpenSource?.setText(UserPortal.myLangResource?.getString(R.string.acik_kaynak_kutuphaneleri))
        tvUygulama?.setText(UserPortal.myLangResource?.getString(R.string.uygulama))
        tvDateReset?.setText(UserPortal.myLangResource?.getString(R.string.tarih_bilgilerini_sifirla))
        tvSifreDegistir?.setText(UserPortal.myLangResource?.getString(R.string.sifre_degistir))
        tvHesap?.setText(UserPortal.myLangResource?.getString(R.string.hesap))
        tvThanks?.setText(UserPortal.myLangResource?.getString(R.string.tesekkur))
        tvCheckUp?.setText(UserPortal.myLangResource?.getString(R.string.Check_Up_Time))
        tvExit?.setText(UserPortal.myLangResource!!.getString(R.string.cikis_yap))

    }


}// Required empty public constructor
