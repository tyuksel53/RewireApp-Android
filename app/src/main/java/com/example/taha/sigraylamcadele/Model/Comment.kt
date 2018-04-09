package com.example.taha.sigraylamcadele.Model

class Comment {

    var ID:Int? = null
    var Message:String? = null
    var ShareId:Int? = null
    var Username:String? = null
    var Date:String? = null

    constructor()
    {

    }

    constructor(id:Int,message:String,shareID:Int,Username:String,Date:String)
    {
        this.ID = id
        this.Date = Date
        this.Message = message
        this.ShareId = shareID
        this.Username = Username

    }


}