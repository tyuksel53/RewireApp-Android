package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.LeaderBoardAdapter
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.User

import com.example.taha.sigraylamcadele.R
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class LeaderBoardFragment : android.app.Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_leaderboard, container, false)
        val rank = view.findViewById<TextView>(R.id.tvR)
        rank.setText(UserPortal.myLangResource!!.getString(R.string.rank))
        val username = view.findViewById<TextView>(R.id.tvU)
        username.setText(UserPortal.myLangResource!!.getString(R.string.kullaniciAdi))
        val ClearDay = view.findViewById<TextView>(R.id.tvC)
        ClearDay.setText(UserPortal.myLangResource!!.getString(R.string.temiz_gun))

        val recyclerV = view.findViewById<RecyclerView>(R.id.rvLeaderBoard)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbLeaderBoard)
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeLeaderBoard)
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        val result = apiInterface?.getLeaderBoard("Bearer ${UserPortal.loggedInUser!!.AccessToken}")

        swipe.setOnRefreshListener {
            result?.clone()?.enqueue(object:Callback<ArrayList<User>>{
                override fun onFailure(call: Call<ArrayList<User>>?, t: Throwable?) {
                    swipe.setRefreshing(false)
                    Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                            Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ArrayList<User>>?, response: Response<ArrayList<User>>?) {
                    swipe.setRefreshing(false)
                    if(response?.code() == 200)
                    {
                        if(activity != null)
                        {
                            UserPortal.leaderBoard = response.body()
                            val adapter = LeaderBoardAdapter(UserPortal.leaderBoard!!,activity)
                            recyclerV.adapter = adapter
                            val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                            recyclerV.layoutManager = myManager
                        }

                    }else
                    {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG).show()
                    }
                }

            })
        }


        if(UserPortal.leaderBoard == null)
        {
            result?.clone()?.enqueue(object:Callback<ArrayList<User>>{
                override fun onFailure(call: Call<ArrayList<User>>?, t: Throwable?) {
                    progressBar.visibility = View.INVISIBLE
                    Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                            Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ArrayList<User>>?, response: Response<ArrayList<User>>?) {
                    progressBar.visibility = View.INVISIBLE
                    if(response?.code() == 200)
                    {
                        if(activity != null)
                        {
                            UserPortal.leaderBoard = response.body()
                            val adapter = LeaderBoardAdapter(response.body()!!,activity)
                            recyclerV.adapter = adapter
                            val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                            recyclerV.layoutManager = myManager
                        }

                    }else
                    {
                        Toasty.error(activity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG).show()
                    }
                }

            })
        }else
        {
            progressBar.visibility = View.INVISIBLE
            val adapter = LeaderBoardAdapter(UserPortal.leaderBoard!!,activity)
            recyclerV.adapter = adapter
            val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            recyclerV.layoutManager = myManager
        }



        return view
    }

}
