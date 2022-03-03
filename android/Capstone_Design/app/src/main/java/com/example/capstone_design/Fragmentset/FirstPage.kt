package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Fragmentset.JoinPage
import com.example.capstone_design.Fragmentset.LoginPage
import com.example.capstone_design.R


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