package com.rewire.mobile.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.rewire.mobile.app.API.ApiClient
import com.rewire.mobile.app.API.ApiInterface
import com.rewire.mobile.app.Library.UserPortal
import com.rewire.mobile.app.Model.Shares
import com.rewire.mobile.app.PaperHelper.LocaleHelper
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_insert_share.*
import kotlinx.android.synthetic.main.insert_share_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertUpdateShareActivity : AppCompatActivity() {
    var isUserCanClick = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_share)


        val inflator = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.insert_share_toolbar,null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.customView = view
        pbInsertShare.visibility = View.INVISIBLE
        val type = intent.getIntExtra("type",0)
        var updateShare = Shares()
        if(intent.getSerializableExtra("currentShare") is Shares)
        {
            updateShare =  intent.getSerializableExtra("currentShare") as Shares
            edShareHeader.setText(updateShare.Header.toString())
            edShareDescription.setText(updateShare.Message.toString())
        }

        updateView(Paper.book().read<String>("language"),type)
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)



        ivInsertShareCancel.setOnClickListener {
            this@InsertUpdateShareActivity.finish()
        }

        ivInsertShareOK.setOnClickListener {
            if(isUserCanClick)
            {
                isUserCanClick = false
                var control = true
                if(edShareHeader.text.toString().isNullOrBlank() || edShareHeader.text.toString().isNullOrEmpty())
                {
                    control = false
                    edShareHeader.error = UserPortal.myLangResource!!.getString(R.string.hataShareBaslik)
                }else if( edShareHeader.text.toString().length < 3)
                {
                    control = false
                    edShareHeader.error = UserPortal.myLangResource!!.getString(R.string.hataShareBaslikUzunlukMin)
                }else if(edShareHeader.text.toString().length > 200)
                {
                    control = false
                    edShareHeader.error = UserPortal.myLangResource!!.getString(R.string.hataShareBaslikUzunlukMax)
                }

                if(edShareDescription.text.toString().isNullOrBlank() || edShareDescription.text.toString().isNullOrEmpty())
                {
                    control = false
                    edShareDescription.error = UserPortal.myLangResource!!.getString(R.string.hataShareAciklama)
                }else if( edShareDescription.text.toString().length < 3)
                {
                    control = false
                    edShareDescription.error = UserPortal.myLangResource!!.getString(R.string.hataShareAciklamaUzunlukMin)
                }else if(edShareDescription.text.toString().length > 40000)
                {
                    control = false
                    edShareDescription.error = UserPortal.myLangResource!!.getString(R.string.hataShareAciklamaUzunlukMax)
                }

                if(control)
                {   pbInsertShare.visibility = View.VISIBLE
                    if(type==1)
                    {
                        updateShare.Message = edShareDescription.text.toString()
                        updateShare.Header = edShareHeader.text.toString()
                        val update = apiInterface?.updateShare("Bearer "+UserPortal?.loggedInUser?.AccessToken!!,
                               updateShare =  updateShare)
                        update?.clone()?.enqueue(object:Callback<String>{
                            override fun onFailure(call: Call<String>?, t: Throwable?) {
                                isUserCanClick = true
                                pbInsertShare.visibility = View.INVISIBLE
                                Toasty.error(this@InsertUpdateShareActivity,
                                        UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                                        ,Toast.LENGTH_LONG,true)
                                        .show()
                            }

                            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                                isUserCanClick = true
                                pbInsertShare.visibility = View.INVISIBLE
                                if(response?.code() == 200)
                                {
                                    Toasty.success(this@InsertUpdateShareActivity,
                                            UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                            Toast.LENGTH_LONG,true).show()
                                    UserPortal.newShare = true
                                    finish()
                                }else
                                {
                                    Toasty.error(this@InsertUpdateShareActivity,
                                            UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                            ,Toast.LENGTH_LONG,true)
                                            .show()
                                }
                            }
                        })
                    }else
                    {
                        val share = Shares()
                        share.Header = edShareHeader.text.toString()
                        share.Message = edShareDescription.text.toString()
                        val postShare = apiInterface?.postShare(
                                "Bearer "+UserPortal?.loggedInUser?.AccessToken!!,share)

                        postShare?.clone()?.enqueue(object: Callback<String> {
                            override fun onFailure(call: Call<String>?, t: Throwable?) {
                                isUserCanClick = true
                                pbInsertShare.visibility = View.INVISIBLE
                                Toasty.error(this@InsertUpdateShareActivity,
                                        UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                                        ,Toast.LENGTH_LONG,true)
                                        .show()
                            }

                            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                                isUserCanClick = true
                                pbInsertShare.visibility = View.INVISIBLE
                                if(response?.code() == 200)
                                {
                                    Toasty.success(this@InsertUpdateShareActivity,
                                            UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                            Toast.LENGTH_LONG,true).show()
                                    UserPortal.newShare = true
                                    finish()
                                }else
                                {
                                    Toasty.error(this@InsertUpdateShareActivity,
                                            UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers)
                                            ,Toast.LENGTH_LONG,true)
                                            .show()
                                }
                            }

                        })
                    }
                }else
                {
                    isUserCanClick = true
                }
            }




        }
    }

    private fun updateView(lang: String,type:Int) {
        val context = LocaleHelper.setLocale(this@InsertUpdateShareActivity,lang)
        UserPortal.myLangResource = context.resources

        edShareHeader.setHint(UserPortal.myLangResource!!.getString(R.string.baslik_girin))
        edShareDescription.setHint(UserPortal.myLangResource!!.getString(R.string.aciklama_girin))
        val text = findViewById<TextView>(R.id.tvShareSometing)
        if(type == 1)
        {
            text.setText(UserPortal.myLangResource!!.getString(R.string.paylasimi_güncelle))
        }else
        {
            text.setText(UserPortal.myLangResource!!.getString(R.string.bir_seyler_paylas))
        }

    }
}
