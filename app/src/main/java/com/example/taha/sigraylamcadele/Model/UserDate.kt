package com.example.taha.sigraylamcadele.Model

import java.io.Serializable

class UserDate:Serializable {

    var Id:Int? = null
    var Date:String? = null
    var Type:Int? = null
    var Username:String? = null
    var SmokeCount:Int? = null

    constructor(id:Int?,date:String?,type:Int?,username:String?,smokeCount:Int?)
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