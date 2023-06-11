package com.example.omniscient.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.MainBottomNavigation
import com.example.omniscient.composable.TopBar
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*

@Composable
fun ErrorPage(navController: NavHostController,str:String){
    Scaffold(topBar ={
        TopBar(navController,"详 情", HomeButtonPosAtStart = false)
    }){
        if(str=="网络错误"){
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Row(modifier= Modifier
                    .padding(start =20.dp, bottom = 80.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "网络错误!", color = MyDeepGray)
                    TextButton(onClick = { /*TODO*/navController.popBackStack() }) {
                        Text("刷新重试",color= MyOrange)
                    }
                }

            }
        }else if(str=="无匹配词条"){
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Row(modifier= Modifier
                    .padding(start =20.dp, bottom = 80.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "无匹配词条!", color = MyDeepGray)
                    TextButton(onClick = { /*TODO*/if(DataConfig.Login) navController.navigate(RouteConfig.ROUTE_InputAttr){navController.popBackStack()}
                    else navController.navigate(RouteConfig.ROUTE_NoLoginError){navController.popBackStack()}}) {
                        Text("新建词条",color= MyOrange)
                    }
                }
            }
        }else if(str=="登录以使用更多功能"){
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Row(modifier= Modifier
                    .padding(start =20.dp, bottom = 80.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "登录以使用更多功能", color = MyDeepGray)
                    TextButton(onClick = { navController.navigate(RouteConfig.ROUTE_LoginPage){navController.popBackStack()} }) {
                        Text("登录",color= MyOrange)
                    }
                }

            }
        }else{
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Row(modifier= Modifier
                    .padding(start =20.dp, bottom = 80.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = str, color = MyDeepGray)
                }
            }
        }

    }
}