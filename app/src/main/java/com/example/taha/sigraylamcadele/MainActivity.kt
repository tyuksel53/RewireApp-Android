package com.example.taha.sigraylamcadele

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var tiklamaCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //kayitEkle("taha","12345","mundi","user")
        kayitlariOku()
        sil()
        tvNewUser.setOnClickListener {
            var intent = Intent(this@MainActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        tvLoginError.visibility = View.INVISIBLE
        pbLogin.visibility = View.INVISIBLE

        var apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        btnLogin.setOnClickListener {
            btnLogin.setEnabled(false)
            tvLoginError.visibility = View.INVISIBLE
            pbLogin.visibility = View.VISIBLE
            if( edLoginUsername.text.toString().isNotEmpty() && edLoginPassword.text.isNotEmpty() )
            {
                var result = apiInterface?.tokenAl(edLoginUsername.text.toString(),
                        edLoginPassword.text.toString(),"password")

                result?.enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        Toast.makeText(this@MainActivity,"Bir şeyler ters gitti",Toast.LENGTH_LONG);
                    }

                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        if(response?.message()?.toString() == "OK")
                        {
                            var body = response?.body()
                            Toast.makeText(this@MainActivity,"Giris basarili",Toast.LENGTH_LONG).show()
                        }else
                        {
                            tvLoginError.visibility = View.VISIBLE
                        }

                    }

                })

            }else
            {
                if(edLoginUsername.text.toString().isEmpty())
                    edLoginUsername.setError("Kullanici Adi boş olamaz")
                if(edLoginPassword.text.toString().isEmpty())
                    edLoginPassword.setError("Şifre boş olamaz")

                pbLogin.visibility = View.INVISIBLE
                btnLogin.setEnabled(true)
            }


        }

    }

    private fun guncelle()
    {
        var helper = DatabaseHelper(this@MainActivity)
        var db =  helper.readableDatabase

        var guncellenenDegerler = ContentValues()
        guncellenenDegerler.put(DbContract.UserEntry.COLUMN_ROLE,"admin")

        var args = arrayOf("1")

        var resultCount =  db.update(DbContract.UserEntry.TABLE_NAME,guncellenenDegerler,
                DbContract.UserEntry._ID + " = ?",args)


    }

    private fun sil()
    {
        val helper = DatabaseHelper(this@MainActivity)
        val db = helper.readableDatabase
        var args = arrayOf("100")

        var resultCount = db.delete(DbContract.UserEntry.TABLE_NAME,
                DbContract.UserEntry._ID + " < ?",
                args)
    }

    private fun kayitlariOku() {

        var helper = DatabaseHelper(this@MainActivity)
        var db = helper.readableDatabase

        var protection = arrayOf(DbContract.UserEntry.COLUMN_USERNAME,
                DbContract.UserEntry.COLUMN_EMAIL,
                DbContract.UserEntry.COLUMN_PASSWORD,
                DbContract.UserEntry.COLUMN_ROLE)

        var selection = DbContract.UserEntry._ID + " < ?"

        var selectionAgs = arrayOf("100")

        var myCorsor = db.query(DbContract.UserEntry.TABLE_NAME,protection,selection,selectionAgs,null,null,null)

        var count = myCorsor.count

        var myString:String = ""

        while(myCorsor.moveToNext())
        {
            for(i in 0 until myCorsor.columnCount)
            {
                myString += myCorsor.getString(i) + " "
            }

            myString += "\n"
        }


        myCorsor.close()
        db.close()
    }

    private fun kayitEkle(username:String,password:String,email:String,role:String) {

        var helper = DatabaseHelper(this@MainActivity)
        var db = helper.writableDatabase


        /*yöntem 1*/
        /*var query:String = "Insert INTO users("+
                DbContract.UserEntry.COLUMN_USERNAME + "," +
                DbContract.UserEntry.COLUMN_PASSWORD + "," +
                DbContract.UserEntry.COLUMN_EMAIL + "," +
                DbContract.UserEntry.COLUMN_ROLE + ")" +
                " values('$username', '$password' , '$email' , '$role' )"

        db.execSQL(query)*/

        /*yöntem 2*/
        var yeniKayit = ContentValues()
        yeniKayit.put(DbContract.UserEntry.COLUMN_USERNAME,username)
        yeniKayit.put(DbContract.UserEntry.COLUMN_PASSWORD,password)
        yeniKayit.put(DbContract.UserEntry.COLUMN_EMAIL,email)
        yeniKayit.put(DbContract.UserEntry.COLUMN_ROLE,role)

        var id = db.insert(DbContract.UserEntry.TABLE_NAME,null,yeniKayit)


    }
}
