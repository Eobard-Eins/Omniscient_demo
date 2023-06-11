package com.example.omniscient.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.page.ErrorPage
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.Repository
import com.example.omniscient.util.trans
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun FavoriteDialog(navController: NavHostController, word:String){

    val showDialog = remember { mutableStateOf(false) }

    val lt=DataConfig.FavoriteFolds.toMutableList()
    var lexiconList by remember {
        mutableStateOf(
            lt
        )
    }//假设这是要显示的收藏夹
    var lexiconMap by remember {
        mutableStateOf(DataConfig.wordInFavorite)
    }

    val inFavorite= remember {
        mutableStateOf(false)
    }

    val old_list= mutableListOf<String>()
    lt.forEach {
        if(lexiconMap[it]?.contains(word) == true){
            inFavorite.value=true
            old_list.add(it)
        }
    }
    val list = mutableListOf<String>()
    old_list.forEach { list.add(it) }

    IconButton(
        onClick = {
            if (DataConfig.Login) showDialog.value = true
            else {navController.navigate(RouteConfig.ROUTE_NoLoginError)}
        }
    ) {
        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite", tint = if(inFavorite.value) MyOrange else MyBlack)
    }

    var info1 by remember {
        mutableStateOf(false)
    }
    var info2 by remember {
        mutableStateOf(false)
    }
//Text(text = info.toString())


    val addList= remember {
        mutableListOf<String>()
    }
    val delList= remember {
        mutableListOf<String>()
    }

    val opFavoriteEvent=remember{ mutableStateOf(false) }
    val opFavoriteInfo=if(opFavoriteEvent.value) {
        trans<Boolean> {
            opWordInFavorite(
                DataConfig.ID,
                word,
                addFavorites = addList,
                delFavorites = delList
            )
        }
    } else null

    opFavoriteInfo?.run {
        opFavoriteInfo.hasError.run{
            if(opFavoriteInfo.hasError)navController.navigate(RouteConfig.ROUTE_NetError)
            else{
                opFavoriteInfo.value?.run {
                    if(this){
                        old_list.clear()
                        list.forEach { old_list.add(it) }
                        addList.forEach { lexiconMap[it]?.add(word) }
                        delList.forEach { lexiconMap[it]?.remove(word) }
                        if (old_list.isEmpty()) inFavorite.value = false
                        info1=true
                        opFavoriteEvent.value=false
                        showDialog.value = false
                    }else{
                        info2=true
                        opFavoriteEvent.value=false
                        showDialog.value = false
                    }

                }
            }
        }
    }

     if(showDialog.value) {
         Dialog(
             onDismissRequest = { showDialog.value = false },
             properties = DialogProperties(usePlatformDefaultWidth = false)
         ) {
             Box(Modifier
                 .fillMaxWidth()
                 .fillMaxHeight()
                 .clickable(onClick = {
                     showDialog.value = false
                 },
                     indication = null,
                     interactionSource = remember { MutableInteractionSource() }
                 ),
                 contentAlignment = Alignment.Center) {
                 Column(
                     Modifier
                         .size(300.dp, 400.dp)
                         .shadow(5.dp, RoundedCornerShape(10.dp))
                         .background(
                             MaterialTheme.colors.background,
                             RoundedCornerShape(10.dp)
                         )
                 ) {
                     /**
                      * 标题
                      */
                     Text(
                         modifier = Modifier.padding(top = 14.dp, start = 14.dp),
                         text = "选择收藏夹",
                         fontSize = 20.sp,
                         color = MyBlack
                     )
                     Row(Modifier.padding(start = 8.dp, top = 3.dp, bottom = 5.dp)) {
                         Spacer(
                             modifier = Modifier
                                 .background(color = MyLightGray)
                                 .size(160.dp, 0.6.dp)
                         )
                     }

                     /**
                      * 列表
                      */

                     Box(
                         Modifier
                             .weight(1f)
                             .background(color = MyGray)
                     ) {
                         LazyColumn(
                             contentPadding = PaddingValues(1.dp)
                         ) {
                             items(lexiconList) { it ->
                                 val flag = remember { mutableStateOf(old_list.contains(it)) }
                                 Button(
                                     modifier = Modifier
                                         .fillMaxWidth()
                                         .height(60.dp)
                                         .padding(10.dp, 2.dp),
                                     onClick = {
                                         flag.value = !flag.value
                                         if (list.contains(it)) list.remove(it)
                                         else list.add(it)
                                     },
                                     shape = RoundedCornerShape(10.dp),
                                     colors = ButtonDefaults.buttonColors(
                                         backgroundColor = MaterialTheme.colors.background
                                     ),
                                     contentPadding = PaddingValues(0.dp),
                                     interactionSource = remember { NoRippleInteractionSource() }
                                 ) {
                                     Text(
                                         modifier = Modifier
                                             .weight(1f)
                                             .padding(start = 14.dp),
                                         text = it,
                                         fontSize = 16.sp,
                                         color = MyBlack,
                                         textAlign = TextAlign.Left,
                                         maxLines = 1,
                                         overflow = TextOverflow.Ellipsis
                                     )
                                     Checkbox(
                                         checked = flag.value,
                                         onCheckedChange = { b ->
                                             flag.value = !flag.value
                                             if (!b) list.remove(it)
                                             else list.add(it)
                                         },
                                         colors = CheckboxDefaults.colors(
                                             checkedColor = MyOrange,
                                             uncheckedColor = MyDeepGray,
                                             checkmarkColor = MyWhite
                                         ),
                                         interactionSource = remember { NoRippleInteractionSource() }
                                     )
                                 }
                             }
                         }
                     }

                     /**
                      * 提交
                      */
                     Button(
                         modifier = Modifier
                             .fillMaxWidth()
                             .height(40.dp),
                         onClick = {
                             //tool.addFavorite(word,list)

                             delList.clear()
                             addList.clear()
                             for (i in list) {
                                 if (!old_list.contains(i)) {
                                     addList.add(i)
                                 }
                             }

                             for (i in old_list) {
                                 if (!list.contains(i)) {
                                     delList.add(i)
                                 }
                             }


                             //info = addList.toString()
                             //lexiconMap["1"]?.remove("word")

                             opFavoriteEvent.value=true
                         },
                         shape = RoundedCornerShape(0.dp),
                         colors = ButtonDefaults.buttonColors(
                             backgroundColor = MaterialTheme.colors.background
                         ),
                         contentPadding = PaddingValues(0.dp)
                     ) {

                         Text(
                             modifier = Modifier
                                 .weight(1f)
                                 .padding(start = 20.dp),
                             text = "已完成",
                             color = MyBlack,
                             textAlign = TextAlign.Center,
                             maxLines = 1,
                             overflow = TextOverflow.Ellipsis
                         )

                     }
                 }
             }
         }
     }

    if(info1) {
        Dialog(
            onDismissRequest = { info1 = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = {
                        info1 = false
                    },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .size(140.dp, 140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xDDA0A0A0), RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Default.Done, contentDescription = null,
                        tint = MyQing, modifier = Modifier.size(100.dp),
                    )
                    Text(
                        text = "操作成功",
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        color = MyWhite,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
        }
    }
    if(info2) {
        Dialog(
            onDismissRequest = { info2 = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = {
                        info2 = false
                    },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Row(modifier= Modifier
                        .padding(bottom = 75.dp)
                        .shadow(5.dp, RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colors.background,
                            RoundedCornerShape(10.dp)
                        )) {
                        Text(modifier= Modifier
                            .padding(vertical = 10.dp, horizontal = 25.dp),
                            text = "操作失败", color = MyDeepGray)
                    }
                }
            }
        }
    }
}