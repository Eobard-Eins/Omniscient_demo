package com.example.omniscient

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.omniscient.composable.FavoriteDialog
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.page.ErrorPage
import com.example.omniscient.page.ResultPage
import com.example.omniscient.unit.Repository
import com.example.omniscient.util.produceUiState
import com.example.omniscient.util.trans
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

@Composable
fun testPage() {/*
    val (postUiState, refreshPost, clearError) = produceUiState<Repository,Boolean>(Repository){
        isSearched("as")
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    if (postUiState.value.hasError) {
        NetworkErrorSnackbar(
            onRefresh = refreshPost,
            onClearError = clearError,
            scaffoldState
        )
    }*//*
    val FavoriteFolds = trans<Map<String,MutableList<String>>>{ getFavoriteInfo(DataConfig.ID) }
    FavoriteFolds.run {
        if(FavoriteFolds.hasError){
            Text("empty")
        }else{
            //DataConfig.wordInFavorite["test2"]?.add("W2")
            //val a=DataConfig.wordInFavorite["test2"]
            //DataConfig.fileEdit.putStringSet("@test2", a).commit()
            //val t = DataConfig.file.getStringSet("@test2", setOf<String>())
            Text(text = DataConfig.UserName)
            Text(FavoriteFolds.value.toString())
        }
    }*/

//DataConfig.wordInFavorite["test2"]?.add("W2")
            //val a=DataConfig.wordInFavorite["test2"]
            //DataConfig.fileEdit.putStringSet("@test2", a).commit()
            //val t = DataConfig.file.getStringSet("@test2", setOf<String>())
    //DataConfig.addWordToFavorite("W2","test2")
    //Text(DataConfig.file.getStringSet("@test2", setOf()).toString())
    //FavoriteDialog(navController = rememberNavController(), res = 1, word = "W1")





    val opFavoriteEvent=remember{ mutableStateOf(false) }
    val info = remember{ mutableStateOf("a") }
    Text(text = info.value)
    Button(onClick = { /*TODO*/ opFavoriteEvent.value=true}) {
        Text(text = "123")
    }
    val opFavoriteInfo=if(opFavoriteEvent.value) {
        trans<Boolean> {
            val attrList= mutableListOf<String>("A1","A2")
            val mp= mutableMapOf<String,String>("A1" to "W1_W2","A2" to "")
            addWordRel("W8", attrList = attrList, mp)
        }
    } else null

    opFavoriteInfo?.run {
        if(opFavoriteInfo.hasError) info.value="3"
        else{
            opFavoriteInfo.value?.run {
                if(this){
                    info.value="1"
                    opFavoriteEvent.value=false
                }else{
                    info.value="2"
                    opFavoriteEvent.value=false
                }

            }
        }

    }
}