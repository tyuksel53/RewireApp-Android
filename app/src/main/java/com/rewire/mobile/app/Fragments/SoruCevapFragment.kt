package com.rewire.mobile.app.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.rewire.mobile.app.API.ApiClient
import com.rewire.mobile.app.API.ApiInterface
import com.rewire.mobile.app.Adapter.SoruCevapAdapter
import com.rewire.mobile.app.Dialogs.SortByDialog
import com.rewire.mobile.app.InsertUpdateShareActivity
import com.rewire.mobile.app.Library.UserPortal
import com.rewire.mobile.app.Model.Shares
import com.rewire.mobile.app.Model.User
import com.rewire.mobile.app.PaperHelper.LocaleHelper

import com.rewire.mobile.app.R
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoruCevapFragment : android.app.Fragment(),SortByDialog.sortSelected {
    var recyclerV:RecyclerView? = null
    var result:Call<ArrayList<Shares>>? = null
    var adapter:SoruCevapAdapter? = null
    var textSort:TextView? = null
    var paramlist = arrayListOf("yeni","Tum","${UserPortal.myLangResource!!.getString(R.string.yeni_gonderiler)}")
    var ivSort:ImageView? = null
    var progressBar:ProgressBar? = null
    var lengthCheck = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_soru_cevap, container, false)
        textSort = view.findViewById(R.id.tvSortBy)
        ivSort = view.findViewById(R.id.ivSort)
        textSort?.setOnClickListener {

            val dialog = SortByDialog()
            dialog.setTargetFragment(this,2)
            dialog.show(fragmentManager,"sort")
        }
        setSortText(paramlist)

        Paper.init(activity)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }



        updateView(Paper.book().read("language"))
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser?.AccessToken}"
                ,0,
                paramlist[0],
                paramlist[1])

        recyclerV = view.findViewById<RecyclerView>(R.id.rvSoruCevap)
        progressBar = view.findViewById<ProgressBar>(R.id.pbSoruCevap)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeSoruCevap)
        val fb = view.findViewById<FloatingActionButton>(R.id.fbInsertShare)

        fb.setOnClickListener {
            val intent = Intent(activity,InsertUpdateShareActivity::class.java)
            startActivity(intent)

        }

        swipeRefresh.setOnRefreshListener {
            result?.clone()?.enqueue(object:Callback<ArrayList<Shares>>{
                override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                    swipeRefresh.setRefreshing(false)
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                            ,Toast.LENGTH_LONG)
                            .show()
                }

                override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
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
            result?.enqueue(object:Callback<ArrayList<Shares>>{
                override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                    progressBar?.visibility = View.INVISIBLE
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                            ,Toast.LENGTH_LONG)
                            .show()
                }
                override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
                    progressBar?.visibility = View.INVISIBLE
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
            progressBar?.visibility = View.INVISIBLE
        }



        return view
    }

    private fun updateView(lang: String) {
        val context = LocaleHelper.setLocale(activity,lang)
        UserPortal.myLangResource = context.resources
    }

    override fun onResume() {
        if(UserPortal.newShare)
        {
            UserPortal.newShare = false
            result?.clone()?.enqueue(object:Callback<ArrayList<Shares>>{
                override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                    Toasty.error(activity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),Toast.LENGTH_LONG)
                            .show()
                }

                override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {

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
    fun initRecyclerView(source:ArrayList<Shares>?)
    {
        if(activity != null)
        {
            adapter = SoruCevapAdapter(source!!,activity)
            recyclerV!!.adapter = adapter
            val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            recyclerV!!.layoutManager = myManager

            recyclerV!!.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val offset = recyclerV!!.computeVerticalScrollOffset()
                    val extent = recyclerV!!.computeVerticalScrollExtent()
                    val range = recyclerV!!.computeVerticalScrollRange()

                    val percentage = (100.0 * offset / (range - extent))

                    if( (adapter?.getDataLength()!! % 25)  == 0 && UserPortal.shares?.size != 0 && percentage >70 && lengthCheck)
                    {
                        lengthCheck = false
                        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
                        result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                                UserPortal.shares!!.size,
                                paramlist[0],paramlist[1])

                        result?.clone()?.enqueue(object:Callback<ArrayList<Shares>>{
                            override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                                lengthCheck = true
                            }

                            override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
                                lengthCheck = true
                                if(response?.message()?.toString() == "OK") {
                                    val body = response.body()
                                    if(body?.size == 0)
                                    {
                                        lengthCheck = false
                                    }
                                    adapter?.newShares(body)
                                }else {
                                    Toasty.error(activity,
                                            UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                            ,Toast.LENGTH_LONG)
                                            .show()
                                }
                            }

                        })

                        result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                                0,
                                paramlist[0],paramlist[1])
                    }

                }
            })
        }
    }

    override fun sortSelected(selectedTime: String) {
        progressBar?.visibility = View.VISIBLE
        lengthCheck = true
        val selectedParams = selectedTime.split("-")
        setSortText(selectedParams as ArrayList)
        paramlist[0] = selectedParams[0]
        paramlist[1] = selectedParams[1]
        paramlist[2] = selectedParams[2]
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser?.AccessToken}"
                ,0,
                paramlist[0],
                paramlist[1])

        result?.clone()?.enqueue(object:Callback<ArrayList<Shares>>{
            override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers))
                progressBar?.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
                progressBar?.visibility = View.INVISIBLE
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
    }
    fun setSortText(selectedParams:ArrayList<String>)
    {
        if(selectedParams[0] == "like")
        {
            ivSort?.setImageResource(R.drawable.ic_action_heart)
            ivSort?.setColorFilter(ContextCompat.getColor(activity, R.color.myRed),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            textSort?.text = "${UserPortal.myLangResource!!.getString(R.string.top_likes)} - ${selectedParams[2]}"
        }else if(selectedParams[0] == "yeni")
        {   ivSort?.setImageResource(R.drawable.ic_timeline)
            ivSort?.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            textSort?.text = "${UserPortal.myLangResource!!.getString(R.string.yeni_gonderiler)}"
        }
    }
}// Required empty public constructor
