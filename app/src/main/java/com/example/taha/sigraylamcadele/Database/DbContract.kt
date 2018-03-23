package com.example.taha.sigraylamcadele.Database

import android.provider.BaseColumns

/**
 * Created by Taha on 12-Mar-18.
 */

class DbContract {

    class UserEntry : BaseColumns {
        companion object {
            var TABLE_NAME = "User"

            var _ID = BaseColumns._ID
            var COLUMN_USERNAME = "username"
            var COLUMN_ROLE = "role"
            var COLUMN_EMAIL = "email"
            var COLUMN_PASSWORD = "password"
            var COLUMN_ACCESSTOKEN = "accesstoken"
        }
    }


}
