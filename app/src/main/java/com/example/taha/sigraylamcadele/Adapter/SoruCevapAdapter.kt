package com.example.taha.sigraylamcadele.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.ShareLike
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import com.example.taha.sigraylamcadele.SoruCevapDetay
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.card_view_share.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast


class SoruCevapAdapter(var dataSource:ArrayList<Shares>,var context:Context): RecyclerView.Adapter<SoruCevapAdapter.SoruCevapViewHolder>() {

    var apiInterface:ApiInterface? = null
    var isUserCanClick = true
    init {
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoruCevapViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val myRow = inflater.inflate(R.layout.card_view_share,parent,false)

        return SoruCevapViewHolder(myRow)
    }

    override fun onBindViewHolder(holder: SoruCevapViewHolder, position: Int) {

        var currentShare = dataSource[position]

        holder?.setData(currentShare,position)
    }


    override fun getItemCount(): Int {

        return dataSource.size
    }



    inner class SoruCevapViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView) {

        var cardInfo = itemView as CardView

        var message = cardInfo.tvShareMessage
        var userId = cardInfo.tvShareUserID
        var LikeCount = cardInfo.tvShareLikeCount
        var date = cardInfo.tvShareDate
        var header = cardInfo.tvShareHeader
        var yorumCount = cardInfo.tvYorumCount
        var viewLike = cardInfo.viewLike
        var spinner = cardInfo.shareSpinner
        var likeHearth = cardInfo.ivShareLike
        var check=0
        @SuppressLint("ResourceAsColor")
        fun setData(share:Shares,position: Int)
        {

        var adp= ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1,
                arrayOf(UserPortal.myLangResource!!.getString(R.string.Raporla)))
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adp)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(++check > 1)
                {
                    Toasty.success(context,
                        UserPortal.myLangResource!!.getString(R.string.Rapor_Iletildi),
                        Toast.LENGTH_SHORT).show()

                    val result = apiInterface?.shareReport(
                        "Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        share.ID.toString())

                    result?.clone()?.enqueue(object:Callback<String>{
                        override fun onFailure(call: Call<String>?, t: Throwable?) {

                         }

                        override fun onResponse(call: Call<String>?, response: Response<String>?) {

                        }

                     })
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                    // your code here
            }

        }

            val check = UserPortal.getLikes()?.find { x-> x.ShareId == share.ID }
            if(check != null)
            {
                likeHearth.setColorFilter(ContextCompat.getColor(context, R.color.myRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }

            viewLike.setOnClickListener {
                if(UserPortal.getLikes() != null)
                {
                    if(isUserCanClick)
                    {
                        isUserCanClick = false
                        var like = ShareLike()
                        like.ShareId = share.ID
                        val result = apiInterface?.userLiked(
                                "Bearer ${UserPortal.loggedInUser?.AccessToken}",like)


                        result?.clone()?.enqueue(object:Callback<String>{
                            override fun onFailure(call: Call<String>?, t: Throwable?) {
                                Log.e("Like","Başarısız")
                                isUserCanClick = true
                            }

                            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                                isUserCanClick = true
                                if(response?.code() == 200)
                                {
                                    var newLike = ShareLike()
                                    newLike.ShareId = share.ID
                                    newLike.UserID = UserPortal.loggedInUser!!.Username
                                    UserPortal.insertLikes(newLike)

                                    likeHearth.setColorFilter(ContextCompat.getColor(context, R.color.myRed),
                                            android.graphics.PorterDuff.Mode.SRC_IN)

                                    var likeCounts = Integer.parseInt(LikeCount.text.toString())
                                    likeCounts++
                                    UserPortal.shares!![position].UpVoteCount = likeCounts
                                    share.UpVoteCount = likeCounts
                                    LikeCount.text = "$likeCounts"
                                }

                                if(response?.code() == 202)
                                {
                                    val check202 = UserPortal.getLikes()?.find { x-> x.ShareId == share.ID }
                                    likeHearth.setColorFilter(ContextCompat.getColor(context, R.color.textColorPrimary),
                                            android.graphics.PorterDuff.Mode.SRC_IN)
                                    var likeCounts = Integer.parseInt(LikeCount.text.toString())
                                    likeCounts--
                                    LikeCount.text = "$likeCounts"
                                    UserPortal.shares!![position].UpVoteCount = likeCounts
                                    share.UpVoteCount = likeCounts
                                    UserPortal.removeLikes(check202!!)
                                }
                            }

                        })
                    }



                }
            }

            cardInfo.setOnClickListener {
                var intent = Intent(context,SoruCevapDetay::class.java)
                intent.putExtra("currentShare",UserPortal.shares!![position])
                intent.putExtra("position",position)
                it.context.startActivity(intent)
            }

            if(share.Message!!.length > 200)
            {
                message.text = share.Message!!.substring(0..200) + "..."
            }else
            {
                message.text = share.Message
            }
            header.text = share.Header

            if(share.UserID == UserPortal.loggedInUser?.Username)
            {
                userId.text = share.UserID
            }else
            {
                userId.text = share.UserID
            }

            LikeCount.text = share.UpVoteCount.toString()
            date.text = share.PublishedTime
            yorumCount.text = share.YorumCount.toString()


        }

    }
}