package com.example.taha.sigraylamcadele.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.card_leaderboard_user.view.*

class LeaderBoardAdapter(var data:ArrayList<User>,var context:Context):RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {

        val inflater = LayoutInflater.from(parent?.context)
        val myRow = inflater.inflate(R.layout.card_leaderboard_user,parent,false)
        if(viewType == 2)
        {
            myRow.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_shadow_end_color))
        }
        return LeaderBoardViewHolder(myRow)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 2 == 0) 1 else 2
    }
    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {

        val currentUser = data[position]

        holder?.setData(currentUser,position)
    }


    inner class LeaderBoardViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView)
    {
        val cardView = itemView as CardView
        var rank = cardView.tvRankNumber
        var username = cardView.tvRankUsername
        var clearday = cardView.tvRankClearDay

        fun setData(currentUser: User, position: Int) {
            val userRank = position + 1
            rank.text = userRank.toString()
            username.text = currentUser.Username
            clearday.text = currentUser.ClearDayCount.toString()
        }

    }
}