package com.example.taha.sigraylamcadele

import android.content.Context
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Adapter.CommentAdapter
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Comment
import com.example.taha.sigraylamcadele.Model.Shares
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_soru_cevap_detay.*
import kotlinx.android.synthetic.main.register_toolbar.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class SoruCevapDetay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soru_cevap_detay)

        val inflator = this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflator.inflate(R.layout.register_toolbar, null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.customView = v

        ivRegisterCancel.setOnClickListener {
            finish()
        }

        ivShareDetayCommentSend.setOnClickListener {

            if(edShareDetayComment.text.isNullOrEmpty() || edShareDetayComment.text.isNullOrBlank() )
            {
                edShareDetayComment.setError("Mesaj boş olamaz")
                return@setOnClickListener
            }

            if(edShareDetayComment.text.length < 3)
            {
                edShareDetayComment.setError("Mesajınızın uzunluğu minimum 3 karakter olmalıdır !")
                return@setOnClickListener
            }

            

        }

        val inComingShare = intent.getSerializableExtra("currentShare") as Shares


        val apiInterFace = ApiClient.client?.create(ApiInterface::class.java)

        val getComments = apiInterFace?.getComments(inComingShare.ID!!,
                "Bearer "+UserPortal.loggedInUser?.AccessToken!!)

        val enqueue = getComments?.enqueue(object : Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>?, t: Throwable?) {
                pbShareDetay.visibility = View.INVISIBLE
                Toasty.error(this@SoruCevapDetay,"İnternet Bağlantınızı Kontrol Edin",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Comment>>?, response: Response<List<Comment>>?) {
                pbShareDetay.visibility = View.INVISIBLE
                if(response?.code() == 200)
                {
                    var body = response?.body()
                    var adapter = CommentAdapter(body!!,inComingShare)
                    rvShareDetayComments.adapter = adapter
                    val myManager = LinearLayoutManager(this@SoruCevapDetay,LinearLayoutManager.VERTICAL,false)
                    rvShareDetayComments!!.layoutManager = myManager
                }
            }

        })


    }
}
