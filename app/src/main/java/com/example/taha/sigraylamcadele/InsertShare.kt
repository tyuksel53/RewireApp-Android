package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_insert_share.*
import kotlinx.android.synthetic.main.insert_share_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertShare : AppCompatActivity() {

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

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)



        ivInsertShareCancel.setOnClickListener {
            this@InsertShare.finish()
        }

        ivInsertShareOK.setOnClickListener {

            var control = true
            if(edShareHeader.text.toString().isNullOrBlank() || edShareHeader.text.toString().isNullOrEmpty())
            {
                control = false
                edShareHeader.error = "Başlık boş olamaz"
            }else if( edShareHeader.text.toString().length < 3)
            {
                control = false
                edShareHeader.error = "Başlık 3 karakterden küçük olmamalıdır"
            }else if(edShareHeader.text.toString().length > 200)
            {
                control = false
                edShareHeader.error = "Başlık 200 karakterden büyük olmamalıdır"
            }

            if(edShareDescription.text.toString().isNullOrBlank() || edShareDescription.text.toString().isNullOrEmpty())
            {
                control = false
                edShareDescription.error = "Açıklama boş olamaz"
            }else if( edShareDescription.text.toString().length < 3)
            {
                control = false
                edShareDescription.error = "Açıklama 3 karakterden küçük olmamalıdır"
            }else if(edShareDescription.text.toString().length > 40000)
            {
                control = false
                edShareDescription.error = "Açıklama 40000 karakterden büyük olmamalıdır"
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
                        pbInsertShare.visibility = View.INVISIBLE
                        Toasty.error(this@InsertShare,"Bir şeyler ters gitti." +
                                " İnternet bağlantınızı kontrol edin.",Toast.LENGTH_LONG,true)
                                .show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        pbInsertShare.visibility = View.INVISIBLE
                        if(response?.code() == 200)
                        {
                            Toasty.success(this@InsertShare,"İşlem başarılı",
                                    Toast.LENGTH_LONG,true).show()
                            UserPortal.newShare = true
                            finish()
                        }else
                        {
                            Toasty.error(this@InsertShare,"Bir şeyler ters gitti."
                                    ,Toast.LENGTH_LONG,true)
                                    .show()
                        }
                    }

                })
            }



        }
    }
}
