package com.example.omniscient.config

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.omniscient.page.ShowPage
import com.example.omniscient.page.homePage
import com.example.omniscient.page.lexiconPage
import com.example.omniscient.page.personalCenterPage

object RouteConfig {
    /**
     * home page 路由
     */
    const val ROUTE_HomePage="ROUTE_Home"

    /**
     * lexicon page 路由
     */
    const val ROUTE_LexiconPage="ROUTE_Lexicon"

    /**
     * Personal center 路由
     */
    const val ROUTE_PersonalCenterPage="ROUTE_PersonalCenter"

    /**
     * 功能页路由
     */
    const val ROUTE_ResultPage="ROUTE_SearchResultPage"

    /**
     * 搜索页
     */
    const val ROUTE_SearchPage="ROUTE_SearchPage"

    /**
     * 修改个人资料
     */
    const val ROUTE_ProfilePage="ROUTE_UserInfoSet"


    /**
     * 意见反馈
     */
    const val ROUTE_FeedbackPage="ROUTE_FeedBack"

    /**
     * 关于我们
     */
    const val ROUTE_AboutPage="ROUTE_About"

    /**
     * 登录页
     */
    const val ROUTE_LoginPage="ROUTE_Login"

    /**
     * 注册页
     */
    const val ROUTE_Register="ROUTE_Register"

    /**
     * 网络错误
     */
    const val ROUTE_NetError="ROUTE_NetError"

    const val ROUTE_AddRel="ROUTE_AddRel"

    /**
     * 未登录
     */
    const val ROUTE_NoLoginError="ROUTE_NoLoginError"

    //创建新词条
    /**
     * 输入属性
     */
    const val ROUTE_InputAttr="ROUTE_InputAttr"
    /**
     * 输入信息
     */
    const val ROUTE_InputInfo="ROUTE_InputInfo"
    /**
    * 输入关联词条
    */
    const val ROUTE_InputRel="ROUTE_InputRel"
    /**
     * temp
     */
    const val ROUTE_temp="ROUTE_temp"

}