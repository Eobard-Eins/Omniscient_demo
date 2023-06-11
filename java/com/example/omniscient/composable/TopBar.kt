package com.example.omniscient.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.MyBlack
import com.example.omniscient.ui.theme.MyRed
import com.example.omniscient.unit.Repository
data class Favorite(val hasFavoriteButton: Boolean,val word:String)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    navController: NavHostController,
    str:String, HomeButtonPosAtStart:Boolean=true,
    Favorite:Favorite= Favorite(false,"-1"),
    contentColor:Color=MyBlack,
    BackGroundColor:Color=MaterialTheme.colors.background,
    backArrowClickEvent:()->Unit={navController.popBackStack()}
){

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        //标题
        title = {
            if(str == "-1"){
                SearchBarButton(navController)
            }else{
                Text(
                    modifier = Modifier
                        .fillMaxSize(0.92f)
                        .wrapContentSize(Alignment.Center),
                    text =str,
                    maxLines=1,
                    color = MyBlack,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        },

        //导航按钮(返回按钮、首页按钮)
        navigationIcon = {
            IconButton(modifier = Modifier, onClick = backArrowClickEvent) {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = "Back Key"
                )
            }
            if(HomeButtonPosAtStart){
                IconButton(onClick = {
                    //TODO 跳转首页
                    navController.navigate(RouteConfig.ROUTE_HomePage){popUpTo(RouteConfig.ROUTE_HomePage);launchSingleTop = true}
                }) {
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(R.drawable.home_key),
                        contentDescription = "Home Key"
                    )
                }
            }

        },

        //收藏按钮
        actions = {
            if(Favorite.hasFavoriteButton) {
                FavoriteDialog(navController,word = Favorite.word)
            }else if(!HomeButtonPosAtStart){
                IconButton(onClick = {
                    //TODO 跳转首页
                    navController.navigate(RouteConfig.ROUTE_HomePage){popUpTo(RouteConfig.ROUTE_HomePage);launchSingleTop = true}
                }) {
                    Image(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 0.dp, end = 5.dp),
                        painter = painterResource(R.drawable.home_key),
                        contentDescription = "Home Key"
                    )
                }
            }
        },

        //背景色
        backgroundColor = BackGroundColor,
        //内容颜色
        contentColor = contentColor,
        //底部阴影
        elevation = 5.dp
    )
}
