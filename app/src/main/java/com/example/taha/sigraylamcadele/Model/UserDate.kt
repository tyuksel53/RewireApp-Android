package com.example.taha.sigraylamcadele.Model

class UserDate {

    var Id:Int? = null
    var Date:String? = null
    var Type:Int? = null
    var Username:String? = null
    var SmokeCount:Double? = null

    constructor(id:Int?,date:String?,type:Int?,username:String?,smokeCount:Double?)
    {
        this.Date = date
        this.Id = id
        this.Type = type
        this.Username = username
        this.SmokeCount = smokeCount
    }
    constructor()
    {

    }

}