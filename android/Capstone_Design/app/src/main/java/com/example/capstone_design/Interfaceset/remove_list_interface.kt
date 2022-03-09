package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PlanInfo

interface remove_list_interface {
    fun RemoveList( position:Int)
    fun changeDetail(planList : MutableList<PlanInfo>,position:Int)
}