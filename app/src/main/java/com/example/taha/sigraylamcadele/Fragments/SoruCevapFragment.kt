package com.example.taha.sigraylamcadele.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.SoruCevapAdapter
import com.example.taha.sigraylamcadele.InsertShare
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.Model.User

import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.fragment_soru_cevap.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoruCevapFragment : Fragment() {

    var recyclerV:RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_soru_cevap, container, false)

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        val result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser?.AccessToken}")
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
                    UserPortal.counter++
                    Toast.makeText(activity,"Counter:${UserPortal.counter}",Toast.LENGTH_LONG).show()
                    swipeRefresh.setRefreshing(false)
                }

                override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {
                    UserPortal.counter++
                    Toast.makeText(activity,"Counter:${UserPortal.counter}",Toast.LENGTH_LONG).show()
                    if(response?.message()?.toString() == "OK") {
                        val body = response.body()
                        UserPortal.shares = body
                        initRecyclerView(body)
                        swipeRefresh.setRefreshing(false)
                    }else {
                        Toast.makeText(activity,"Bir şeyler ters gitti",Toast.LENGTH_LONG)
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
                    UserPortal.counter++
                    Toast.makeText(activity,"Counter:${UserPortal.counter}",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {
                    UserPortal.counter++
                    Toast.makeText(activity,"Counter:${UserPortal.counter}",Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE
                    if(response?.message()?.toString() == "OK")
                    {
                        val body = response.body()
                        UserPortal.shares = body
                        initRecyclerView(body)

                    }else
                    {
                        Toast.makeText(activity,"Bir şeyler ters gitti",Toast.LENGTH_LONG)
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

    fun initRecyclerView(source:List<Shares>?)
    {
        recyclerV!!.adapter = SoruCevapAdapter(source!!)
        val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recyclerV!!.layoutManager = myManager
    }

}// Required empty public constructor
