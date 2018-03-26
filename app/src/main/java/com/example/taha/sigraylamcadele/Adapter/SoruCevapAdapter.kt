package com.example.taha.sigraylamcadele.Adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.card_view_share.view.*

/**
 * Created by Taha on 26-Mar-18.
 */
class SoruCevapAdapter(var dataSource:ArrayList<Shares>): RecyclerView.Adapter<SoruCevapAdapter.SoruCevapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SoruCevapViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val myRow = inflater.inflate(R.layout.card_view_share,parent,false)

        return SoruCevapViewHolder(myRow)
    }

    override fun getItemCount(): Int {

        return dataSource.size
    }

    override fun onBindViewHolder(holder: SoruCevapViewHolder?, position: Int) {

        var currentShare = dataSource[position]

        holder?.setData(currentShare)
    }


    inner class SoruCevapViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView) {

        var cardInfo = itemView as CardView

        var message = cardInfo.tvShareMessage
        var userId = cardInfo.tvShareUserID
        var upVoteCount = cardInfo.tvUpVoteCount
        var date = cardInfo.tvShareDate

        fun setData(share:Shares)
        {
            message.text = share.message
            userId.text = share.userId
            upVoteCount.text = share.upVoteCount.toString()
            date.text = share.publishedTime

        }

    }
}