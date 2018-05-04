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
            var COLUMN_TIMEZONEID = "timezoneid"
            var COLUMN_LANGUAGE = "language"
        }
    }

    class SettingsEntry:BaseColumns{
        companion object {
            var TABLE_NAME = "Settings"

            var _ID = BaseColumns._ID
            var COLUMN_NOTFICATION = "notfication"
            var COLUMN_TIMEZONENAME = "timezone"

        }
    }

    class JournalEntry:BaseColumns{
        companion object {
            var TABLE_NAME = "Journal"

            var _ID = BaseColumns._ID
            var COLUMN_HEADER = "header"
            var COLUMN_MESSAGE = "message"
            var COLUMN_DATE = "date"

        }
    }


}
