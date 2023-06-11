package com.example.omniscient

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.omniscient.config.RouteConfig
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.omniscient.config.CreateNewWordTemp
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.ParamsConfig
import com.example.omniscient.page.*
import com.example.omniscient.ui.theme.MyGray
import com.example.omniscient.ui.theme.MyOrange
import com.example.omniscient.ui.theme.MyRed
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataConfig.file= this.getSharedPreferences("data", Context.MODE_PRIVATE)
        DataConfig.fileEdit=DataConfig.file.edit()
        DataConfig.init()

        setContent {
            val navController=rememberNavController()
            /*沉浸式状态栏*/
            ProvideWindowInsets {
                SideEffect{
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                }
                rememberSystemUiController().setStatusBarColor(
                    Color.Transparent, darkIcons = MaterialTheme.colors.isLight)
                Surface(color= MaterialTheme.colors.background) {
                    Column(
                        Modifier//底部导航栏适配
                    ) {
                        Spacer(modifier = Modifier
                            .statusBarsHeight()
                            .fillMaxWidth())

                        MainNavHost(navController)
                        //testPage()
                    }
                }
            }
        }
    }


    @Composable
    fun MainNavHost(navController: NavHostController){
        NavHost(
            navController= navController,
            startDestination= RouteConfig.ROUTE_HomePage,
        ){
            composable(
                route= RouteConfig.ROUTE_HomePage
                ){
                homePage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_LexiconPage,
            ){
                lexiconPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_PersonalCenterPage,
            ){
                personalCenterPage(navController)
            }
            composable(
                route = "${RouteConfig.ROUTE_ResultPage}/{${ParamsConfig.PARAMS_Word}}",
                arguments = listOf(
                    navArgument(ParamsConfig.PARAMS_Word) { type = NavType.StringType}
                )

            ){
                val argument = requireNotNull(it.arguments)
                val word=argument.getString(ParamsConfig.PARAMS_Word)
                ShowPage(navController, word!!)
            }
            composable(
                route = RouteConfig.ROUTE_SearchPage,
            ){
                SearchPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_LoginPage,
            ){
                loginPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_ProfilePage,
            ){
                profileEditingPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_FeedbackPage,
            ){
                FeedbackPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_AboutPage,
            ){
                AboutPage(navController)
            }
            composable(
                route = RouteConfig.ROUTE_NetError,
            ){
                ErrorPage(navController,"网络错误")
            }
            composable(
                route = RouteConfig.ROUTE_NoLoginError,
            ){
                ErrorPage(navController,"登录以使用更多功能")
            }
            composable(
                route = RouteConfig.ROUTE_AddRel,
            ){
                addRel(navController = navController)
            }
            composable(
                route = RouteConfig.ROUTE_InputAttr +"?${ParamsConfig.PARAMS_Word}={${ParamsConfig.PARAMS_Word}}"
                +"?num={num}",
                arguments = listOf(
                    navArgument(ParamsConfig.PARAMS_Word) { type = NavType.StringType;defaultValue=""},
                    navArgument("num") { type = androidx.navigation.NavType.IntType;defaultValue=0}
                )

            ){
                val argument = requireNotNull(it.arguments)
                val word=argument.getString(ParamsConfig.PARAMS_Word)
                val n=argument.getInt("num")
                inputAttr(navController = navController, word.toString(),n)
            }
            composable(
                route = RouteConfig.ROUTE_InputInfo,
            ){
                inputInfo(navController = navController)
            }
            composable(
                route = RouteConfig.ROUTE_InputRel,
            ){
                inputRele(navController = navController)
            }
        }
    }

}
