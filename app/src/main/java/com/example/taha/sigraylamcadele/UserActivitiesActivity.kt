package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.SoruCevapAdapter
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_kulanici_aktivite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivitiesActivity : AppCompatActivity() {

    var type:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kulanici_aktivite)
        type = intent.getIntExtra("type",1)
        tvKullaniciNoData.setText(UserPortal.myLangResource!!.getString(R.string.veri_yok))
        val apiInterFace = ApiClient.client?.create(ApiInterface::class.java)
        if(type==1)
        {
            val inflator = this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = inflator.inflate(R.layout.your_posts, null)
            val header = v.findViewById<TextView>(R.id.tvYourPostsHeader)
            header.setText(UserPortal.myLangResource!!.getString(R.string.paylasimlarin))
            val exit = v.findViewById<ImageView>(R.id.ivYourPostCancel)
            exit.setOnClickListener {
                this@UserActivitiesActivity.finish()
            }
            val actionBar = supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowHomeEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.customView = v

            initRecylcer()

        }else
        {
            val inflator = this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = inflator.inflate(R.layout.your_likes, null)
            val header = v.findViewById<TextView>(R.id.tvYourLikesHeader)
            header.setText(UserPortal.myLangResource!!.getString(R.string.begenilerin))
            val exit = v.findViewById<ImageView>(R.id.ivYourLikesCancel)
            exit.setOnClickListener {
                this@UserActivitiesActivity.finish()
            }

            val actionBar = supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowHomeEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.customView = v


            val result = apiInterFace?.getUserLikes("Bearer ${UserPortal.loggedInUser!!.AccessToken}")
            result?.clone()?.enqueue(object: Callback<ArrayList<Shares>> {
                override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                    pbKullaniciAktivite.visibility = View.INVISIBLE
                    Toasty.error(this@UserActivitiesActivity,
                            UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                            Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
                    pbKullaniciAktivite.visibility = View.INVISIBLE
                    if(response?.code() == 200)
                    {
                        if(response.body()!!.size == 0)
                        {
                            ivKullaniciNoData.visibility = View.VISIBLE
                            tvKullaniciNoData.visibility = View.VISIBLE
                        }else
                        {
                            val adapter = SoruCevapAdapter(response.body()!!,this@UserActivitiesActivity)
                            rvKullaniciAktivite.adapter = adapter
                            val myManager = LinearLayoutManager(this@UserActivitiesActivity, LinearLayoutManager.VERTICAL,false)
                            rvKullaniciAktivite!!.layoutManager = myManager
                        }

                    }else
                    {
                        Toasty.error(this@UserActivitiesActivity,
                                UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                Toast.LENGTH_LONG).show()
                    }
                }

            })

        }

    }

    override fun onResume() {
        if(UserPortal.newShare)
        {
            initRecylcer()
        }
        super.onResume()
    }

    fun initRecylcer()
    {
        pbKullaniciAktivite.visibility = View.VISIBLE
        val apiInterFace = ApiClient.client?.create(ApiInterface::class.java)
        val result = apiInterFace?.getUserPosts("Bearer ${UserPortal.loggedInUser!!.AccessToken}")
        result?.clone()?.enqueue(object: Callback<ArrayList<Shares>> {
            override fun onFailure(call: Call<ArrayList<Shares>>?, t: Throwable?) {
                pbKullaniciAktivite.visibility = View.INVISIBLE
                Toasty.error(this@UserActivitiesActivity,
                        UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                        Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<Shares>>?, response: Response<ArrayList<Shares>>?) {
                pbKullaniciAktivite.visibility = View.INVISIBLE
                if(response?.code() == 200)
                {
                    if(response.body()!!.size == 0)
                    {
                        ivKullaniciNoData.visibility = View.VISIBLE
                        tvKullaniciNoData.visibility = View.VISIBLE
                    }else
                    {
                        val adapter = SoruCevapAdapter(response.body()!!,this@UserActivitiesActivity)
                        rvKullaniciAktivite.adapter = adapter
                        val myManager = LinearLayoutManager(this@UserActivitiesActivity, LinearLayoutManager.VERTICAL,false)
                        rvKullaniciAktivite!!.layoutManager = myManager
                    }
                }else
                {
                    Toasty.error(this@UserActivitiesActivity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                            Toast.LENGTH_LONG).show()
                }
            }

        })

    }
}
