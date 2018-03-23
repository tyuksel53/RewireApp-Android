package com.example.taha.sigraylamcadele.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taha.sigraylamcadele.Database.DbContract.*

/**
 * Created by Taha on 12-Mar-18.
 */

class DatabaseHelper : SQLiteOpenHelper {


    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int) : super(context, name, factory, version) {}

    constructor(context: Context) : super(context, DATABSE_NAME, null, DATABASE_VERSION) {}

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_USERS_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME)
        onCreate(db)

    }

    companion object {

        val DATABSE_NAME = "sigaraylamucadele.db"
        private val DATABASE_VERSION = 2

        private val TABLE_USERS_CREATE = "CREATE TABLE " + UserEntry.TABLE_NAME + "(" +
                UserEntry._ID + " INTEGER PRIMARY KEY, " +
                UserEntry.COLUMN_USERNAME + " TEXT, " +
                UserEntry.COLUMN_PASSWORD + " TEXT, " +
                UserEntry.COLUMN_EMAIL + " TEXT, " +
                UserEntry.COLUMN_ROLE + " TEXT, "+
                UserEntry.COLUMN_ACCESSTOKEN + " TEXT)"
    }
}
