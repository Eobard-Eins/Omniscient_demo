package com.example.omniscient.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.omniscient.R
import com.example.omniscient.composable.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataConfig {
    lateinit var file: SharedPreferences
    lateinit var fileEdit: SharedPreferences.Editor

    var FavoriteFolds:MutableSet<String> = mutableSetOf()
    var wordInFavorite:MutableMap<String,MutableSet<String>> = mutableMapOf()

    var UserName:String=""//2~6
    var PassWord:String=""
    var ID:String=""
    var Login:Boolean=false
    var Avatar:Int= R.drawable.users


    fun init(){
        if(file.contains("account")) ID= file.getString("account","").toString()
        else fileEdit.putString("account","")

        if(file.contains("password")) PassWord= file.getString("password","").toString()
        else fileEdit.putString("password","").apply()

        if(file.contains("username")) UserName= file.getString("username","").toString()
        else fileEdit.putString("username","").apply()

        if(file.contains("login")) Login= file.getBoolean("login",false)
        else fileEdit.putBoolean("login",false).apply()

        if(file.contains("avatar")) Avatar= file.getInt("avatar",R.drawable.users)
        else fileEdit.putInt("avatar",R.drawable.users).apply()

        if(file.contains("favorites")) FavoriteFolds= strToSet(file.getString("favorites", "").toString())
        else fileEdit.putString("favorites", "").apply()

        FavoriteFolds.forEach {
            wordInFavorite[it] = strToSet(file.getString("@$it", "").toString())
        }


        fileEdit.apply()
    }
    fun listToSet(ls:MutableList<String>):MutableSet<String>{
        val res= mutableSetOf<String>()
        res.addAll(ls)
        return res
    }
    fun listToStr(st:MutableList<String>):String{
        var res=""
        if(st.isEmpty()) return res
        var flag=false
        st.forEach {
            if(flag) res= "${res}_$it"
            else { res += it;flag=true}
        }
        return res
    }
    private fun strToSet(s:String): MutableSet<String> {
        val res:MutableSet<String> = mutableSetOf()
        if(s=="") return res
        val temp=s.split('@')
        res.addAll(temp)
        return res
    }
    private fun setToStr(st:Set<String>):String{
        var res=""
        if(st.isEmpty()) return res
        var flag=false
        st.forEach {
            if(flag) res= "$res@$it"
            else { res += it;flag=true}
        }
        return res
    }




    fun addWordToFavorite(word:String,favorite: String):Boolean{
        var res=false
        res=wordInFavorite[favorite]?.add(word)==true
        fileEdit.putString("@$favorite", setToStr(wordInFavorite[favorite]!!) )
        fileEdit.commit()
        return res
    }
    fun delWordToFavorite(word:String,favorite: String):Boolean{
        var res=false
        if(wordInFavorite[favorite]?.contains(word) == true) res=wordInFavorite[favorite]?.remove(word)==true
        fileEdit.putString("@$favorite", setToStr(wordInFavorite[favorite]!!) ).commit()
        return res
    }
    fun addFavorite(fold:String):Boolean{
        var res=false
        res=FavoriteFolds.add(fold)
        wordInFavorite[fold]= mutableSetOf()
        fileEdit.putString("favorites", setToStr(FavoriteFolds)).putString("@$fold", setToStr(wordInFavorite[fold]!!) ).apply()
        return res
    }
    fun delFavorite(fold:String):Boolean{
        var res=false
        if(FavoriteFolds.contains(fold)) res=FavoriteFolds.remove(fold)
        if(wordInFavorite.contains(fold)) wordInFavorite.remove(fold)
        fileEdit.putString("favorites", setToStr(FavoriteFolds)).remove("@$fold").apply()
        return res
    }

    fun getWordsFromFavorite(s:String):MutableList<String>{
        val res= mutableListOf<String>()
        if(!wordInFavorite.containsKey(s)) return res
        wordInFavorite[s]?.forEach {
            res.add(it)
        }
        return res
    }


    fun update(){
        fileEdit.putString("account",ID)
            .putString("username", UserName)
            .putString("password", PassWord)
            .putBoolean("login", Login)
            .putInt("avatar", Avatar)
            .apply()
    }

    fun clear(){
        Login=false
        UserName=""//2~6
        PassWord=""
        ID=""
        Avatar=R.drawable.users
        update()


        FavoriteFolds.forEach {
            fileEdit.remove("@$it")
        }
        FavoriteFolds= mutableSetOf()
        wordInFavorite= mutableMapOf()
        fileEdit.putString("favorites", "")
        fileEdit.apply()
    }

    suspend fun updateFavorite(){
        fileEdit.putString("favorites", setToStr(FavoriteFolds) )
        var i=0
        FavoriteFolds.forEach {
            withContext(Dispatchers.Default) {
                fileEdit.putString("@$it", setToStr(wordInFavorite[it]!!) )
                i+=1
            }
        }
        if(i>= FavoriteFolds.size) fileEdit.apply()
    }
}