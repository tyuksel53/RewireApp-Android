package com.example.taha.sigraylamcadele.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.card_view_share.view.*

/**
 * Created by Taha on 26-Mar-18.
 */
class SoruCevapAdapter(var dataSource:List<Shares>): RecyclerView.Adapter<SoruCevapAdapter.SoruCevapViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoruCevapViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val myRow = inflater.inflate(R.layout.card_view_share,parent,false)

        return SoruCevapViewHolder(myRow)
    }

    override fun onBindViewHolder(holder: SoruCevapViewHolder, position: Int) {

        var currentShare = dataSource[position]

        holder?.setData(currentShare)
    }


    override fun getItemCount(): Int {

        return dataSource.size
    }


    inner class SoruCevapViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView) {

        var cardInfo = itemView as CardView

        var message = cardInfo.tvShareMessage
        var userId = cardInfo.tvShareUserID
        var upVoteCount = cardInfo.tvUpVoteCount
        var date = cardInfo.tvShareDate
        var header = cardInfo.tvShareHeader
        var yorumCount = cardInfo.tvYorumCount
        var profile = cardInfo.ivUserProfile

        @SuppressLint("ResourceAsColor")
        fun setData(share:Shares)
        {
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

            upVoteCount.text = share.UpVoteCount.toString()
            date.text = share.PublishedTime
            yorumCount.text = share.YorumCount.toString()


        }

    }
}