package com.example.capstone_design.Fragmentset

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.R


class FirstPage : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.firstpage, container, false)

        var Linear = view.findViewById<LinearLayout>(R.id.mainframe)
        var TextTitle = view.findViewById<TextView>(R.id.AppNameText)
        var gotoLoginPage = view.findViewById<Button>(R.id.LoginButton)
        var gotoJoinPage = view.findViewById<Button>(R.id.JoinButton)

        Linear.bringToFront()

        var Imageview = view.findViewById<ImageView>(R.id.imagesview)
        //Imageview.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        Glide.with(this).load(R.raw.result).into(Imageview)

        gotoLoginPage.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.login_and_join_frameLayout,
                LoginPage()
            ).commit()
        }

        gotoJoinPage.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.login_and_join_frameLayout,
                JoinPage()
            ).commit()
        }

        return view
    }
}