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

    constructor(username:String,password:String,role:String,email:String?,accessToken:String?)
    {
        this.Username = username
        this.Email = email
        this.Password = password
        this.Role = role
        this.AccessToken = accessToken
    }
    constructor()
    {

    }

}