package com.example.omniscient.page

import android.text.InputType
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.TextInput
import com.example.omniscient.composable.TopBar
import com.example.omniscient.composable.addAlterDialog
import com.example.omniscient.composable.inputType
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.AccountJudge
import com.example.omniscient.unit.PasswordJudge
import com.example.omniscient.unit.Repository
import com.example.omniscient.util.INFO
import com.example.omniscient.util.trans
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun loginPage(navController: NavHostController) {
    val flag= remember {
        mutableStateOf(true)
    }
    BackHandler {
        //TODO:拦截物理返回键
        if(flag.value) navController.popBackStack()
        else flag.value=true
    }

    ProvideWindowInsets() {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            modifier=Modifier.navigationBarsWithImePadding(),
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(
                    navController = navController,
                    str = if(flag.value) "登录" else "注册",
                    HomeButtonPosAtStart = false,
                    backArrowClickEvent = {if(flag.value) navController.popBackStack()
                    else flag.value=true}
                )
            },
            snackbarHost = {
                SnackbarHost(it) { data ->
                    if(data.message=="登录成功"||data.message=="注册成功"){
                        Box(
                            modifier = Modifier.fillMaxSize().padding(top=60.dp).clickable(
                                onClick = {},
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier
                                .size(140.dp, 140.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xDDA0A0A0), RoundedCornerShape(10.dp)),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done, contentDescription = null,
                                    tint= MyQing,modifier=Modifier.size(100.dp),
                                )
                                Text(
                                    text = data.message,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    color= MyWhite,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }
                        }
                    }else{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Row(modifier= Modifier
                                .padding(top = 75.dp)
                                .shadow(5.dp, RoundedCornerShape(10.dp))
                                .background(
                                    MaterialTheme.colors.background,
                                    RoundedCornerShape(10.dp)
                                )) {
                                Text(modifier= Modifier
                                    .padding(vertical = 10.dp, horizontal = 25.dp),
                                    text = data.message, color = MyDeepGray)
                            }
                        }
                    }
                }
            }
        ) {
            val passwordFocusRequester = FocusRequester()
            val focusManager: FocusManager = LocalFocusManager.current

            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF7B25E),
                    Color(0xFFF880E4)
                )
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(gradient),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    if(flag.value) "Login" else "Register",
                    modifier = Modifier
                        .padding(bottom = 550.dp),
                    fontSize = 100.sp,
                    //fontStyle= FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    color = MyWhite
                )

                Column(
                    Modifier
                        .height(if(flag.value) 450.dp else 500.dp)
                        .fillMaxWidth()
                        .shadow(5.dp, RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                        .background(
                            MaterialTheme.colors.background,
                            RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)
                        )
                        .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            alignment = Alignment.Bottom
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(flag.value)
                            loginEvent(navController = navController,flag,scaffoldState,scope)
                        else
                            registerEvent(navController = navController,flag,scaffoldState,scope)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun loginEvent(navController: NavHostController,loginOrRegister:MutableState<Boolean>,scaffoldState:ScaffoldState,scope: CoroutineScope){
    val keyboard = LocalSoftwareKeyboardController.current
    val passwordFocusRequester=FocusRequester()
    val focusManager:FocusManager = LocalFocusManager.current
    val account=remember { mutableStateOf("") }
    val password=remember { mutableStateOf("") }
    val accountError:Boolean= AccountJudge(account.value)
    val passwordError:Boolean= PasswordJudge(password.value)
    val flag = remember {
        mutableStateOf(false)
    }
    TextInput(s=account,inputType = inputType.Name, keyboardActions = KeyboardActions(onNext={
        passwordFocusRequester.requestFocus()
    }), isError = accountError)
    TextInput(s=password,inputType = inputType.Password, keyboardActions = KeyboardActions(onDone={

        focusManager.clearFocus()
        if(!(accountError||passwordError)) flag.value=true
        else{
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
            }
        }
        keyboard?.hide()
    }), focusRequester = passwordFocusRequester, isError = passwordError)


    //addAlterDialog(openDialog = openDialog, s = "登录")
    val info = if(flag.value) trans<Boolean>{login(account.value,password.value)} else null
    info?.run {
        info.hasError.run{
            if(info.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
            else{
                info.value?.run {
                    if(this){
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("登录成功")
                            navController.popBackStack()
                        }

                    }
                    else{
                        scope.launch {
                            flag.value=false
                            scaffoldState.snackbarHostState.showSnackbar("登陆失败,请检查账号密码是否正确")
                        }
                    }
                }
            }
        }
    }
    Button(
        onClick = {
            /*TODO*/
            if(account.value!=""&&password.value!="") flag.value=true
            else{
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("请输入信息")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MyOrange,
            contentColor = MaterialTheme.colors.background,
            disabledBackgroundColor = MyLightGray,
            disabledContentColor = MyBlack
        ),
        enabled = !(accountError||passwordError)||(account.value==""&&password.value=="")
    ) {
        Text(text = "登录", modifier = Modifier.padding(vertical = 8.dp))
    }
    Divider(
        color= MyDeepGray.copy(alpha = 0.3f),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 20.dp),
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "没有账号?", color = MyDeepGray)
        TextButton(onClick = { loginOrRegister.value=false }) {
            Text(text = "注册",color= MyOrange)
        }
    }

}
