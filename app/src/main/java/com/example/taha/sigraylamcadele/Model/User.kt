package com.example.taha.sigraylamcadele.Model

/**
 * Created by Taha on 11-Mar-18.
 */
class User(username:String,password:String,role:String,email:String) {


    var Username:String? = null
    var Password:String? = null
    var Role:String? = null
    var Email:String? = null

    init {
        this.Username = username
        this.Email = email
        this.Password = password
        this.Role = role
    }


}