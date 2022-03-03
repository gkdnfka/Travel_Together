package com.example.capstone_design.Fragmentset
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstone_design.Util.FavoriteAddManager
import com.example.capstone_design.R


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

