package com.example.omniscient.unit

import android.os.Debug
import com.example.omniscient.R
import com.example.omniscient.composable.Favorite
import com.example.omniscient.config.DataConfig
import com.example.omniscient.util.Res
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException

object Repository {
    //Python版后端
    //private var neo:Neo4j=Neo4j()

    //Java版后端
    private var neo:Neo4j_javaServer=Neo4j_javaServer()
    
    suspend fun test(s:String):Res<String> =try{
        withContext(Dispatchers.IO) {
            return@withContext Res.Success(neo.TEST(s).toString())
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun isSearched(str: String):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            return@withContext Res.Success(neo.isSearched(str))
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun getResult(str: String,includeIsSearched:Boolean=false): Res<Map<String,Any>> =try{
        withContext(Dispatchers.IO) {
            val res:Map<String,Any> =neo.getResult(str,includeIsSearched)
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }
//TODO

    suspend fun addWordInfo(word: String,attrList:List<String>,attrToInfo:Map<String,String>):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            var res:Boolean=true
            var flag=0
            var op=0
            if(attrList.isEmpty())  return@withContext Res.Success(false)

            for(i in attrList){
                flag+=1
                res=res&&if(flag>=attrList.size){
                    if(op>=attrList.size-1) neo.addWordInfo("1", word,i, attrToInfo[i].toString())
                    else false
                }else{
                    async { neo.addWordInfo("0", word,i, attrToInfo[i].toString()); op+=1; true }.await()
                }
            }
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }
    suspend fun addWordRel(word:String, attrList: List<String>, attrToRele:Map<String,String>):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            var res:Boolean=true
            var flag=0
            var op=0
            attrList.forEach{attr->
                flag+=1
                res=res&&if(flag>=attrList.size){
                    if(op>=attrList.size-1) neo.addWordRel("1", word,attr, attrToRele[attr].toString())
                    else false
                }else{
                    async { neo.addWordRel("0", word,attr, attrToRele[attr].toString()); op+=1; true }.await()
                }
            }
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun addFavorite(account: String,fold: String):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            val b:Boolean=async { DataConfig.addFavorite(fold) }.await()
            val a:Boolean=async { neo.addFavorite(account,fold) }.await()
            return@withContext Res.Success(a&&b)
        }
    }catch (err:IOException){
        Res.Error(err)
    }
    suspend fun delFavorite(account: String,fold: String):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            val b:Boolean=async { DataConfig.delFavorite(fold) }.await()
            val a:Boolean=async { neo.delFavorite(account,fold) }.await()
            return@withContext Res.Success(a&&b)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun opWordInFavorite(account: String,word:String,addFavorites:List<String> = listOf(),delFavorites:List<String> = listOf()):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            var res=true
            var A=true
            var B=true
            async {
                if(addFavorites.isNotEmpty()){
                    for(i in addFavorites){
                        async {
                            val b:Boolean=async { DataConfig.addWordToFavorite(word,i) }.await()
                            val a:Boolean=async { neo.OperateWordInFavorite(account,i,"add",word) }.await()
                            A=a&&b
                        }.await()
                    }
                }
            }.await()
            async {
                if(delFavorites.isNotEmpty()){
                    for(i in delFavorites){
                        async {
                            val a:Boolean=async { neo.OperateWordInFavorite(account,i,"del",word) }.await()
                            val b:Boolean=async { DataConfig.delWordToFavorite(word,i) }.await()
                            B=a&&b
                        }.await()
                    }
                }
            }.await()
            res=A&&B
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun opWordToFavorite(account: String,fold:String,addWords:List<String> =listOf(),delWords:List<String> =listOf()):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            var res=true
            var A=true
            var B=true
            async {
                if(addWords.isNotEmpty()){
                    for(i in addWords){
                        async {
                            val b:Boolean=async { DataConfig.addWordToFavorite(i,fold) }.await()
                            val a:Boolean=async { neo.OperateWordInFavorite(account,fold,"add",i) }.await()
                            A=a&&b
                        }.await()
                    }
                }
            }.await()
            async {
                if(delWords.isNotEmpty()){
                    for(i in delWords){
                        async {
                            val a:Boolean=async { neo.OperateWordInFavorite(account,fold,"del",i) }.await()
                            val b:Boolean=async { DataConfig.delWordToFavorite(i,fold) }.await()
                            B=a&&b
                        }.await()
                    }
                }
            }.await()
            res=A&&B
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun getFavoriteFold(userName: String): Res<MutableList<String>> =try{
        withContext(Dispatchers.IO) {
            val res:MutableList<String> = if(DataConfig.FavoriteFolds.isEmpty()) mutableListOf() else DataConfig.FavoriteFolds.toList() as MutableList<String>
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun getFavoriteInfo(account: String,allWord:Boolean=false)
    : Res<Map<String,MutableList<String>>> =try{
        withContext(Dispatchers.IO) {
            val res = mutableMapOf<String,MutableList<String>>()
            /*res["@folds"]= DataConfig.FavoriteFolds.toList() as MutableList<String>
            res+=DataConfig.wordInFavorite*/
            //val info:MutableList<String> = neo.getFavoriteFold(account)
            val info:MutableList<String> = if(DataConfig.FavoriteFolds.isEmpty()) mutableListOf() else DataConfig.FavoriteFolds.toList() as MutableList<String>
            res["@folds"] = info
            if(allWord){
                val allword:MutableList<String> = mutableListOf()
                info.forEach {
                    withContext(Dispatchers.Default) {
                        val temp: MutableList<String> = DataConfig.getWordsFromFavorite(it)
                        res[it] = temp
                        allword.addAll(temp)
                    }
                }
                res["@allWord"]=allword
            }
            else{
                info.forEach {
                    async {
                        val temp:MutableList<String> = DataConfig.getWordsFromFavorite(it)
                        res[it]=temp
                    }.await()
                }
            }
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }
    private suspend fun updateWhenLoginOrRegister(account: String, password: String, userName: String){
        withContext(Dispatchers.Default) {
            async {
                val num:Int=(account.substring(7).toInt()%5)+1
                DataConfig.Login=true
                DataConfig.ID=account
                DataConfig.UserName=userName
                DataConfig.PassWord=password
                DataConfig.Avatar=when(num){
                    1 -> R.drawable.user_1
                    2 -> R.drawable.user_2
                    3 -> R.drawable.user_3
                    4 -> R.drawable.user_4
                    5 -> R.drawable.user_5
                    else -> R.drawable.users
                }
                DataConfig.update()
            }.await()
            async {
                val temp=neo.getFavoriteFold(account)
                var num=0
                temp.forEach {
                    async {
                        DataConfig.FavoriteFolds.add(it)
                        num+=1
                    }.await()
                }
                if(num>=temp.size&&num!=0){
                    var i=0
                    DataConfig.FavoriteFolds.forEach { it->
                        async {
                            val temp:MutableList<String> =neo.getWordInFavoriteFold(account,it)
                            DataConfig.wordInFavorite[it] = DataConfig.listToSet(temp)
                            i += 1
                        }.await()
                    }
                    if(i>=DataConfig.FavoriteFolds.size) DataConfig.updateFavorite()
                }

            }.await()
        }
    }

    suspend fun login(account:String,password: String): Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            val info:Map<String, Any> =neo.login(account,password)
            if(info["pass"]==true){
                updateWhenLoginOrRegister(info["account"] as String,info["password"] as String,info["username"] as String)
            }
            return@withContext Res.Success(info["pass"] as Boolean)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun register(account: String, password: String, userName: String): Res<String> =try{
        withContext(Dispatchers.IO) {
            val info:Map<String, Any> =neo.register(account,password,userName)
            val res:String=if(info["res"]==true) {
                updateWhenLoginOrRegister(account,password,userName)
                "true"
            } else info["info"] as String
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }

    suspend fun setUserInfo(account:String,info:Map<String,String>):Res<Boolean> =try{
        withContext(Dispatchers.IO) {
            val res:Boolean=neo.setUserInfo(account,info)
            if(res){
                val temp=DataConfig.file.edit()
                if(info.containsKey("username")) {DataConfig.UserName=info["username"].toString(); temp.putString("username",DataConfig.UserName)}
                if(info.containsKey("password")) {DataConfig.PassWord=info["password"].toString(); temp.putString("password",DataConfig.PassWord)}
                temp.apply()
            }
            return@withContext Res.Success(res)
        }
    }catch (err:IOException){
        Res.Error(err)
    }
}
