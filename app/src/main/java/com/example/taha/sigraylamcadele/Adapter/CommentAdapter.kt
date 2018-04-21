package com.example.taha.sigraylamcadele.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.Comment
import com.example.taha.sigraylamcadele.Model.ShareLike
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.share_detay_comment.view.*
import kotlinx.android.synthetic.main.share_detay_comment_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(var allComments:ArrayList<Comment>,var headerShare:Shares,var context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var apiInterface:ApiInterface? = null
    init {
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CommentAdapter.CommentViewHolder)
        {
            val currentComment = allComments[position-1]
            holder?.setData(currentComment)

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
        var check = 0
        var spinner = itemView.spComment

        fun setData(currentComment:  Comment) {

            message.text = currentComment.Message
            username.text = currentComment.Username
            date.text = currentComment.Date
            var adp= ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1,
                    arrayOf(UserPortal.myLangResource!!.getString(R.string.Raporla)))
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.setAdapter(adp)

            val listener = object:AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Toasty.success(context,
                            UserPortal.myLangResource!!.getString(R.string.Rapor_Iletildi),
                            Toast.LENGTH_SHORT).show()

                    val result = apiInterface?.commentReport(
                            "Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                            currentComment.ID.toString())

                    result?.clone()?.enqueue(object:Callback<String>{
                        override fun onFailure(call: Call<String>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<String>?, response: Response<String>?) {

                        }

                    })
                }
            }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                    spinner.setOnItemSelectedListener(listener)
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // your code here
                }

            }
        }

    }

    inner class CommentHeaderViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        var cardView = itemView as CardView
        var yourmCount = cardView.tvShareDateyYorumCount
        var username = cardView.tvShareDetayUsername
        var header = cardView.tvShareDetayHeader
        var message = cardView.tvShareDetayMessage
        var LikeCount = cardView.tvShareDetayLikeCount
        var likeImg = cardView.ivShareDetayLike
        var userCanClick = true
        fun setData(inComingShare: Shares)
        {
            val like = UserPortal.getLikes()?.find { x->x.ShareId == inComingShare.ID }
            if(like != null)
            {
                likeImg.setColorFilter(ContextCompat.getColor(context, R.color.myRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }

            likeImg.setOnClickListener {
                if(UserPortal.getLikes() != null)
                {
                    if(userCanClick)
                    {
                        userCanClick = false
                        var like = ShareLike()
                        like.ShareId = inComingShare.ID
                        val result = apiInterface?.userLiked(
                                "Bearer ${UserPortal.loggedInUser?.AccessToken}",like)


                        result?.clone()?.enqueue(object: Callback<String> {
                            override fun onFailure(call: Call<String>?, t: Throwable?) {
                                Log.e("Like","Başarısız")
                                userCanClick = true
                            }

                            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                                userCanClick = true
                                if(response?.code() == 200)
                                {
                                    var newLike = ShareLike()
                                    newLike.ShareId = inComingShare.ID
                                    newLike.UserID = UserPortal.loggedInUser!!.Username
                                    UserPortal.insertLikes(newLike)

                                    likeImg.setColorFilter(ContextCompat.getColor(context, R.color.myRed),
                                            android.graphics.PorterDuff.Mode.SRC_IN)

                                    var likeCounts = Integer.parseInt(LikeCount.text.toString())
                                    likeCounts++
                                    UserPortal.shares!![position].UpVoteCount = likeCounts
                                    LikeCount.text = "$likeCounts"
                                }

                                if(response?.code() == 202)
                                {
                                    val check202 = UserPortal.getLikes()!!.find { x-> x.ShareId == inComingShare.ID }
                                    likeImg.setColorFilter(ContextCompat.getColor(context, R.color.textColorPrimary),
                                            android.graphics.PorterDuff.Mode.SRC_IN)
                                    var likeCounts = Integer.parseInt(LikeCount.text.toString())
                                    likeCounts--
                                    LikeCount.text = "$likeCounts"
                                    UserPortal.shares!![position].UpVoteCount = likeCounts
                                    UserPortal.removeLikes(check202!!)
                                }
                            }

                        })
                    }
                }
            }

            yourmCount.text = inComingShare.YorumCount.toString()
            username.text = "${inComingShare.UserID}, ${UserPortal.fixDate(inComingShare.PublishedTime!!)}"
            header.text = inComingShare.Header
            message.text = inComingShare.Message
            LikeCount.text = inComingShare.UpVoteCount.toString()
        }
    }
}