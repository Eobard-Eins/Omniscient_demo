package com.example.omniscient.page

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.MainBottomNavigation
import com.example.omniscient.composable.NoRippleInteractionSource
import com.example.omniscient.composable.TopBar
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun personalCenterPage(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(navController = navController, str = "我的", HomeButtonPosAtStart = false) },
        bottomBar ={
        MainBottomNavigation(navController)
    }) {
        val Login= remember {
            mutableStateOf(DataConfig.Login)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MyGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp)
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp))
            ) {
                Row(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(1f)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(0.5.dp, MyDeepGray, CircleShape),
                            painter = painterResource(
                                if(Login.value) DataConfig.Avatar else R.drawable.users
                            ),
                            contentDescription = "头像"
                        )
                        Text(
                            text = if(Login.value) DataConfig.UserName else "未登录",
                            color=if(Login.value) MyBlack else MyDeepGray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 15.dp,top=6.dp),
                            fontSize = 20.sp
                        )
                    }
                    Button(
                        enabled = Login.value,
                        onClick = { navController.navigate(RouteConfig.ROUTE_ProfilePage) },
                        modifier = Modifier
                            .padding(16.dp)
                            .height(30.dp),
                        border=BorderStroke(1.dp,color = MyDeepGray),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MyDeepGray,
                            disabledBackgroundColor = MaterialTheme.colors.background,
                        ),
                    ) {
                        Text(text = "编辑资料",
                            fontSize = 12.sp,
                            color = MyDeepGray,
                            textAlign = TextAlign.Center,
                            maxLines = 1,)
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp))
            ) {
                data class personalButton(
                    val draw:Int,
                    val Route:String,
                    val text:String
                )
                val drawList= listOf(
                    personalButton(R.drawable.add,if(DataConfig.Login) RouteConfig.ROUTE_InputAttr else RouteConfig.ROUTE_NoLoginError,"添加词条"),
                    personalButton(R.drawable.add,if(DataConfig.Login) RouteConfig.ROUTE_AddRel else RouteConfig.ROUTE_NoLoginError,"添加关联词条"),
                    personalButton(R.drawable.feedback,RouteConfig.ROUTE_FeedbackPage,"意见反馈"),
                    personalButton(R.drawable.about,RouteConfig.ROUTE_AboutPage,"关于我们"),
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp)
                ){
                    items(drawList){
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(top = 3.dp, bottom = 3.dp),
                            onClick = {
                                navController.navigate(it.Route)
                            } ,
                            shape = RoundedCornerShape(0.dp),
                            elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp,0.dp,0.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background
                            ),
                            interactionSource = remember { NoRippleInteractionSource() }
                        ) {
                            Image(painter = painterResource(it.draw),
                                contentDescription = it.text,
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(21.dp)
                            )
                            Text(text = it.text, color = MyBlack,fontSize = 18.sp)
                            Image(painter = painterResource(R.drawable.arrow_right1),
                                contentDescription = "right",
                                modifier = Modifier.fillMaxWidth(),
                                alignment = Alignment.CenterEnd
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if(DataConfig.Login){
                        DataConfig.clear()

                        Login.value=false
                    }else{
                        navController.navigate(RouteConfig.ROUTE_LoginPage)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .padding(top = 30.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MyOrange,
                    contentColor = MaterialTheme.colors.background
                ),
                contentPadding = PaddingValues(50.dp,8.dp)
            ) {
                val str:String=if(Login.value) "退出登录" else "登录"
                Text(text = str,fontSize = 18.sp)
            }
        }
    }
}