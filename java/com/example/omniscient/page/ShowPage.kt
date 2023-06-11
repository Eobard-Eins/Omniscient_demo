package com.example.omniscient.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.composable.*
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.Repository
import com.example.omniscient.util.produceUiState
import com.example.omniscient.util.trans
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.IOException


@Composable
fun ShowPage(navController: NavHostController,word:String) {

    val inDataBase = trans<Boolean>{ isSearched(word) }
    inDataBase.run {
        if(inDataBase.hasError){
            navController.navigate(RouteConfig.ROUTE_NetError)
        }else if (inDataBase.value == false) {
            ErrorPage(navController, "无匹配词条")
        }else if(inDataBase.value == true){
            val info= trans<Map<String,Any>>{ getResult(word) }
            info.run {
                if(info.hasError){
                    navController.navigate(RouteConfig.ROUTE_NetError)
                }
                else {
                    info.value?.let { ResultPage(navController = navController, word , it) }
                }
            }
        }
    }
}

@Composable
fun ResultPage(navController: NavHostController, word:String, info:Map<String,Any>){
    Scaffold(topBar ={
        TopBar(navController, str = "-1", Favorite = Favorite(true,word))
    }){
        Column(
            Modifier
                .fillMaxSize()
                .background(color = MyGray)) {//背景色
            /**
             * 关键词
             */
            KeyWord(word = word)

            /**
             * 导航条
             */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                val listKey:List<String> = info["attr_list"] as List<String>
                var state by remember { mutableStateOf(0) }
                Column {
                    ScrollableTabRow(
                        selectedTabIndex = state,
                        modifier = Modifier
                            .wrapContentWidth()
                            .shadow(5.dp),
                        edgePadding = 16.dp,
                        backgroundColor = MaterialTheme.colors.background,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                //使用系统默认的
                                modifier = Modifier.tabIndicatorOffset(tabPositions[state]),
                                //选中下划线的颜色
                                color = MyOrange
                            )
                        }) {
                        listKey.forEachIndexed { index, title ->
                            Tab(
                                modifier = Modifier.pressClickEffect(),
                                text = {
                                    Text(
                                        title,
                                        fontSize = 15.sp,
                                        color = if (state == index) MyOrange else MyDeepGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                selected = state == index,
                                onClick = { state = index },
                                unselectedContentColor = MaterialTheme.colors.background,
                                selectedContentColor = MaterialTheme.colors.background
                            )
                        }
                    }
                    val attr_info:Map<String,Any> = info[listKey[state]] as Map<String, Any>
                    //TODO
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)
                                .shadow(5.dp, RoundedCornerShape(10.dp))
                                .background(
                                    MaterialTheme.colors.background,
                                    RoundedCornerShape(10.dp)
                                )
                                .verticalScroll(
                                    state = rememberScrollState()
                                )) {
                            SelectionContainer {
                                Text(
                                    text = attr_info["value"].toString(),
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                        /**
                         * 关联词条
                         */
                        val la:List<String> =attr_info["relev"] as List<String>
                        VerticalButtonList(navController, la)
                    }
                }
            }
        }
    }
}

@Composable
fun ColorfulColum(){
    Column(
        modifier = Modifier
            .width(8.dp)
            .fillMaxHeight()
    ) {
        Spacer(modifier= Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(color = MyRed))
        Spacer(modifier= Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(color = MyYellow))
        Spacer(modifier= Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(color = MyQing))
    }
}