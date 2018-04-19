package com.example.taha.sigraylamcadele.Fragments


import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.SoruCevapAdapter
import com.example.taha.sigraylamcadele.InsertShare
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper

import com.example.taha.sigraylamcadele.R
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoruCevapFragment : android.app.Fragment() {
    var recyclerV:RecyclerView? = null
    var result:Call<List<Shares>>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_soru_cevap, container, false)


        Paper.init(activity)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }


        updateView(Paper.book().read("language"))
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser?.AccessToken}",0)
        recyclerV = view.findViewById<RecyclerView>(R.id.rvSoruCevap)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbSoruCevap)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeSoruCevap)
        val fb = view.findViewById<FloatingActionButton>(R.id.fbInsertShare)

        fb.setOnClickListener {
            var intent = Intent(activity,InsertShare::class.java)
            startActivity(intent)

        }

        swipeRefresh.setOnRefreshListener {
            result?.clone()?.enqueue(object:Callback<List<Shares>>{
                override fun onFailure(call: Call<List<Shares>>?, t: Throwable?) {
                    swipeRefresh.setRefreshing(false)
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                            ,Toast.LENGTH_LONG)
                            .show()
                }

                override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {
                    if(response?.message()?.toString() == "OK") {
                        val body = response.body()
                        UserPortal.shares = body
                        initRecyclerView(body)
                        swipeRefresh.setRefreshing(false)
                    }else {
                        Toasty.error(activity,
                                UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                ,Toast.LENGTH_LONG)
                                .show()
                        swipeRefresh.setRefreshing(false)
                    }
                }

            })
        }

        if(UserPortal.shares == null)
        {
            result?.enqueue(object:Callback<List<Shares>>{
                override fun onFailure(call: Call<List<Shares>>?, t: Throwable?) {
                    progressBar.visibility = View.INVISIBLE
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                            ,Toast.LENGTH_LONG)
                            .show()
                }
                override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {
                    progressBar.visibility = View.INVISIBLE
                    if(response?.message()?.toString() == "OK")
                    {
                        val body = response.body()
                        UserPortal.shares = body
                        initRecyclerView(body)

                    }else
                    {
                        Toasty.error(activity,
                                UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                ,Toast.LENGTH_LONG)
                                .show()
                    }}

            })
        }else
        {
            initRecyclerView(UserPortal.shares)
            progressBar.visibility = View.INVISIBLE
        }



        return view
    }

    private fun updateView(lang: String) {
        val context = LocaleHelper.setLocale(activity,lang)
        UserPortal.myLangResource = context.resources

    }

    override fun onResume() {
        updateView(Paper.book().read<String>("language"))
        if(UserPortal.newShare)
        {
            UserPortal.newShare = false
            result?.clone()?.enqueue(object:Callback<List<Shares>>{
                override fun onFailure(call: Call<List<Shares>>?, t: Throwable?) {
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),Toast.LENGTH_LONG)
                            .show()
                }

                override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {

                    if(response?.message()?.toString() == "OK") {
                        val body = response.body()
                        UserPortal.shares = body
                        initRecyclerView(body)
                    }else {
                        Toasty.error(activity,
                                UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                ,Toast.LENGTH_LONG)
                                .show()
                    }
                }

            })
        }else if(UserPortal.hasSharesChanged)
        {
            UserPortal.hasSharesChanged = false
            if(UserPortal.shares != null)
            {
                recyclerV!!.adapter = SoruCevapAdapter(UserPortal.shares!!,activity)
            }
        }
        super.onResume()
    }
    fun initRecyclerView(source:List<Shares>?)
    {
        val adapter = SoruCevapAdapter(source!!,activity)
        recyclerV!!.adapter = adapter
        val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recyclerV!!.layoutManager = myManager
        adapter.updateUserInteraction()
    }

}// Required empty public constructor
