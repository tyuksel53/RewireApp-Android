package com.rewire.mobile.app.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rewire.mobile.app.Database.DbContract.*

/**
 * Created by Taha on 12-Mar-18.
 */

class DatabaseHelper : SQLiteOpenHelper {


    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int) : super(context, name, factory, version) {}

    constructor(context: Context) : super(context, DATABSE_NAME, null, DATABASE_VERSION) {}

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_USERS_CREATE)
        db.execSQL(TABLE_SETTINGS_CREATE)
        db.execSQL(TABLE_JOURNAL_CREATE)
        db.execSQL(TABLE_NOTIFY_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + SettingsEntry.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + NotifyEntry.TABLE_NAME)
        onCreate(db)

    }

    companion object {

        val DATABSE_NAME = "sigaraylamucadele.db"
        private val DATABASE_VERSION = 10

        private val TABLE_USERS_CREATE = "CREATE TABLE " + UserEntry.TABLE_NAME + "(" +
                UserEntry._ID + " INTEGER PRIMARY KEY, " +
                UserEntry.COLUMN_USERNAME + " TEXT, " +
                UserEntry.COLUMN_PASSWORD + " TEXT, " +
                UserEntry.COLUMN_EMAIL + " TEXT, " +
                UserEntry.COLUMN_ROLE + " TEXT, "+
                UserEntry.COLUMN_ACCESSTOKEN + " TEXT, "+
                UserEntry.COLUMN_TIMEZONEID + " TEXT, "+
                UserEntry.COLUMN_LANGUAGE + " TEXT)"

        private val TABLE_SETTINGS_CREATE = "CREATE TABLE " + SettingsEntry.TABLE_NAME + "(" +
                SettingsEntry._ID + " INTEGER PRIMARY KEY, " +
                SettingsEntry.COLUMN_NOTFICATION + " TEXT, " +
                SettingsEntry.COLUMN_TIMEZONENAME + " TEXT, " +
                SettingsEntry.COLUMN_USERNAME + " TEXT, " +
                SettingsEntry.COLUMN_CHECKUPTIME + " TEXT) "

        private val TABLE_JOURNAL_CREATE = "CREATE TABLE " + JournalEntry.TABLE_NAME + "(" +
                JournalEntry._ID + " INTEGER PRIMARY KEY, " +
                JournalEntry.COLUMN_HEADER + " TEXT, " +
                JournalEntry.COLUMN_MESSAGE + " TEXT, " +
                JournalEntry.COLUMN_USERNAME + " TEXT, " +
                JournalEntry.COLUMN_DATE + " TEXT)"

        private val TABLE_NOTIFY_CREATE = "CREATE TABLE " + NotifyEntry.TABLE_NAME + "(" +
                NotifyEntry._ID + " INTEGER PRIMARY KEY, " +
                NotifyEntry.COLUMN_DATE + " TEXT, " +
                NotifyEntry.COLUM_USERNAME + " TEXT, " +
                NotifyEntry.COLUM_NOTIFICATON_SENT + " TEXT)"
    }
}
