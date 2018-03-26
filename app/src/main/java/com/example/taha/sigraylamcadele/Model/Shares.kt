package com.example.taha.sigraylamcadele.Model


class Shares{

    var message:String? = null
    var userId:String? = null
    var publishedTime:String? = null
    var upVoteCount:Int? = null

    constructor(message:String,userId:String,publishedTime:String,upvoteCount:Int)
    {
        this.message = message
        this.publishedTime = publishedTime
        this.userId = userId
        this.upVoteCount = upVoteCount
    }
    constructor()
    {

    }

}