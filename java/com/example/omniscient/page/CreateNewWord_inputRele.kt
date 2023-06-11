package com.example.omniscient.page

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.omniscient.R
import com.example.omniscient.config.CreateNewWordTemp
import com.example.omniscient.config.CreateNewWordTemp.AttrList
import com.example.omniscient.config.CreateNewWordTemp.AttrToInfo
import com.example.omniscient.config.CreateNewWordTemp.AttrToRele
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.ParamsConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.example.omniscient.util.trans
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

/*
设置关联词条的页面 3  关联词条1_关联词条2_关联词条3
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun inputRele(navController: NavController){
    val keyboard = LocalSoftwareKeyboardController.current
    val mp= mutableMapOf<String,MutableList<String>>()
    val word:String= CreateNewWordTemp.numProperties
    var currentInputText by remember { mutableStateOf("") }
    val numProperties = CreateNewWordTemp.AttrList.size // 假设有 3 个属性
    val listKey:List<String> = remember {CreateNewWordTemp.AttrList}  //属性列表
    val attrToInfo:MutableMap<String,String> =remember {CreateNewWordTemp.AttrToInfo}
    var state by remember { mutableStateOf(0) }
    var inputText by remember { mutableStateOf("") } // 定义一个变量保存当前输入框中的文本
    val propertyValues = remember { mutableStateMapOf<String, MutableList<String>>() }
    //val propertyValues = remember(propertyValues.keys.hashCode()) { mutableStateMapOf<String, MutableList<String>>() }
    var transport  =  remember  { mutableStateMapOf<String,String>() }
    var ttemp:String=""

    // 使用键值对类型的可变状态变量保存详细信息
    val detailsMap = remember { mutableStateMapOf<String, String>() }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                //标题
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxSize(0.92f)
                            .wrapContentSize(Alignment.Center),
                        text ="添加 '$word' 的关联词条",
                        maxLines=1,
                        color = MyBlack,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )

                },
                //背景色
                backgroundColor = MaterialTheme.colors.background,
                //内容颜色
                contentColor = MyBlack,
                //底部阴影
                elevation = 5.dp
            )

        },
        backgroundColor = MyGray,
        snackbarHost = {
            SnackbarHost(it) { data ->
                if(data.message=="操作成功"){
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
                else if(data.message.split('@')[0]=="no"){
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
                            ),verticalAlignment = Alignment.CenterVertically) {
                            Text(modifier= Modifier
                                .padding(vertical = 10.dp, horizontal = 25.dp),
                                text = "数据库中无该关联词条", color = MyDeepGray)
                            /*TextButton(onClick = {
                                Log.d("testREL", data.message.split('@')[1])
                                /*TODO*/ navController.navigate(RouteConfig.ROUTE_InputAttr +"?${ParamsConfig.PARAMS_Word}=${data.message.split('@')[1]}"+"?num=1")
                            }) {
                                Text("添加词条",color= MyOrange)
                            }*/
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
            Column(
                Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp))) {
                Column {
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
                                    state = index
                                    inputText=""
                                    currentInputText = attrToInfo[listKey[state]].toString()
                                    // 切换到对应属性的详细信息
                                    // currentInputText = detailsMap.getOrDefault(title, "")
                                },
                                unselectedContentColor = MaterialTheme.colors.background,
                                selectedContentColor = MaterialTheme.colors.background
                            )
                        }
                    }
                    Divider(
                        color= MyDeepGray.copy(alpha = 0.0f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 0.dp),
                    )
                }
                //在此处放文本框
                currentInputText = attrToInfo[listKey[state]].toString()
                //Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(0.dp))
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp),
                    //contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        item {
                            Text(
                                text = currentInputText,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .padding(start = 0.dp, top = 0.dp, bottom = 0.dp)
            ) {

                val inDataBaseEvent= remember {
                    mutableStateOf(false)
                }
                val inDataBase = if(inDataBaseEvent.value) trans<Boolean>{ isSearched(inputText) } else null
                inDataBase?.run {
                    if(inDataBase.hasError){
                        navController.navigate(RouteConfig.ROUTE_NetError)
                    } else if(inDataBase.value == true){
                        if (inputText.isNotEmpty() && propertyValues[listKey[state]]?.contains(
                                inputText
                            ) != true ) {
                            propertyValues[listKey[state]] = ((propertyValues[listKey[state]] ?: emptyList()) + inputText) as MutableList<String>
                            //propertyValuesSec.add(currentInputText)
                            transport[listKey[state]] = "${transport[listKey[state]]}$inputText" + "_"
                            if(!mp.containsKey(listKey[state])) mp[listKey[state]] = mutableListOf<String>()
                            mp[listKey[state]]?.add(inputText)
                            inputText = ""
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("添加成功")
                            }
                        }
                        else{
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("关联词条为空或重复")
                            }
                        }
                        inDataBaseEvent.value=false
                    }else if(inDataBase.value==false){
                        scope.launch {

                            scaffoldState.snackbarHostState.showSnackbar("no@"+inputText)
                            //navController.popBackStack()
                        }
                        inDataBaseEvent.value=false
                    }else{}
                }

                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .weight(3f)
                        .height(55.dp)
                        .padding(top = 0.dp, start = 0.dp),
                ) {
                    val context = LocalContext.current
                    // Use a Column to arrange the elements vertically
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(top = 0.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            Modifier
                                .shadow(5.dp, RoundedCornerShape(10.dp))
                                .background(Color.White, RoundedCornerShape(10.dp))) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .padding(start = 0.dp, end = 0.dp, top = 0.dp),
                                value = inputText,
                                onValueChange = {value ->
                                    val filteredValue = value.filter { it != ' ' && it != '\n' }.toString()
                                    inputText = filteredValue
                                },
                                label = { Text(text = "请输入关联词条",) },
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
                                    inDataBaseEvent.value=true
                                }),
                                //isError = wordError&&newEntryText!=""
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                val context = LocalContext.current
                Button(
                    onClick = {
                              inDataBaseEvent.value=true/*
                        if (inputText.isNotEmpty() && propertyValues[listKey[state]]?.contains(
                                inputText
                            ) != true ) {
                            propertyValues[listKey[state]] = (propertyValues[listKey[state]] ?: emptyList()) + inputText
                            //propertyValuesSec.add(currentInputText)
                            transport[listKey[state]] = "${transport[listKey[state]]}_$inputText"
                            if(!mp.containsKey(listKey[state])) mp[listKey[state]] = mutableListOf<String>()
                            mp[listKey[state]]?.add(inputText)
                            inputText = ""
                        }
                        else{
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("关联词条为空或重复")
                            }
                        }*/
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 0.dp)
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp)),
                //contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "关联词条", fontSize = 16.sp, color = MyDeepGray)
                }
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)) {
                    val brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, MyDeepGray.copy(alpha = 0.5f), MyDeepGray, MyDeepGray.copy(alpha = 0.5f), Color.Transparent),
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
                        .fillMaxSize()
                        .padding(10.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        FlowRow(mainAxisSpacing = 10.dp) {

                            propertyValues[listKey[state]]?.forEach { entry ->
                                entryButton(entry = entry, onDelete = {
//TODO
                                    mp[listKey[state]]?.remove(entry)
                                    propertyValues[listKey[state]]?.remove(entry)
                                    inputText="1"
                                    inputText=""
                                })
                            }
                        }

                    }
                }
            }

            val addWordRelEvent=remember{ mutableStateOf(false) }
            val addWordRelInfo=if(addWordRelEvent.value) {
                trans<Boolean> {
                    addWordRel(word, attrList = CreateNewWordTemp.AttrList, AttrToRele)
                }
            } else null

            addWordRelInfo?.run {
                if(addWordRelInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                else{
                    addWordRelInfo.value?.run {
                        if(this){
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                navController.popBackStack()
                            }
                            addWordRelEvent.value=false
                        }else{
                            //info.value="2"
                            addWordRelEvent.value=false
                        }

                    }
                }

            }

            val context = LocalContext.current
            var isButtonClicked by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {

                    transport.forEach { (key, value) ->
                        transport.put(key, value.dropLast(1))
                    }
                    //AttrToRele = transport
                    AttrList.forEach {
                        AttrToRele[it]= mp[it]?.let { it1 -> DataConfig.listToStr(it1) }.toString()
                    }
                    /*scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(AttrToRele.entries.toString())
                        //navController.popBackStack()
                    }*/
                    addWordRelEvent.value=true
                        //Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
                    }

                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MyOrange,
                    contentColor = Color.White
                )
            ) {
                Text(text = "保存")
            }

        }
    }
}

@Composable
fun entryButton(
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
            title = { Text("是否删除此关联词条？") },
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