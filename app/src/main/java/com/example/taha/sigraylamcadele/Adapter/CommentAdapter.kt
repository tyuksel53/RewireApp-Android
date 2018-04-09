package com.example.taha.sigraylamcadele.Adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Model.Comment
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.share_detay_comment.view.*
import kotlinx.android.synthetic.main.share_detay_comment_header.view.*

class CommentAdapter(var allComments:List<Comment>,var headerShare:Shares): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CommentAdapter.CommentViewHolder)
        {
            val currentComment = allComments[position-1]
            holder?.setData(currentComment,position)
        }else if(holder is CommentHeaderViewHolder)
        {
            holder?.setData(headerShare)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent?.context)
        if(viewType == 0)
        {
            val myRow = inflater.inflate(R.layout.share_detay_comment,parent,false)
            return CommentViewHolder(myRow)

        }else
        {
            val myRow = inflater.inflate(R.layout.share_detay_comment_header,parent,false)
            return CommentHeaderViewHolder(myRow)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0)
        {
            return 1
        }else
            return 0
    }

    override fun getItemCount(): Int {
        return allComments.size+1
    }



    inner class CommentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var message = itemView.tvCommentMessage
        var username = itemView.tvCommentUsername
        var date = itemView.tvCommentDate


        fun setData(currentComment:  Comment,position: Int) {

            message.text = currentComment.Message
            username.text = currentComment.Username
            date.text = currentComment.Date

        }

    }

    inner class CommentHeaderViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        var cardView = itemView as CardView
        var yourmCount = cardView.tvShareDateyYorumCount
        var username = cardView.tvShareDetayUsername
        var header = cardView.tvShareDetayHeader
        var message = cardView.tvShareDetayMessage
        var upvoteCount = cardView.tvShareDetayUpVoteCount

        fun setData(inComingShare: Shares)
        {
            yourmCount.text = inComingShare.YorumCount.toString()
            if(inComingShare.PublishedTime == "şimdi")
            {
                username.text = "${inComingShare.UserID}, ${inComingShare.PublishedTime} yazdı"
            }else
            {
                username.text = "${inComingShare.UserID}, ${inComingShare.PublishedTime} önce yazdı"
            }
            header.text = inComingShare.Header
            message.text = inComingShare.Message
            upvoteCount.text = inComingShare.UpVoteCount.toString()
        }
    }
}