package com.example.taha.sigraylamcadele.Model

/**
 * Created by Taha on 11-Mar-18.
 */
class User {


    var Username:String? = null
    var Password:String? = null
    var Role:String? = null
    var Email:String? = null
    var AccessToken:String? = null
    var TimeZoneId:String? = null
    var Language:String? = null
    var LastLoginTime:String? = null
    var RegisteredDate:String? = null

    constructor(username:String, password:String, role:String, email:String?, accessToken:String?
                ,timeZoneId:String?,language:String?,lastLoginTime:String?,registeredDate:String?)
    {
        this.Username = username
        this.Email = email
        this.Password = password
        this.Role = role
        this.AccessToken = accessToken
        this.TimeZoneId = timeZoneId
        this.Language = language
        this.LastLoginTime = lastLoginTime
        this.RegisteredDate = registeredDate
    }

    constructor()
    {

    }

}