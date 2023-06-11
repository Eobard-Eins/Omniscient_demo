package com.example.omniscient.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.TopBar
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.MyDeepGray
import com.example.omniscient.ui.theme.MyGray
import com.example.omniscient.ui.theme.MyOrange

@Composable
fun FeedbackPage(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var showFailDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                str = "意见反馈",
                HomeButtonPosAtStart = false
            )
        },
        content = {
            if(DataConfig.Login){
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    var textValue by remember{ mutableStateOf("") }
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = {textValue=it},
                        label = { Text(text = "请输入您的意见或建议") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            focusedBorderColor = MyOrange,
                            unfocusedBorderColor = MyDeepGray,
                            cursorColor = MyOrange,
                            focusedLabelColor = MyOrange,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (textValue.isNotBlank()) {
                                showDialog = true
                            } else {
                                showFailDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .padding(start = 20.dp, end = 20.dp)
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MyOrange,
                            contentColor = MaterialTheme.colors.background
                        ),
                        contentPadding = PaddingValues(50.dp,8.dp)
                    ) {
                        Text(text = "提交", fontSize = 18.sp)
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
                        Text(text = "登录以使用更多功能", color = MyDeepGray)
                        TextButton(onClick = { navController.navigate(RouteConfig.ROUTE_LoginPage) }) {
                            Text("登录",color= MyOrange)
                        }
                    }

                }
            }

        }
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "提交成功") },
            text = { Text(text = "谢谢您的反馈!") },
            confirmButton = {
                TextButton(onClick = { showDialog = false },) {
                    Text(text = "确定",color= MyOrange)
                }
            }
        )
    }
    if (showFailDialog) {
        AlertDialog(
            onDismissRequest = { showFailDialog = false },
            title = { Text(text = "提交失败") },
            text = { Text(text = "请输入内容。") },
            confirmButton = {
                TextButton(onClick = { showFailDialog = false },
                    ) {
                    Text(text = "确定",color= MyOrange)
                }
            }
        )
    }
}