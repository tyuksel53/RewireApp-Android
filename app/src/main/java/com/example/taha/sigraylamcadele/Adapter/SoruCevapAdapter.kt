package com.example.taha.sigraylamcadele.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import com.example.taha.sigraylamcadele.SoruCevapDetay
import kotlinx.android.synthetic.main.card_view_share.view.*

/**
 * Created by Taha on 26-Mar-18.
 */
class SoruCevapAdapter(var dataSource:List<Shares>,var context:Context): RecyclerView.Adapter<SoruCevapAdapter.SoruCevapViewHolder>() {
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
        var profile = cardInfo.ivUserProfile
        var spinner = cardInfo.shareSpinner

        @SuppressLint("ResourceAsColor")
        fun setData(share:Shares,position: Int)
        {

            val adapter = ArrayAdapter(context,
                    android.R.layout.simple_dropdown_item_1line, arrayOf("Report"))
            spinner.adapter = adapter

            cardInfo.setOnClickListener {

                var intent = Intent(context,SoruCevapDetay::class.java)
                intent.putExtra("currentShare",share)
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