package com.rewire.mobile.app.Model

/**
 * Created by Taha on 05-Mar-18.
 */

class LoginResponse
{
    var access_token:String? = null
    var token_type:String? = null
    var expires_in:String? = null

    constructor(accessToken:String,tokenType:String,expiresIn:String)
    {
        this.access_token = accessToken
        this.token_type = tokenType
        this.expires_in = expiresIn
    }

}