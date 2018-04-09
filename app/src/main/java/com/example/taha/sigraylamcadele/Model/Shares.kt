package com.example.taha.sigraylamcadele.Model

import java.io.Serializable


class Shares:Serializable{

    var ID:Int? = null
    var Header:String? = null
    var Message:String? = null
    var UserID:String? = null
    var PublishedTime:String? = null
    var UpVoteCount:Int? = null
    var YorumCount:Int? = null

    constructor(message:String,userId:String,publishedTime:String,upvoteCount:Int,header:String,yorumCount:Int,Id:Int)
    {
        this.ID =Id
        this.Header = header
        this.Message = message
        this.PublishedTime = publishedTime
        this.UserID = userId
        this.UpVoteCount = upvoteCount
        this.YorumCount = yorumCount
    }
    constructor()
    {

    }

}