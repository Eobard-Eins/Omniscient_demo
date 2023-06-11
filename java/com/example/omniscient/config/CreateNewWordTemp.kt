package com.example.omniscient.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


object CreateNewWordTemp {

    var AttrList = (mutableListOf<String>())
    var AttrToInfo= mutableMapOf<String,String>()//属性 to 对应信息的map
    var numProperties by mutableStateOf("")//词
    var AttrToRele= mutableMapOf<String,String>()//属性 to 对应的关联词条 关联词条1_关联词条2_关联词条3

    fun Clear(){
        AttrList.clear()
        AttrToInfo.clear()
        numProperties = ""
        AttrToRele.clear()
    }
    fun isEmpty():Boolean{
        if(AttrList.isNotEmpty()) return false
        if(AttrToInfo.isNotEmpty()) return false
        if(AttrToRele.isNotEmpty()) return false
        if(numProperties!="") return false
        return true
    }
}