package com.example.capstone_design

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirstPage : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.firstpage, container, false)

        var gotoLoginPage = view.findViewById<Button>(R.id.LoginButton)
        var gotoJoinPage = view.findViewById<Button>(R.id.JoinButton)

        gotoLoginPage.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, LoginPage()).commit()
        }

        gotoJoinPage.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, JoinPage()).commit()
        }

        return view
    }
}