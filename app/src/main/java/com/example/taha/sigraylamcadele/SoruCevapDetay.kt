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

    var adapter:CommentAdapter? = null
    var isUserCanClick = true
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

        val inComingShare = intent.getSerializableExtra("currentShare") as Shares
        val position = intent.getIntExtra("position",0)
        ivRegisterCancel.setOnClickListener {
            finish()
        }

        ivShareDetayCommentSend.setOnClickListener {
            if(isUserCanClick)
            {
                isUserCanClick = false
                if(edShareDetayComment.text.isNullOrEmpty() || edShareDetayComment.text.isNullOrBlank() )
                {
                    edShareDetayComment.setError("Mesaj boş olamaz")
                    isUserCanClick = true
                    return@setOnClickListener
                }

                if(edShareDetayComment.text.length < 3)
                {
                    edShareDetayComment.setError("Mesajınızın uzunluğu minimum 3 karakter olmalıdır !")
                    isUserCanClick = true
                    return@setOnClickListener
                }
                pbShareDetay.visibility = View.VISIBLE
                var comment = Comment()
                comment.Username = UserPortal.loggedInUser!!.Username
                comment.ShareId = inComingShare.ID
                comment.Message = edShareDetayComment.text.toString()
                val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
                val result = apiInterface?.postComment(
                        "Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        comment)

                result?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        pbShareDetay.visibility = View.INVISIBLE
                        isUserCanClick = true
                        Toasty.error(this@SoruCevapDetay,"İnternet bağlantınızı kontrol edin",
                                Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        pbShareDetay.visibility = View.INVISIBLE
                        isUserCanClick = true
                        if(response?.code() == 200)
                        {
                            UserPortal.shares!![position].YorumCount =
                                    UserPortal.shares!![position].YorumCount!! + 1

                            UserPortal.hasSharesChanged = true
                            comment.Date = "şimdi"
                            rvShareDetayComments.scrollToPosition(1)
                            adapter!!.add(comment)
                            adapter!!.commentCountChangend()
                            edShareDetayComment.setText("")
                        }
                    }

                })
            }


        }



        val apiInterFace = ApiClient.client?.create(ApiInterface::class.java)

        val getComments = apiInterFace?.getComments(inComingShare.ID!!,
                "Bearer "+UserPortal.loggedInUser?.AccessToken!!)

        val enqueue = getComments?.enqueue(object : Callback<ArrayList<Comment>> {
            override fun onFailure(call: Call<ArrayList<Comment>>?, t: Throwable?) {
                pbShareDetay.visibility = View.INVISIBLE
                Toasty.error(this@SoruCevapDetay,"İnternet Bağlantınızı Kontrol Edin",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ArrayList<Comment>>?, response: Response<ArrayList<Comment>>?) {
                pbShareDetay.visibility = View.INVISIBLE
                if(response?.code() == 200)
                {
                    var body = response?.body()
                    adapter = CommentAdapter(body!!,inComingShare)
                    rvShareDetayComments.adapter = adapter
                    val myManager = LinearLayoutManager(this@SoruCevapDetay,LinearLayoutManager.VERTICAL,false)
                    rvShareDetayComments!!.layoutManager = myManager
                }
            }

        })


    }
}
