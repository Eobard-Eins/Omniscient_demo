package com.example.omniscient.page

import android.os.Debug
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.omniscient.composable.MainBottomNavigation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omniscient.R
import com.example.omniscient.composable.TopBar
import com.example.omniscient.composable.inputType
import com.example.omniscient.config.CreateNewWordTemp
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.config.CreateNewWordTemp.AttrList
import com.example.omniscient.config.CreateNewWordTemp.Clear
import com.example.omniscient.config.CreateNewWordTemp.numProperties
import com.example.omniscient.ui.theme.*
import com.example.omniscient.unit.WordNameJudge
import com.example.omniscient.util.trans
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun inputAttr(navController: NavHostController,init:String,k:Int) {
    Log.d("test",init)
    if(!DataConfig.Login) navController.navigate(RouteConfig.ROUTE_NoLoginError)

    val keyboard = LocalSoftwareKeyboardController.current
    CreateNewWordTemp.AttrToInfo.clear()
    CreateNewWordTemp.AttrToRele.clear()

    var NumProperties by remember {
        mutableStateOf(init)
    }

    numProperties=NumProperties
    val attrList= mutableListOf<String>()
    var currentPage by remember { mutableStateOf(k) }
    var totalPage by remember { mutableStateOf(1) }
    // 定义属性值的可变状态列表
    //val propertyValues = remember { mutableStateListOf<String>() }
    val propertyValuesSec = remember { mutableStateListOf<String>() }
    var currentInputText by remember { mutableStateOf("") } // 定义一个变量保存当前输入框中的文本

    val inDataBaseEvent= remember {
        mutableStateOf(false)
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar ={ TopBar(navController = navController, str = "添加词条", HomeButtonPosAtStart = false) },
        backgroundColor = MyGray,
        snackbarHost = {
            SnackbarHost(it) { data ->
                if(data.message=="indatabase"){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp)
                            .clickable(
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
                                imageVector = Icons.Default.Info, contentDescription = null,
                                tint= Color(0xFFEE5454),modifier= Modifier
                                    .size(90.dp)
                                    .padding(top = 10.dp),
                            )
                            Text(
                                text = "词条已存在",
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                color= MyWhite,
                                modifier = Modifier.padding(top=10.dp,bottom = 5.dp)
                            )
                        }
                    }
                }
                else{
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
        val inDataBase = if(inDataBaseEvent.value) trans<Boolean>{ isSearched(NumProperties) } else null
        inDataBase?.run {
            if(inDataBase.hasError){
                navController.navigate(RouteConfig.ROUTE_NetError)
            } else if(inDataBase.value == true){
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("indatabase")
                    navController.popBackStack()
                }
                //
            }
        }

        val empty_word=remember{ mutableStateOf(false) }
        val wordError:Boolean= WordNameJudge(NumProperties)
        if (currentPage == 0 ) {

            // Show dialog to input number of properties
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text("添加词条")
                },
                text = {

                    Column (){
                        TextField(
                            modifier=Modifier,
                            value = NumProperties,
                            onValueChange = {value ->
                                // 将输入内容存储为字符串
                                val filteredValue = value.filter { it != ' ' && it != '\n' }.toString()
                                NumProperties = filteredValue
                            },
                            label = { Text(text = "请输入新的词条",) },
                            shape = RoundedCornerShape(10.dp),
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
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions= KeyboardActions(onDone = {
                                //TODO

                                if (NumProperties.isEmpty()) {
                                    empty_word.value=true
                                } else {
                                    // 点击确认按钮时，将输入内容存储为字符串
                                    totalPage = if ((NumProperties.toIntOrNull() ?: 0) > 0) NumProperties.toInt() else 1
                                    currentPage++
                                    inDataBaseEvent.value=true
                                }
                                keyboard?.hide()
                            }),
                            isError = (wordError&&NumProperties!="")||empty_word.value
                        )
                        Text(
                            text = if(empty_word.value) "请输入词条" else if(wordError&&NumProperties!="") "词条长度应在1~8位之间" else "",
                            modifier = Modifier
                                .padding(start = 10.dp, top = 3.dp)
                                .fillMaxWidth(),
                            fontSize = 12.sp,
                            color = MyRed,
                            textAlign = TextAlign.Left
                        )
                    }/*
                    TextField(
                        maxLines=1,
                        modifier = Modifier.fillMaxWidth(),
                        value = numProperties,
                        onValueChange = { value ->
                            // 将输入内容存储为字符串
                            val filteredValue = value.filter { it != ' ' && it != '\n' }.toString()
                            numProperties = filteredValue
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )*/
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (NumProperties.isEmpty()) {
                                empty_word.value=true
                            } else {
                                // 点击确认按钮时，将输入内容存储为字符串
                                totalPage = if ((NumProperties.toIntOrNull() ?: 0) > 0) NumProperties.toInt() else 1
                                currentPage++
                                inDataBaseEvent.value=true
                            }
                        },
                    ) {
                        Text("确定",color= MyOrange)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {//TODO
                            currentPage =1
                            navController.popBackStack()
                        },
                    ) {
                        Text("取消",color= MyDeepGray)
                    }
                }
            )


        }

        else if (currentPage == 1) {

            // Show entry form for current property
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Property title and page number
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("请输入 $NumProperties 的属性", modifier = Modifier.padding(5.dp,3.dp))
                }
                Spacer(modifier = Modifier.height(15.dp))

                // Property name input box
                // Use a Box with padding and shape to wrap the TextField
                Row(
                    modifier = Modifier
                        .padding(start = 0.dp, top = 0.dp, bottom = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(10.dp))
                            .weight(3f)
                            .height(55.dp)
                            .padding(top = 0.dp, start = 0.dp),
                    ) {
                        // Use a Column to arrange the elements vertically
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(top = 0.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            /*Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "属性")
                        }
                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)) {
                            val brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f), Color.Black, Color.Black.copy(alpha = 0.5f), Color.Transparent),
                                startX = 0f,
                                endX = size.width
                            )
                            drawLine(
                                brush = brush,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f)
                            )
                        }*/
                            // 将 TextField 中的 value 定义为可变状态，并在 onValueChange 方法中更新对应位置上的值
                            Row(
                                Modifier
                                    .shadow(5.dp, RoundedCornerShape(10.dp))
                                    .background(Color.White, RoundedCornerShape(10.dp))) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(start = 0.dp, end = 0.dp),
                                    value = currentInputText,
                                    onValueChange = {value ->
                                        val filteredValue = value.filter { it != ' ' && it != '\n' }.toString()
                                        currentInputText = filteredValue
                                    },
                                    label = { Text(text = "请输入属性",) },
                                    shape = RoundedCornerShape(10.dp),
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
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions= KeyboardActions(onDone = {
                                        //keyboard?.hide()
                                        //TODO
                                        if (currentInputText.isNotEmpty() && currentInputText !in attrList) {
                                            attrList.add(currentInputText)
                                            propertyValuesSec.add(currentInputText)
                                            currentInputText = ""
                                        }
                                        else {
                                            scope.launch {
                                                scaffoldState.snackbarHostState.showSnackbar("添加的属性重复或为空")
                                            }
                                        }
                                    }),
                                    //isError = wordError&&newEntryText!=""
                                )
                            }
                            /*
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .padding(start = 10.dp, end = 10.dp, top = 0.dp),
                                value = currentInputText,
                                onValueChange = { value ->
                                    val filteredValue = value.filter { it != ' ' && it != '\n' }.toString()
                                    currentInputText = filteredValue
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = Color(0xFF51FF4B),
                                    unfocusedIndicatorColor = Color.Yellow,
                                    disabledTextColor = Color.Gray,
                                    cursorColor = Color(0xff1a73e8),
                                ),
                                // Use a placeholder to show the text in the center of the TextField
                                placeholder = { Text("请输入属性", textAlign = TextAlign.Center) }
                            )*/
                        }
                    }
                    Spacer(Modifier.width(5.dp))
                    Button(
                        onClick = {
                            if (currentInputText.isNotEmpty() && currentInputText !in attrList) {
                                attrList.add(currentInputText)
                                propertyValuesSec.add(currentInputText)
                                currentInputText = ""
                            }
                            else {
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("添加的属性重复或为空")
                                }
                            }
                        },
                        modifier = Modifier
                            .height(55.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevation(5.dp)
                    ) {
                        Text("确认")
                    }


                }
                Spacer(modifier = Modifier.height(10.dp))
                // Property details input box
                // Use a Box with padding and shape to wrap the TextField
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(top = 0.dp)
                        .shadow(5.dp, RoundedCornerShape(10.dp))
                        .background(Color.White, RoundedCornerShape(10.dp)),
                    //contentAlignment = Alignment.Center
                ) {
                    // Use a Column to arrange the elements vertically
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .padding(top = 0.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "$NumProperties", fontSize = 18.sp)
                        }
                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)) {
                            val brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f), Color.Black, Color.Black.copy(alpha = 0.5f), Color.Transparent),
                                startX = 0f,
                                endX = size.width
                            )
                            drawLine(
                                brush = brush,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(270.dp)
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                FlowRow(mainAxisSpacing = 10.dp) {
                                    propertyValuesSec.forEach { entry ->
                                        EntryButton1(entry = entry, onDelete = {
                                            attrList.remove(entry)
                                            propertyValuesSec.remove(entry)
                                        })
                                    }
                                }

                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (propertyValuesSec.isEmpty()) {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("请添加至少一个属性")
                            }
                        } else {
                            // TODO: 添加点击事件的具体实现
                            AttrList.clear()
                            attrList.forEach { AttrList.add(it) }

                            navController.navigate(RouteConfig.ROUTE_InputInfo){navController.popBackStack()}

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MyOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "下一步")
                }


            }
        }
    }
}

@Composable
fun EntryButton1(
    entry: String,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Button(
        onClick = {
            showDeleteDialog = true
        },
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xDFFD9696),
            contentColor = Color.White
        ),
    ) {
        Text(entry)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("是否删除此属性？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("确定",color= MyOrange)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("取消",color= MyDeepGray)
                }
            }
        )
    }
}