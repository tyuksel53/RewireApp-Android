package com.example.taha.sigraylamcadele.Model


class Shares{

    var Message:String? = null
    var UserID:String? = null
    var PublishedTime:String? = null
    var UpVoteCount:Int? = null

    constructor(message:String,userId:String,publishedTime:String,upvoteCount:Int)
    {
        this.Message = message
        this.PublishedTime = publishedTime
        this.UserID = userId
        this.UpVoteCount = upvoteCount
    }
    constructor()
    {

    }

}