package com.rewire.mobile.app

import android.content.Context
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.rewire.mobile.app.API.ApiClient
import com.rewire.mobile.app.API.ApiInterface
import com.rewire.mobile.app.Adapter.CommentAdapter
import com.rewire.mobile.app.Library.UserPortal
import com.rewire.mobile.app.Model.Comment
import com.rewire.mobile.app.Model.Shares
import com.rewire.mobile.app.PaperHelper.LocaleHelper
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_soru_cevap_detay.*
import kotlinx.android.synthetic.main.register_toolbar.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PostsActivity : AppCompatActivity() {

    var adapter:CommentAdapter? = null
    var isUserCanClick = true
    lateinit var myResources: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soru_cevap_detay)
        Paper.init(this)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }

        updateView(Paper.book().read<String>("language"))
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
                if(edShareComment.text.isNullOrEmpty() || edShareComment.text.isNullOrBlank() )
                {
                    edShareComment.setError(myResources.getString(R.string.hataYorumBos))
                    isUserCanClick = true
                    return@setOnClickListener
                }

                if(edShareComment.text.length < 3)
                {
                    edShareComment.setError(myResources.getString(R.string.hataYorumMinLength))
                    isUserCanClick = true
                    return@setOnClickListener
                }

                if(edShareComment.text.length > 400)
                {
                    edShareComment.setError(myResources.getString(R.string.hataYorumUzunlukMax))
                    isUserCanClick = true
                    return@setOnClickListener
                }

                pbShareDetay.visibility = View.VISIBLE
                var comment = Comment()
                comment.Username = UserPortal.loggedInUser!!.Username
                comment.ShareId = inComingShare.ID
                comment.Message = edShareComment.text.toString()
                val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
                val result = apiInterface?.postComment(
                        "Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        comment)

                result?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        pbShareDetay.visibility = View.INVISIBLE
                        isUserCanClick = true
                        Toasty.error(this@PostsActivity,
                                myResources.getString(R.string.hataBaglantiBozuk),
                                Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        pbShareDetay.visibility = View.INVISIBLE
                        isUserCanClick = true
                        if(response?.code() == 200)
                        {
                            UserPortal.shares?.get(position)?.YorumCount =
                                    UserPortal.shares?.get(position)?.YorumCount!! + 1

                            UserPortal.hasSharesChanged = true
                            comment.Date = myResources.getString(R.string.simdi)
                            rvShareDetayComments.scrollToPosition(1)
                            adapter!!.add(comment)
                            adapter!!.commentCountChangend()
                            edShareComment.setText("")
                        }else
                        {
                            Toasty.error(this@PostsActivity,
                                    myResources.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

                })
            }


        }



        val apiInterFace = ApiClient.client?.create(ApiInterface::class.java)

        val getComments = apiInterFace?.getComments(inComingShare.ID!!,
                "Bearer "+UserPortal.loggedInUser?.AccessToken!!)

        getComments?.enqueue(object : Callback<ArrayList<Comment>> {
            override fun onFailure(call: Call<ArrayList<Comment>>?, t: Throwable?) {
                pbShareDetay.visibility = View.INVISIBLE
                Toasty.error(this@PostsActivity,
                        myResources.getString(R.string.hataBaglantiBozuk),
                        Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<ArrayList<Comment>>?, response: Response<ArrayList<Comment>>?) {
                pbShareDetay.visibility = View.INVISIBLE
                if(response?.code() == 200)
                {
                    val body = response?.body()
                    adapter = CommentAdapter(body!!,inComingShare,this@PostsActivity)
                    rvShareDetayComments.adapter = adapter
                    val myManager = LinearLayoutManager(this@PostsActivity,LinearLayoutManager.VERTICAL,false)
                    rvShareDetayComments!!.layoutManager = myManager
                }else
                {
                    Toasty.error(this@PostsActivity,
                            myResources.getString(R.string.hataBirSeylerTers),
                            Toast.LENGTH_SHORT).show()
                }
            }

        })


    }

    private fun updateView(lang: String?) {
        val context = LocaleHelper.setLocale(this@PostsActivity,lang)
        myResources = context.resources

        edShareComment.setHint(myResources.getString(R.string.CevapYaz))
    }

    override fun onResume() {
        updateView(Paper.book().read<String>("language"))
        super.onResume()
    }
}
