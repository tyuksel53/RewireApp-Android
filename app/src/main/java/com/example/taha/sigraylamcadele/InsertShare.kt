package com.example.taha.sigraylamcadele

import android.content.Context
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_insert_share.*
import kotlinx.android.synthetic.main.insert_share_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertShare : AppCompatActivity() {
lateinit var myResources: Resources
    var isUserCanClick = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_share)

        Paper.init(this)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }



        val inflator = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.insert_share_toolbar,null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.customView = view
        pbInsertShare.visibility = View.INVISIBLE
        updateView(Paper.book().read("language"))
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)



        ivInsertShareCancel.setOnClickListener {
            this@InsertShare.finish()
        }

        ivInsertShareOK.setOnClickListener {
            if(isUserCanClick)
            {
                isUserCanClick = false
                var control = true
                if(edShareHeader.text.toString().isNullOrBlank() || edShareHeader.text.toString().isNullOrEmpty())
                {
                    control = false
                    edShareHeader.error = myResources.getString(R.string.hataShareBaslik)
                }else if( edShareHeader.text.toString().length < 3)
                {
                    control = false
                    edShareHeader.error = myResources.getString(R.string.hataShareBaslikUzunlukMin)
                }else if(edShareHeader.text.toString().length > 200)
                {
                    control = false
                    edShareHeader.error = myResources.getString(R.string.hataShareBaslikUzunlukMax)
                }

                if(edShareDescription.text.toString().isNullOrBlank() || edShareDescription.text.toString().isNullOrEmpty())
                {
                    control = false
                    edShareDescription.error = myResources.getString(R.string.hataShareAciklama)
                }else if( edShareDescription.text.toString().length < 3)
                {
                    control = false
                    edShareDescription.error = myResources.getString(R.string.hataShareAciklamaUzunlukMin)
                }else if(edShareDescription.text.toString().length > 40000)
                {
                    control = false
                    edShareDescription.error = myResources.getString(R.string.hataShareAciklamaUzunlukMax)
                }

                if(control)
                {
                    pbInsertShare.visibility = View.VISIBLE
                    val share = Shares()
                    share.Header = edShareHeader.text.toString()
                    share.Message = edShareDescription.text.toString()
                    val postShare = apiInterface?.postShare("Bearer "+UserPortal?.loggedInUser?.AccessToken!!,share)

                    postShare?.enqueue(object: Callback<String> {
                        override fun onFailure(call: Call<String>?, t: Throwable?) {
                            isUserCanClick = true
                            pbInsertShare.visibility = View.INVISIBLE
                            Toasty.error(this@InsertShare,
                                    myResources.getString(R.string.hataBaglantiBozuk)
                                    ,Toast.LENGTH_LONG,true)
                                    .show()
                        }

                        override fun onResponse(call: Call<String>?, response: Response<String>?) {
                            isUserCanClick = true
                            pbInsertShare.visibility = View.INVISIBLE
                            if(response?.code() == 200)
                            {
                                Toasty.success(this@InsertShare,
                                        myResources.getString(R.string.islem_basarili),
                                        Toast.LENGTH_LONG,true).show()
                                UserPortal.newShare = true
                                finish()
                            }else
                            {
                                Toasty.error(this@InsertShare,
                                        myResources.getString(R.string.hataBirSeylerTers)
                                        ,Toast.LENGTH_LONG,true)
                                        .show()
                            }
                        }

                    })
                }else
                {
                    isUserCanClick = true
                }
            }




        }
    }

    private fun updateView(lang: String) {
        val context = LocaleHelper.setLocale(this@InsertShare,lang)
        myResources = context.resources

        edShareHeader.setHint(myResources.getString(R.string.baslik_girin))
        edShareDescription.setHint(myResources.getString(R.string.aciklama_girin))
        var text = findViewById<TextView>(R.id.tvShareSometing)
        text.setText(myResources.getString(R.string.bir_seyler_paylas))

    }
}
