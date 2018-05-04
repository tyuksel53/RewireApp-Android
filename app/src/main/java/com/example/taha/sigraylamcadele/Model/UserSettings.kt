package com.example.taha.sigraylamcadele.Model

class UserSettings {

    var Notification:String? = null
    var TimeZoneName:String? = null
    var UserCheckUpTime:String? = null
    constructor(notification:String,timeZoneName:String,checkUpTime:String)
    {
        this.Notification = notification
        this.TimeZoneName = timeZoneName
        this.UserCheckUpTime = checkUpTime
    }

    constructor()
    {

    }
}