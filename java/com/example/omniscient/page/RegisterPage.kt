package com.example.omniscient.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.composable.TextInput
import com.example.omniscient.composable.TopBar
import com.example.omniscient.composable.inputType
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.AccountJudge
import com.example.omniscient.unit.PasswordJudge
import com.example.omniscient.unit.UsernameJudge
import com.example.omniscient.util.trans
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun registerEvent(navController: NavHostController,loginOrRegister:MutableState<Boolean>,scaffoldState:ScaffoldState,scope: CoroutineScope){
    val keyboard = LocalSoftwareKeyboardController.current
    val passwordFocusRequester= FocusRequester()
    val accountFocusRequester=FocusRequester()
    val passwordAgainFocusRequester=FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current
    val username= remember { mutableStateOf("") }
    val account= remember { mutableStateOf("") }
    val password= remember { mutableStateOf("") }
    val passwordAgain= remember { mutableStateOf("") }
    val usernameError:Boolean= UsernameJudge(username.value)
    val accountError:Boolean= AccountJudge(account.value)
    val passwordError:Boolean= PasswordJudge(password.value)
    val passwordAgainError:Boolean= PasswordJudge(passwordAgain.value)
    val flag = remember {
        mutableStateOf(false)
    }
    TextInput(s=username,inputType = inputType.UserName, keyboardActions = KeyboardActions(onNext={
        accountFocusRequester.requestFocus()
    }), isError = usernameError)

    TextInput(s=account,inputType = inputType.Name, keyboardActions = KeyboardActions(onNext={
        passwordFocusRequester.requestFocus()
    }), focusRequester = accountFocusRequester, isError = accountError)

    TextInput(s=password,inputType = inputType.Password_r, keyboardActions = KeyboardActions(onNext={
        passwordAgainFocusRequester.requestFocus()
    }), focusRequester = passwordFocusRequester, isError = passwordError)

    TextInput(s=passwordAgain,inputType = inputType.PasswordAgain, keyboardActions = KeyboardActions(onDone={

        focusManager.clearFocus()
        if(!(accountError||passwordError||passwordAgainError||usernameError)) flag.value=true
        else{
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
            }
        }
        keyboard?.hide()
    }), focusRequester = passwordAgainFocusRequester, isError = passwordAgainError)

    val info = if(flag.value) trans<String>{ register(account.value,password.value,username.value) } else null
    info?.run {
        info.hasError.run{
            if(info.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
            else{
                info.value?.run {
                    if(this=="error"){
                        scope.launch {
                            flag.value=false
                            scaffoldState.snackbarHostState.showSnackbar("注册失败,请稍后重试")

                        }
                    }else if(this=="账号不唯一"){
                        scope.launch {
                            flag.value=false
                            scaffoldState.snackbarHostState.showSnackbar("注册失败,账号不唯一")

                        }
                    } else if(password.value!=passwordAgain.value){
                        scope.launch {
                            flag.value=false
                            scaffoldState.snackbarHostState.showSnackbar("注册失败,请检查重复输入密码是否正确")

                        }
                    }else{
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("注册成功")
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
    Button(
        onClick = {
            /*TODO*/
            if(account.value!=""&&password.value!=""&&username.value!=""&&passwordAgain.value!="")
                flag.value=true
            else{
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("请输入信息")
                }
            }
        },
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MyOrange,
            contentColor = MaterialTheme.colors.background,
            disabledBackgroundColor = MyLightGray,
            disabledContentColor = MyBlack
        ),
        enabled = !(usernameError||accountError||passwordError||passwordAgainError)||
                (account.value==""&&password.value==""&&username.value==""&&passwordAgain.value=="")
    ) {
        Text(text = "注册", modifier = Modifier.padding(vertical = 8.dp))
    }
}