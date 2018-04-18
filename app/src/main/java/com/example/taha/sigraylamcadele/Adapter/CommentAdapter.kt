package com.example.taha.sigraylamcadele.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Comment
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import kotlinx.android.synthetic.main.share_detay_comment.view.*
import kotlinx.android.synthetic.main.share_detay_comment_header.view.*

class CommentAdapter(var allComments:ArrayList<Comment>,var headerShare:Shares,var context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    fun add(comment:Comment)
    {
        allComments.add(0,comment)
        notifyItemInserted(1)
        notifyItemRangeChanged(0,allComments.size+1)
    }

    fun commentCountChangend()
    {
        headerShare.YorumCount = headerShare.YorumCount!! + 1
        notifyItemChanged(0)
    }
    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
        {
            1
        }else
            0
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
        var likeImg = cardView.ivShareDetayLike

        fun setData(inComingShare: Shares)
        {
            val like = UserPortal.getLikes()?.find { x->x.ShareId == inComingShare.ID }
            if(like != null)
            {
                likeImg.setColorFilter(ContextCompat.getColor(context, R.color.myRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
            yourmCount.text = inComingShare.YorumCount.toString()
            username.text = "${inComingShare.UserID}, ${inComingShare.PublishedTime}"
            header.text = inComingShare.Header
            message.text = inComingShare.Message
            upvoteCount.text = inComingShare.UpVoteCount.toString()
        }
    }
}