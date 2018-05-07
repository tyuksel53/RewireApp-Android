package com.rewire.mobile.app.Model

class ShareLike {

    var Id:Int? = null
    var ShareId:Int? = null
    var UserID:String? = null

    constructor(id:Int,shareid:Int,userId:String)
    {
        this.Id = id
        this.ShareId = shareid
        this.UserID = userId
    }

    constructor()
    {

    }
}