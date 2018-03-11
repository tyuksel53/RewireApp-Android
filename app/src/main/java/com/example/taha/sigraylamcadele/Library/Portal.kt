package com.example.taha.sigraylamcadele.Library

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Taha on 11-Mar-18.
 */
class Portal {

    companion object {
        fun isEmailValid(input:String):Boolean
        {
            var expression:String = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            var pattern =  Pattern.compile(expression,Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(input)
            return matcher.matches()
        }
    }

}