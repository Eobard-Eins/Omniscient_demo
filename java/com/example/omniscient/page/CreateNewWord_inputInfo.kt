package com.example.omniscient.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.TopBar
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.config.CreateNewWordTemp.AttrList
import com.example.omniscient.config.CreateNewWordTemp.AttrToInfo
import com.example.omniscient.config.CreateNewWordTemp.numProperties
import com.example.omniscient.ui.theme.*
import com.example.omniscient.util.trans
import kotlinx.coroutines.launch

/*
设置属性对应信息的页面 2
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun inputInfo(navController: NavHostController){
    val keyboard = LocalSoftwareKeyboardController.current
    val word:String= numProperties
    var currentInputText by remember { mutableStateOf("") }
    val numProperties = AttrList.size // 假设有 3 个属性
    val listKey:List<String> = AttrList  //属性列表
    var state by remember { mutableStateOf(0) }

    // 使用键值对类型的可变状态变量保存详细信息
    val detailsMap = remember { mutableStateMapOf<String, String>() }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val addWordEvent=remember{ mutableStateOf(false) }
    val addWordInfo=if(addWordEvent.value) {
        trans<Boolean> {
            addWordInfo(word, attrList = AttrList, AttrToInfo)
        }
    } else null

    addWordInfo?.run {
        if(addWordInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
        else{
            addWordInfo.value?.run {
                if(this){
                    //
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("操作成功")
                        navController.navigate(RouteConfig.ROUTE_InputRel){navController.popBackStack()}
                    }
                    addWordEvent.value=false
                }else{
                    //info.value="2"
                    addWordEvent.value=false
                }

            }
        }

    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar ={ TopBar(navController =navController, str = "添加 '$word' 的详细信息", HomeButtonPosAtStart = false) },
        backgroundColor = MyGray,
        snackbarHost = {
            SnackbarHost(it) { data ->
                if(data.message=="操作成功"){
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

        var currentInputText by remember { mutableStateOf("") } // 定义一个变量保存当前输入框中的文本



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(top = 0.dp)
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp))
                //contentAlignment = Alignment.Center
            ) {
                Column() {
                    ScrollableTabRow(
                        selectedTabIndex = state,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(10.dp))
                            .wrapContentWidth()
                            .padding(top = 0.dp)
                            .shadow(2.dp),
                        edgePadding = 16.dp,
                        backgroundColor = MaterialTheme.colors.background,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                //使用系统默认的
                                modifier = Modifier.tabIndicatorOffset(tabPositions[state]),
                                //选中下划线的颜色
                                color = MyOrange
                            )
                        }) {
                        listKey.forEachIndexed { index, title ->
                            Tab(
                                //  modifier = Modifier.pressClickEffect(),
                                text = {
                                    Text(
                                        title,
                                        fontSize = 15.sp,
                                        color = if (state == index) MyOrange else MyDeepGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                selected = state == index,
                                onClick = {
                                    // 切换选中状态时，将当前输入框中的文本保存到 detailsMap 中相应属性名称对应的值中
                                    detailsMap[listKey[state]] = currentInputText
                                    AttrToInfo[listKey[state]] = currentInputText
                                    state = index
                                    // 切换到对应属性的详细信息
                                    currentInputText = detailsMap.getOrDefault(title, "")
                                },
                                unselectedContentColor = MaterialTheme.colors.background,
                                selectedContentColor = MaterialTheme.colors.background
                            )
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)
                        .padding(start = 10.dp, end = 10.dp, top = 55.dp, bottom = 10.dp),
                    value = currentInputText,
                    onValueChange = { value ->
                        currentInputText = value
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = MyLightOrange,
                        unfocusedIndicatorColor = MyMi,
                        disabledTextColor = Color.Gray,
                        cursorColor = MyDeepGray,
                        focusedLabelColor = MyLightOrange
                    ),
                    // Use a placeholder to show the text in the center of the TextField
                    label = { Text("请输入详细信息", textAlign = TextAlign.Center) },
                    keyboardOptions = KeyboardOptions(imeAction = if(state==listKey.size-1) ImeAction.Done else ImeAction.Next),
                    keyboardActions= KeyboardActions(onDone = {
                        //TODO
                        detailsMap[listKey[state]] = currentInputText
                        AttrToInfo[listKey[state]] = currentInputText

                        if (detailsMap.size == numProperties) {
                            // TODO: 去设置关联词条
                            addWordEvent.value=true

                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("存在未设置信息的属性")
                            }
                        }
                        keyboard?.hide()
                    }, onNext = {
                        detailsMap[listKey[state]] = currentInputText
                        AttrToInfo[listKey[state]] = currentInputText
                        state=state+1
                        currentInputText = detailsMap.getOrDefault(listKey[state], "")
                    })
                )
            }

            //val context = LocalContext.current
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    detailsMap[listKey[state]] = currentInputText
                    AttrToInfo[listKey[state]] = currentInputText
                    if (detailsMap.size == numProperties) {
                        // TODO: 去设置关联词条

                        addWordEvent.value=true

                    } else {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("存在未设置信息的属性")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MyOrange, contentColor = Color.White
                )
            ) {
                Text(text = "下一步")
            }
        }

    }

}