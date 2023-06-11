package com.example.omniscient.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.R
import com.example.omniscient.ui.theme.MyBlack
import com.example.omniscient.ui.theme.MyGray
import com.example.omniscient.ui.theme.MyOrange
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun MainBottomNavigation(navController: NavHostController){
    BottomNavigation(
        Modifier.navigationBarsPadding()
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp
    ) {
        val backstackEntry = navController.currentBackStackEntryAsState()
        //获取当前的路由状态
        val route = backstackEntry.value?.destination?.route
        BottomNavigationItem(
            selected = (route==RouteConfig.ROUTE_HomePage),
            onClick = {
                navController.navigate(RouteConfig.ROUTE_HomePage){popUpTo(RouteConfig.ROUTE_HomePage);launchSingleTop = true}
            },
            modifier = Modifier.padding(5.dp),
            icon={
                var temp=R.drawable.home_logo_not_selected
                if(route==RouteConfig.ROUTE_HomePage) {
                    temp=R.drawable.home_logo_selected
                }
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(temp),
                    contentDescription = RouteConfig.ROUTE_HomePage,
                )
            },
            label = {
                var color_temp= MyBlack
                if(route==RouteConfig.ROUTE_HomePage){
                    color_temp= MyOrange
                }
                Text(
                    text = "首页",
                    color = color_temp,
                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        )

        BottomNavigationItem(
            selected = (route==RouteConfig.ROUTE_LexiconPage),
            onClick = {
                navController.navigate(RouteConfig.ROUTE_LexiconPage){popUpTo(RouteConfig.ROUTE_HomePage);}
            },
            modifier = Modifier.padding(5.dp),
            icon={
                var temp=R.drawable.ciku_logo_not_selected
                if(route==RouteConfig.ROUTE_LexiconPage) {
                    temp=R.drawable.ciku_logo_selected
                }
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(temp),
                    contentDescription = RouteConfig.ROUTE_HomePage,
                )
            },
            label = {
                var color_temp= MyBlack
                if(route==RouteConfig.ROUTE_LexiconPage){
                    color_temp= MyOrange
                }
                Text(
                    text = "词库",
                    color = color_temp,
                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        )

        BottomNavigationItem(
            selected = (route==RouteConfig.ROUTE_PersonalCenterPage),
            onClick = {
                navController.navigate(RouteConfig.ROUTE_PersonalCenterPage){popUpTo(RouteConfig.ROUTE_HomePage);}
            },
            modifier = Modifier.padding(5.dp),
            icon={
                var temp=R.drawable.personal_center_logo_not_selected
                if(route==RouteConfig.ROUTE_PersonalCenterPage) {
                    temp=R.drawable.personal_center_logo_selected
                }
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(temp),
                    contentDescription = RouteConfig.ROUTE_HomePage,
                )
            },
            label = {
                var color_temp= MyBlack
                if(route==RouteConfig.ROUTE_PersonalCenterPage){
                    color_temp= MyOrange
                }
                Text(
                    text = "我的",
                    color = color_temp,
                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        )
    }
}