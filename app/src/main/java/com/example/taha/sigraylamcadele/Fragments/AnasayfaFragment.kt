package com.example.taha.sigraylamcadele.Fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.UserActivitiesActivity
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.User

import com.example.taha.sigraylamcadele.R
import es.dmoral.toasty.Toasty

class AnasayfaFragment : android.app.Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_anasayfa, container, false)
        val username = view.findViewById<TextView>(R.id.tvAnaEkranUsername)
        username.setText(UserPortal.loggedInUser!!.Username)
        val bestStrikeText = view.findViewById<TextView>(R.id.tvAnaEkranBestStrikeText)
        bestStrikeText.setText(UserPortal.myLangResource!!.getString(R.string.en_iyi_seri))
        val currentStrikeTExt = view.findViewById<TextView>(R.id.tvAnaEkranCurrentText)
        currentStrikeTExt.setText(UserPortal.myLangResource!!.getString(R.string.şuanki_seri))
        val actions = view.findViewById<TextView>(R.id.tvAnaEkranAction)
        actions.setText(UserPortal.myLangResource!!.getString(R.string.Hareketlerin))
        val posts = view.findViewById<TextView>(R.id.tvAnaEkranPosts)
        posts.setText(UserPortal.myLangResource!!.getString(R.string.paylasimlarin))

        posts.setOnClickListener {
            val intent = Intent(activity,UserActivitiesActivity::class.java)
            intent.putExtra("type",1)
            startActivity(intent)
        }
        val likes = view.findViewById<TextView>(R.id.tvAnaEkranLikes)
        likes.setText(UserPortal.myLangResource!!.getString(R.string.begenilerin))
        likes.setOnClickListener {
            val intent = Intent(activity,UserActivitiesActivity::class.java)
            intent.putExtra("type",2)
            startActivity(intent)
        }
        val currentStrikeNumber = view.findViewById<TextView>(R.id.tvAnaEkranCurrentNumber)
        val bestStrikeNumber = view.findViewById<TextView>(R.id.tvAnaEkranBestStrikeNumber)

        var bestStrikeDummy = Int.MIN_VALUE
        var bestStrike = 0
        var currentStrike = 0
        var flag = false

        for(i in 0 until UserPortal.userDates!!.size)
        {
            if(UserPortal.userDates!![i].Type == 1)
            {
                if(!flag) {
                    currentStrike++
                }
                bestStrike++
            }

            if(UserPortal.userDates!![i].Type == 2)
            {
                flag = true
                if(bestStrikeDummy < bestStrike)
                {
                    bestStrikeDummy = bestStrike
                }
                bestStrike = 0
            }
        }

        if(bestStrikeDummy < bestStrike)
        {
            bestStrikeDummy = bestStrike
        }

        currentStrikeNumber.text = currentStrike.toString()
        bestStrikeNumber.text = bestStrikeDummy.toString()


        return view
    }

}
