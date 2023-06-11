package com.example.omniscient.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.TopBar
import com.example.omniscient.composable.inputType
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.AccountJudge
import com.example.omniscient.unit.PasswordJudge
import com.example.omniscient.unit.UsernameJudge
import com.example.omniscient.util.trans
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun profileEditingPage(navController: NavHostController) {
    val keyboard = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
        TopBar(
            navController = navController,
            str = "编辑资料",
            HomeButtonPosAtStart = false
        )
    },
        snackbarHost = {
            SnackbarHost(it) { data ->
                if(data.message=="修改成功"){
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                text = data.message, color = MyDeepGray)
                        }
                    }
                }
            }
        }
    ) {
        var showDialog1 by remember { mutableStateOf(false) }
        var showDialog2 by remember { mutableStateOf(false) }
        val name= remember {
            mutableStateOf(DataConfig.UserName)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MyGray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colors.background,
                        RoundedCornerShape(10.dp)
                    ),
                verticalArrangement = Arrangement.Center

            ) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                    onClick = {showDialog1 = true},
                    shape = RoundedCornerShape(10.dp,10.dp,0.dp,0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MyWhite,
                        contentColor = MaterialTheme.colors.background,
                        disabledBackgroundColor = MyGray,
                        disabledContentColor = MyBlack
                    ),
                ) {
                    Text(text = "修改昵称", color = MyBlack,fontSize = 18.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = name.value,color = MyDeepGray,fontSize = 16.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Right
                    )
                }

                Divider(
                    color= MyDeepGray.copy(alpha = 0.3f),
                    thickness = 1.dp,
                )

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                    onClick = {showDialog2 = true},
                    shape = RoundedCornerShape(0.dp,0.dp,10.dp,10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MyWhite,
                        contentColor = MaterialTheme.colors.background,
                        disabledBackgroundColor = MyGray,
                        disabledContentColor = MyBlack
                    ),
                )  {
                    Text(text = "修改密码", color = MyBlack,fontSize = 18.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left
                    )
                    Image(painter = painterResource(R.drawable.arrow_right1),
                        contentDescription = "right",
                        modifier = Modifier.weight(1f),
                        alignment = Alignment.CenterEnd
                    )
                }
            }

        }


        var rename by remember { mutableStateOf("") }
        val eventFlag1 = remember {
            mutableStateOf(false)
        }
        val usernameError:Boolean= UsernameJudge(rename)
        val info = if(eventFlag1.value) trans<Boolean>{ setUserInfo(DataConfig.ID, mapOf("username" to rename)) } else null
        info?.run {
            info.hasError.run{
                if(info.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                else{
                    info.value?.run {
                        if(this){
                            scope.launch {
                                showDialog1=false
                                name.value=rename
                                eventFlag1.value=false
                                rename=""
                                scaffoldState.snackbarHostState.showSnackbar("修改成功")
                            }
                        }else{
                            scope.launch {
                                showDialog1=false
                                eventFlag1.value=false
                                rename=""
                                scaffoldState.snackbarHostState.showSnackbar("修改失败")
                            }
                        }

                    }
                }
            }
        }
        if (showDialog1) {
            AlertDialog(
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colors.background,
                        RoundedCornerShape(10.dp)
                    ),
                onDismissRequest = { showDialog1 = false ;rename=""},
                title = { Text(text = "修改昵称", fontSize = 16.sp) },
                text = {
                    Column (){
                        TextField(
                            modifier=Modifier,
                            value = rename,
                            onValueChange = {rename = it },
                            label = { Text(text = "请输入新的昵称",) },
                            shape = RoundedCornerShape(30.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                focusedIndicatorColor = MyOrange,
                                unfocusedIndicatorColor = MyDeepGray,
                                focusedLabelColor = MyOrange,
                                cursorColor = MyDeepGray,
                                errorLabelColor = MyRed,
                                errorCursorColor = MyDeepGray,
                                errorIndicatorColor = MyRed,
                                unfocusedLabelColor = MyDeepGray
                            ),
                            singleLine = true,
                            keyboardOptions = inputType.UserName.keyboardOptions,
                            keyboardActions= KeyboardActions(onDone = {

                                //TODO
                                if(!usernameError) eventFlag1.value=true
                                else{
                                    scope.launch {
                                        showDialog1=false
                                        rename=""
                                        scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                    }
                                }
                                keyboard?.hide()
                            }),
                            isError = usernameError&&rename!=""
                        )
                        Text(
                            text = if(usernameError&&rename!="") inputType.UserName.errorInfo else "",
                            modifier = Modifier
                                .padding(start = 10.dp, top = 3.dp)
                                .fillMaxWidth(),
                            fontSize = 12.sp,
                            color = MyRed,
                            textAlign = TextAlign.Left
                        )
                    }

                },
                confirmButton = {
                    TextButton(onClick = {
                        //TODO
                        if(!usernameError) eventFlag1.value=true
                        else{
                            scope.launch {
                                showDialog1=false
                                scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                            }
                        }
                    }, enabled = !usernameError) {
                        Text(text = "确定",color= MyOrange)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { rename="";showDialog1 = false ;}) {

                        Text(text = "取消",color= MyDeepGray)
                    }
                }
            )
        }

        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        val eventFlag2 = remember {
            mutableStateOf(false)
        }
        val info2 = if(eventFlag2.value) trans<Boolean>{ setUserInfo(DataConfig.ID, mapOf("password" to newPassword)) } else null
        info2?.run {
            info2.hasError.run{
                if(info2.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                else{
                    info2.value?.run {
                        if(this){
                            scope.launch {
                                showDialog2=false
                                eventFlag2.value=false
                                oldPassword=""
                                newPassword=""
                                confirmPassword=""
                                scaffoldState.snackbarHostState.showSnackbar("修改成功")
                            }
                        }else{
                            scope.launch {
                                eventFlag2.value=false
                                showDialog2=false
                                oldPassword=""
                                newPassword=""
                                confirmPassword=""
                                scaffoldState.snackbarHostState.showSnackbar("修改失败")
                            }

                        }

                    }
                }
            }
        }
        val oldPasswordError:Boolean= PasswordJudge(oldPassword)
        val newPasswordError:Boolean= PasswordJudge(newPassword)
        val confirmPasswordError:Boolean= PasswordJudge(confirmPassword)
        if (showDialog2) {
            AlertDialog(
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colors.background,
                        RoundedCornerShape(10.dp)
                    ),
                onDismissRequest = { showDialog2 = false ;oldPassword="";newPassword="";confirmPassword=""},
                title = { Text("修改密码") },
                text = {


                    val newPasswordFocusRequester= FocusRequester()
                    val confirmPasswordFocusRequester= FocusRequester()
                    val focusManager: FocusManager = LocalFocusManager.current
                    Column {
                        Column (){
                            TextField(
                                modifier=Modifier,
                                value = oldPassword,
                                onValueChange = {oldPassword = it},
                                label = { Text(text = "请输入旧密码",) },
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = MyOrange,
                                    unfocusedIndicatorColor = MyDeepGray,
                                    focusedLabelColor = MyOrange,
                                    cursorColor = MyDeepGray,
                                    errorLabelColor = MyRed,
                                    errorCursorColor = MyDeepGray,
                                    errorIndicatorColor = MyRed,
                                    unfocusedLabelColor = MyDeepGray
                                ),
                                singleLine = true,
                                keyboardOptions = inputType.Password_r.keyboardOptions,
                                keyboardActions= KeyboardActions(onNext = {
                                    newPasswordFocusRequester.requestFocus()
                                }),
                                isError = oldPasswordError&&oldPassword!="",
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Text(
                                text = if(oldPasswordError&&oldPassword!="") inputType.Password.errorInfo else "",
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 3.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                color = MyRed,
                                textAlign = TextAlign.Left
                            )
                        }

                        Column (){
                            TextField(
                                modifier=Modifier.focusOrder(newPasswordFocusRequester),
                                value = newPassword,
                                onValueChange = {newPassword = it},
                                label = { Text(text = "请输入新密码",) },
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = MyOrange,
                                    unfocusedIndicatorColor = MyDeepGray,
                                    focusedLabelColor = MyOrange,
                                    cursorColor = MyDeepGray,
                                    errorLabelColor = MyRed,
                                    errorCursorColor = MyDeepGray,
                                    errorIndicatorColor = MyRed,
                                    unfocusedLabelColor = MyDeepGray
                                ),
                                singleLine = true,
                                keyboardOptions = inputType.Password_r.keyboardOptions,
                                keyboardActions= KeyboardActions(onNext = {
                                    confirmPasswordFocusRequester.requestFocus()
                                }),
                                isError = newPasswordError&&newPassword!="",
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Text(
                                text = if(newPasswordError&&newPassword!="") inputType.Password.errorInfo else "",
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 3.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                color = MyRed,
                                textAlign = TextAlign.Left
                            )
                        }

                        Column (){
                            TextField(
                                modifier=Modifier.focusOrder(confirmPasswordFocusRequester),
                                value = confirmPassword,
                                onValueChange = {confirmPassword = it},
                                label = { Text(text = "请重复输入新密码",) },
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = MyOrange,
                                    unfocusedIndicatorColor = MyDeepGray,
                                    focusedLabelColor = MyOrange,
                                    cursorColor = MyDeepGray,
                                    errorLabelColor = MyRed,
                                    errorCursorColor = MyDeepGray,
                                    errorIndicatorColor = MyRed,
                                    unfocusedLabelColor = MyDeepGray,
                                    disabledIndicatorColor = MyDeepGray
                                ),
                                singleLine = true,
                                keyboardOptions = inputType.Password.keyboardOptions,
                                keyboardActions= KeyboardActions(onDone = {

                                    //TODO
                                    if(!(oldPasswordError||newPasswordError||confirmPasswordError)){
                                        if(oldPassword!=DataConfig.PassWord){
                                            scope.launch {
                                                showDialog2=false
                                                scaffoldState.snackbarHostState.showSnackbar("修改失败，旧密码输入错误")
                                            }
                                        }else{
                                            if(newPassword==confirmPassword) eventFlag2.value=true
                                            else{
                                                scope.launch {
                                                    showDialog2=false
                                                    scaffoldState.snackbarHostState.showSnackbar("修改失败，两次密码输入不一致")
                                                }
                                            }
                                        }
                                    }else{
                                        scope.launch {
                                            showDialog2=false
                                            oldPassword=""
                                            newPassword=""
                                            confirmPassword=""
                                            scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                        }
                                    }
                                    keyboard?.hide()
                                }),
                                isError = confirmPasswordError&&confirmPassword!="",
                                visualTransformation = PasswordVisualTransformation(),
                                enabled = newPassword!=""
                            )
                            Text(
                                text = if(confirmPasswordError&&confirmPassword!="") inputType.PasswordAgain.errorInfo else "",
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 3.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                color = MyRed,
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        //TODO
                        if(!(oldPasswordError||newPasswordError||confirmPasswordError)){
                            if(oldPassword!=DataConfig.PassWord){
                                scope.launch {
                                    showDialog2=false
                                    scaffoldState.snackbarHostState.showSnackbar("修改失败，旧密码输入错误")
                                }
                            }else{
                                if(newPassword==confirmPassword) eventFlag2.value=true
                                else{
                                    scope.launch {
                                        showDialog2=false
                                        scaffoldState.snackbarHostState.showSnackbar("修改失败，两次密码输入不一致")
                                    }
                                }
                            }
                        }else{
                            scope.launch {
                                showDialog2=false
                                scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                            }
                        }
                    }, enabled = !(oldPasswordError||newPasswordError||confirmPasswordError)) {
                        Text(text = "确定",color= MyOrange)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {

                        oldPassword=""
                        newPassword=""
                        confirmPassword=""
                        showDialog2 = false
                    }) {

                        Text(text = "取消",color= MyDeepGray)
                    }
                }
            )
        }
    }

}