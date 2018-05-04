package com.example.taha.sigraylamcadele.Model

class UserSettings {

    var Notification:String? = null
    var TimeZoneName:String? = null

    constructor(notification:String,timeZoneName:String)
    {
        this.Notification = notification
        this.TimeZoneName = timeZoneName
    }

    constructor()
    {

    }
}