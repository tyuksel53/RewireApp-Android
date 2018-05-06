package com.example.taha.sigraylamcadele.Fragments



import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Dialogs.DilSecDialog
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.LoginActivity
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import com.example.taha.sigraylamcadele.ChangePasswordActivity
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.OpenSourceActivity
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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
        tvSupport = view.findViewById(R.id.tvAyarlarSupport)
        tvSupportAction = view.findViewById(R.id.tvayarlarSupportAction)
        tvCheckUp = view.findViewById(R.id.tvAyarlarCheckUpTime)
        tvExit = view.findViewById(R.id.tvExitApp)

        if(activity != null)
        {
            val userSettings = Portal.getSettings(activity,UserPortal.loggedInUser!!.Username!!)
            if(userSettings != null)
            {
                if(userSettings.Notification == "YES")
                {
                    switchNotifiy?.setChecked(true)
                }else
                {
                    switchNotifiy?.setChecked(false)
                }
            }
        }

        tvOpenSource?.setOnClickListener {

            val intent = Intent(activity,OpenSourceActivity::class.java)
            activity.startActivity(intent)

        }
        switchNotifiy?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(activity != null)
            {
                if(switchNotifiy?.isChecked!!)
                {
                    Portal.updateUserSettingsNotification(activity,"YES",
                            UserPortal.loggedInUser!!.Username!!)
                    Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.Bildirim_On),
                            Toast.LENGTH_SHORT).show()
                }else
                {
                    Portal.updateUserSettingsNotification(activity,"NO",
                            UserPortal.loggedInUser!!.Username!!)
                    Toasty.success(activity,UserPortal.myLangResource!!.getString(R.string.Bildirim_OFF),
                            Toast.LENGTH_SHORT).show()
                }
            }

        }

        tvExit?.setOnClickListener {
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle(UserPortal.myLangResource!!.getString(R.string.Emin_Misin))
            dialog.setMessage(UserPortal.myLangResource!!.getString(R.string.cikis_yapacaksin))
            dialog.setCancelable(true)
            dialog.setPositiveButton(UserPortal.myLangResource!!.getString(R.string.Evet)) { dialog, which ->
                if(UserPortal.deleteLoggedInUser(activity))
                {
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toasty.success(activity,
                            UserPortal.myLangResource!!.getString(R.string.cikis_basarili)
                            ,Toast.LENGTH_SHORT).show()
                    UserPortal.reset()
                    activity.finish()
                }else
                {
                    Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                            Toast.LENGTH_LONG).show()
                }
            }
            dialog.setNegativeButton(UserPortal.myLangResource!!.getString(R.string.Hayır)) { dialog, which ->

            }
            dialog.show()

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
        if(activity != null)
        {
            val userSettings = Portal.getSettings(activity,UserPortal.loggedInUser!!.Username!!)
            val time = userSettings!!.UserCheckUpTime!!.split(':')[0].toInt()
            spinner.setSelection(time)
        }

        var mundi = 0
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {

                if(activity != null)
                {
                    if(++mundi>1)
                    {
                        val item = spinner.selectedItem.toString()
                        Portal.updateUserSettingsCheckUpTime(activity,item,
                                UserPortal.loggedInUser!!.Username!!)
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

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
                val intent = Intent(activity,ChangePasswordActivity::class.java)
                startActivity(intent)
            }

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
        tvCheckUp?.setText(UserPortal.myLangResource?.getString(R.string.Check_Up_Time))
        tvExit?.setText(UserPortal.myLangResource!!.getString(R.string.cikis_yap))

    }


}