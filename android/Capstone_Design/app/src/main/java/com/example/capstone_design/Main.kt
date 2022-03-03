package com.example.capstone_design
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class Main : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.main, container, false)

        var tv = view.findViewById<TextView>(R.id.main_test_text)
        tv.text = FavoriteAddManager.prefs.getString("NAME", "") + " 님, 안녕하세요"

        return view
    }
}

