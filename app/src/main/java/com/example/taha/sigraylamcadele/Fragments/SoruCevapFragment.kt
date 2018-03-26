package com.example.taha.sigraylamcadele.Fragments


import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.SoruCevapAdapter
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares

import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.fragment_soru_cevap.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class SoruCevapFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_soru_cevap, container, false)

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        val result = apiInterface?.getShares("Bearer ${UserPortal.loggedInUser?.AccessToken}")

        var recyclerV = view.findViewById<RecyclerView>(R.id.rvSoruCevap)

        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.container)
        val navi = activity.findViewById<BottomNavigationView>(R.id.navigation)



        result?.enqueue(object:Callback<List<Shares>>{
            override fun onFailure(call: Call<List<Shares>>?, t: Throwable?) {
                pbSoruCevap.visibility = View.INVISIBLE
            }
            override fun onResponse(call: Call<List<Shares>>?, response: Response<List<Shares>>?) {
                pbSoruCevap.visibility = View.INVISIBLE
                if(response?.message()?.toString() == "OK")
                {
                    val body = response.body()

                    val myRecyclerView = SoruCevapAdapter(body!!)

                    recyclerV.adapter = myRecyclerView
                    val myManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                    myManager.setReverseLayout(true);
                    myManager.setStackFromEnd(true);
                    recyclerV.layoutManager = myManager


                }else
                {
                    Toast.makeText(activity,"Bir şeyler ters gitti",Toast.LENGTH_LONG)
                            .show()
                }}

        })

        return view
    }

}// Required empty public constructor
