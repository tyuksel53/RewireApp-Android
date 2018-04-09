package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.taha.sigraylamcadele.Model.Shares
import kotlinx.android.synthetic.main.activity_soru_cevap_detay.*
import kotlinx.android.synthetic.main.card_view_share.*

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

        var inComingShare = intent.getSerializableExtra("currentShare") as Shares
        tvShareDateyYorumCount.text = inComingShare.YorumCount.toString()
        if(inComingShare.PublishedTime == "şimdi")
        {
            tvShareDetayUsername.text = "${inComingShare.UserID}, ${inComingShare.PublishedTime} yazdı"
        }else
        {
            tvShareDetayUsername.text = "${inComingShare.UserID}, ${inComingShare.PublishedTime} önce yazdı"
        }
        tvShareDetayHeader.text = inComingShare.Header
        tvShareDetayMessage.text = inComingShare.Message
        tvShareDetayUpVoteCount.text = inComingShare.UpVoteCount.toString()


    }
}
