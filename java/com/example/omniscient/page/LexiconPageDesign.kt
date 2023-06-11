package com.example.omniscient.page

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.ui.theme.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.omniscient.composable.*
import com.example.omniscient.config.DataConfig
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.unit.FavoriteJudge
import com.example.omniscient.unit.UsernameJudge
import com.example.omniscient.unit.WordNameJudge
import com.example.omniscient.util.trans
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
fun lexiconPage(navController: NavHostController) {
    if(DataConfig.Login){
        lexicon(navController,)
    }else{
        Scaffold(
            bottomBar = {
                MainBottomNavigation(navController)
            },
            topBar = { TopBar(navController = navController, str = "词库",HomeButtonPosAtStart = false) },
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                    Row(modifier= Modifier
                        .padding(start =20.dp, bottom = 80.dp),verticalAlignment = Alignment.CenterVertically){
                        Text(text = "登录以使用词库功能", color = MyDeepGray)
                        TextButton(onClick = { navController.navigate(RouteConfig.ROUTE_LoginPage) }) {
                            Text("登录",color= MyOrange)
                        }
                    }

            }
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun lexicon(navController: NavHostController) {
    val keyboard = LocalSoftwareKeyboardController.current

    var showAddLexiconDialog by remember { mutableStateOf(false) }
    var newLexiconText by remember { mutableStateOf("") }
    var gotoCreateNewWordDialg by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val FavoriteFolds = trans<Map<String,MutableList<String>>>{ getFavoriteInfo(DataConfig.ID) }
    var lexiconList by remember {
        mutableStateOf(
            mutableListOf<String>()
        )
    }//假设这是要显示的收藏夹
    var lexiconMap by remember {
        mutableStateOf(
            mutableMapOf<String,MutableList<String>>()
        )
    }//假设这是要显示的词条
    var load_finish=remember { mutableStateOf(false) }

    FavoriteFolds.run {
        if(FavoriteFolds.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
        else if(FavoriteFolds.value!=null){
            for (i in FavoriteFolds.value.keys){
                if(i=="@allWord") continue
                if(i=="@folds") lexiconList=FavoriteFolds.value[i]!!
                else lexiconMap.put(i,FavoriteFolds.value[i]!!)
            }
            load_finish.value=true
        }
    }

    val flowRowHeight = remember { mutableStateOf(0) }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar ={
            MainBottomNavigation(navController)
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                //标题
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxSize(0.92f)
                            .wrapContentSize(Alignment.Center),
                        text ="词库",
                        maxLines=1,
                        color = MyBlack,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                },

                //导航按钮(返回按钮、首页按钮)
                navigationIcon = {
                    IconButton(modifier = Modifier, onClick = {navController.popBackStack()}) {
                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "Back Key"
                        )
                    }
                },

                //收藏按钮
                actions = {
                    IconButton(onClick = {
                        showAddLexiconDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "添加",tint= MyBlack)
                    }
                },
                //背景色
                backgroundColor = MaterialTheme.colors.background,
                //内容颜色
                contentColor = MyBlack,
                //底部阴影
                elevation = 5.dp
            )
        },
        snackbarHost = {
            SnackbarHost(it) { data ->
                if(data.message=="操作成功"){
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
                }else if(data.message=="数据库中不存在该词条,是否创建词条"){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Row(modifier= Modifier
                            .padding(bottom = 75.dp)
                            .shadow(5.dp, RoundedCornerShape(10.dp))
                            .background(
                                MaterialTheme.colors.background,
                                RoundedCornerShape(10.dp)
                            )
                        ) {
                            Row(modifier= Modifier
                                .padding(start = 20.dp),verticalAlignment = Alignment.CenterVertically){
                                Text(text = data.message+"?", color = MyDeepGray)
                                TextButton(onClick = { navController.navigate(RouteConfig.ROUTE_InputAttr)}) {
                                    Text("确定",color= MyOrange)
                                }
                            }

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
    )
    {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (lexiconList.isEmpty()&&load_finish.value) {
                Text(
                    "你还没有创建任何收藏夹",
                    color = MyDeepGray.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 70.dp)
        ) {
            lexiconList.forEachIndexed { index, word ->
                var isExpanded by remember { mutableStateOf(false) }//记录当前矩形是否应该展开
                val height by animateDpAsState(
                    if (isExpanded) 170.dp else 85.dp,
                    tween(durationMillis = 300)
                )
                val textStarting by animateDpAsState(
                    if (isExpanded) 12.dp else 30.dp,
                    tween(durationMillis = 300)
                )
                val gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF5EC7F7),
                        Color(0xFFC4B7FF)
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)//根据是否展开渐变改变高度
                        .clip(RoundedCornerShape(15.dp))
                        .background(gradient)
                        .padding(start = 0.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    isExpanded = !isExpanded
                                }
                            )
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = word,
                                    color = White,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(top = textStarting),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                val selectedEntries = remember { mutableStateListOf<String>() } // 记录选择的词条
                                val dialog = remember { mutableStateOf(false) }
                                val anotherDialog = remember { mutableStateOf(false) }
                                val context = LocalContext.current
                                IconButton(
                                    onClick = { dialog.value = true },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Image(painterResource(R.drawable.san_dian), contentDescription = "Menu")
                                }

                                val delFavoriteEvent=remember{ mutableStateOf(false) }
                                val delFavoriteInfo=if(delFavoriteEvent.value) trans<Boolean>{ delFavorite(DataConfig.ID,word) } else null

                                delFavoriteInfo?.run {
                                    delFavoriteInfo.hasError.run{
                                        if(delFavoriteInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                                        else{
                                            delFavoriteInfo.value?.run {
                                                if(this){
                                                    scope.launch {
                                                        delFavoriteEvent.value=false
                                                        if (lexiconList.contains(word)) {
                                                            lexiconList = lexiconList.toMutableList().apply { remove(word) }
                                                        }

                                                        if(lexiconMap.contains(word)) lexiconMap=lexiconMap.toMutableMap().apply { remove(word) }
                                                        dialog.value = false
                                                        scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                                    }
                                                }else{
                                                    scope.launch {
                                                        delFavoriteEvent.value=false
                                                        dialog.value = false
                                                        scaffoldState.snackbarHostState.showSnackbar("操作失败")
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                                if (dialog.value) {
//TODO
                                    Dialog(onDismissRequest = { }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .clickable(
                                                    onClick = { dialog.value = false },
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ),
                                            contentAlignment = Alignment.BottomCenter
                                        ){
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        Color.White,
                                                        RoundedCornerShape(
                                                            10.dp,
                                                            10.dp,
                                                            0.dp,
                                                            0.dp
                                                        )
                                                    )
                                            ) {
                                                TextButton(onClick = {
                                                    selectedEntries.clear()
                                                    if (lexiconMap[word]?.isNotEmpty() == true) {
                                                        anotherDialog.value = true
                                                    } else {
                                                        scope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar("当前收藏夹内没有词条")
                                                        }
                                                    }
                                                    dialog.value = false
                                                },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable(onClick = {
                                                            // 这里也可以省略 onClick 处理函数，
                                                            // 直接使用传递给 TextButton 的 onClick 函数
                                                        })
                                                ) {
                                                    Text("批量删除词条", color = Color.Black)
                                                }
                                                Divider(
                                                    color= MyDeepGray.copy(alpha=0.3f),
                                                    thickness = 0.7.dp,

                                                    )
                                                TextButton(onClick = {
                                                    //TODO 删除收藏夹
                                                    /*lexiconList = lexiconList.filter { it != word }.toMutableStateList()
                                                    lexiconMap.remove(word)
                                                    dialog.value = false*/delFavoriteEvent.value=true
                                                },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable(onClick = {
                                                            // 这里也可以省略 onClick 处理函数，
                                                            // 直接使用传递给 TextButton 的 onClick 函数
                                                        })
                                                ) {
                                                    Text("删除当前收藏夹", color = Color.Black)
                                                }
                                                Divider(
                                                    color= MyDeepGray.copy(alpha=0.3f),
                                                    thickness = 2.dp,

                                                    )
                                                TextButton(onClick = { dialog.value = false },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable(onClick = {
                                                            // 这里也可以省略 onClick 处理函数，
                                                            // 直接使用传递给 TextButton 的 onClick 函数
                                                        })) {
                                                    Text("取消", color = Color.Black)
                                                }
                                            }
                                        }
                                    }
                                }

                                val delWordEvent=remember{ mutableStateOf(false) }
                                val delWordInfo=if(delWordEvent.value) trans<Boolean>{ opWordToFavorite(DataConfig.ID,word, delWords = selectedEntries)} else null
//TODO
                                delWordInfo?.run {
                                    delWordInfo.hasError.run{
                                        if(delWordInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                                        else{
                                            delWordInfo.value?.run {
                                                if(this){
                                                    scope.launch {
                                                        lexiconMap[word]?.removeAll(selectedEntries)
                                                        selectedEntries.clear()
                                                        delWordEvent.value=false
                                                        anotherDialog.value = false
                                                        scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                                    }
                                                }else{
                                                    scope.launch {
                                                        selectedEntries.clear()
                                                        delWordEvent.value=false
                                                        anotherDialog.value = false
                                                        scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                if (anotherDialog.value) {
                                    AlertDialog(
                                        onDismissRequest = { anotherDialog.value = false },
                                        title = { Text("选择要删除的词条") },
                                        text = {
                                            Column(Modifier.verticalScroll(
                                                state = rememberScrollState()
                                            )) {
                                                lexiconMap[word]?.forEach { entry ->
                                                    // 使用 Box 容器包裹一整行，扩大触摸区域
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable(onClick = {
                                                                if (selectedEntries.contains(entry)) {
                                                                    selectedEntries.remove(entry)
                                                                } else {
                                                                    selectedEntries.add(entry)
                                                                }
                                                            })
                                                    ) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Checkbox(
                                                                checked = selectedEntries.contains(entry),
                                                                onCheckedChange = { isChecked ->
                                                                    if (isChecked) {
                                                                        selectedEntries.add(entry)
                                                                    } else {
                                                                        selectedEntries.remove(entry)
                                                                    }
                                                                }
                                                            )
                                                            Text(text = entry)
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                delWordEvent.value=true
                                            }) {
                                                Text("确定",color= MyOrange)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = {
                                                selectedEntries.clear()
                                                anotherDialog.value = false
                                            }) {
                                                Text("取消",color= MyDeepGray)
                                            }
                                        }
                                    )
                                }
                            }
                            // Add the button here
                        }
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter =
                            fadeIn(animationSpec =
                            tween(durationMillis =
                            600)) +
                                    expandVertically(animationSpec =
                                    tween(durationMillis =
                                    20)),
                            exit =
                            fadeOut(animationSpec =
                            tween(durationMillis =
                            300)) +
                                    shrinkVertically(animationSpec =
                                    tween(durationMillis =
                                    300))
                        ) {
                            Spacer(modifier = Modifier.height(50.dp))
                            Box(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, bottom = 5.dp, end = 10.dp, top = 7.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    FlowRow(mainAxisSpacing = 10.dp) {
                                        lexiconMap[word]?.forEach { entry ->
                                            EntryButton(entry = entry,
                                                onClick = {navController.navigate("${RouteConfig.ROUTE_ResultPage}/${entry}")},
                                                onDelete = {
                                                lexiconMap[word]?.remove(entry)
                                            })
                                        }
                                        var showAddDialog by remember { mutableStateOf(false) }
                                        var newEntryText by remember { mutableStateOf("") }
                                        Button(
                                            onClick =
                                            { showAddDialog =
                                                true },
                                            shape =
                                            RoundedCornerShape(30.dp),
                                            colors =
                                            ButtonDefaults.buttonColors(
                                                backgroundColor =
                                                Color(0xE8B4A4EE),
                                                contentColor =
                                                Color.White
                                            )
                                        ) {
                                            Text("+")
                                        }
                                        val context = LocalContext.current

                                        val addWordEvent=remember{ mutableStateOf(false) }
                                        val inDataBase=if(addWordEvent.value) trans<Boolean>{ isSearched(newEntryText) } else null
                                        inDataBase?.run {
                                            if(inDataBase.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                                            else if(inDataBase.value==true){
                                                val addWordInfo=if(addWordEvent.value) trans<Boolean>{ opWordToFavorite(DataConfig.ID,word, addWords =listOf(newEntryText)) } else null
                                                addWordInfo?.run {
                                                    if(addWordInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                                                    else{
                                                        addWordInfo.value?.run {
                                                            if(this){
                                                                scope.launch {
                                                                    addWordEvent.value=false
                                                                    showAddDialog = false
                                                                    //lexiconMap[word]=lexiconMap[word]?.toMutableList()?.apply { add(newEntryText) }
                                                                    lexiconMap[word]?.add(newEntryText)
                                                                    newEntryText = ""
                                                                    scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                                                }

                                                            }else{
                                                                scope.launch {
                                                                    addWordEvent.value=false
                                                                    showAddDialog = false
                                                                    newEntryText = ""
                                                                    scaffoldState.snackbarHostState.showSnackbar("操作失败")
                                                                }

                                                            }

                                                        }
                                                    }

                                                }
                                            }
                                            else if(inDataBase.value==false){
                                                scope.launch {
                                                    showAddDialog = false
                                                    newEntryText = ""
                                                    addWordEvent.value=false
                                                    scaffoldState.snackbarHostState.showSnackbar("数据库中不存在该词条,是否创建词条")
                                                }

                                            } else {

                                            }
                                        }

                                        val wordError:Boolean= WordNameJudge(newEntryText)
                                        if (showAddDialog) {
                                            AlertDialog(
                                                onDismissRequest =
                                                { showAddDialog = false;newEntryText="" },
                                                title =
                                                { Text("添加新词条") },
                                                text = {

                                                    Column (){
                                                        TextField(
                                                            value = newEntryText,
                                                            onValueChange = {newValue ->
                                                                // 将中文字符转成字符串，去除空格和换行符
                                                                val filteredValue = newValue.filter { it != ' ' && it != '\n' }.toString()

                                                                newEntryText = filteredValue // 符合要求，更新输入框的值
                                                                },
                                                            label = { Text(text = "请输入要添加的词条",) },
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
                                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                                            keyboardActions= KeyboardActions(onDone = {

                                                                //TODO
                                                                if (wordError) {
                                                                    // 输入为空，弹出提示
                                                                    scope.launch {
                                                                        showAddDialog=false
                                                                        newLexiconText = ""
                                                                        scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                                                    }
                                                                } else if (lexiconMap[word]?.contains(newEntryText) == true) {
                                                                    // 词条重复，弹出提示
                                                                    scope.launch {
                                                                        showAddDialog=false
                                                                        newLexiconText = ""
                                                                        scaffoldState.snackbarHostState.showSnackbar("该收藏夹中已存在该词条")
                                                                    }
                                                                } else {
                                                                    //TODO 将新词条加入到词典中 是否存在与数据库
                                                                    addWordEvent.value=true
                                                                }
                                                                keyboard?.hide()
                                                            }),
                                                            isError = wordError&&newEntryText!=""
                                                        )
                                                        Text(
                                                            text = if(wordError&&newEntryText!="") "词条长度应在1~8位之间" else "",
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
                                                        if (wordError) {
                                                            // 输入为空，弹出提示
                                                            scope.launch {
                                                                showAddDialog=false
                                                                newLexiconText = ""
                                                                scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                                            }
                                                        } else if (lexiconMap[word]?.contains(newEntryText) == true) {
                                                            // 词条重复，弹出提示
                                                            scope.launch {
                                                                showAddDialog=false
                                                                newLexiconText = ""
                                                                scaffoldState.snackbarHostState.showSnackbar("该收藏夹中已存在该词条")
                                                            }
                                                        } else {
                                                            //TODO 将新词条加入到词典中 是否存在与数据库
                                                            addWordEvent.value=true
                                                        }
                                                    }, enabled = !wordError) {
                                                        Text("确定",color= MyOrange)
                                                    }
                                                },

                                                dismissButton =
                                                {
                                                    TextButton(onClick =
                                                    {
                                                        newEntryText="" //清空内容
                                                        showAddDialog = false }) {
                                                        Text("取消",color= MyDeepGray)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (index != lexiconList.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            val context = LocalContext.current
            val favoriteError:Boolean= FavoriteJudge(newLexiconText)

            val addFavoriteEvent=remember{ mutableStateOf(false) }
            val addFavoriteInfo=if(addFavoriteEvent.value) trans<Boolean>{ addFavorite(DataConfig.ID,newLexiconText) } else null

            addFavoriteInfo?.run {
                addFavoriteInfo.hasError.run{
                    if(addFavoriteInfo.hasError) navController.navigate(RouteConfig.ROUTE_NetError)
                    else{
                        addFavoriteInfo.value?.run {

                            if(this){
                                scope.launch {
                                    addFavoriteEvent.value=false
                                    lexiconList=lexiconList.toMutableList().apply { add(newLexiconText) }
                                    //lexiconList.add(newLexiconText)
                                    lexiconMap[newLexiconText] = mutableListOf()
                                    newLexiconText = ""
                                    showAddLexiconDialog = false
                                    scaffoldState.snackbarHostState.showSnackbar("操作成功")
                                }

                            }else{
                                scope.launch {
                                    addFavoriteEvent.value=false
                                    newLexiconText = ""
                                    showAddLexiconDialog = false
                                    scaffoldState.snackbarHostState.showSnackbar("操作失败")
                                }
                            }

                        }
                    }
                }
            }
            if (showAddLexiconDialog) {
                AlertDialog(
                    onDismissRequest = { showAddLexiconDialog = false;newLexiconText="" },
                    title = { Text("新建收藏夹") },
                    text = {

                        Column (){
                            TextField(
                                value = newLexiconText,
                                onValueChange = {newValue ->
                                    // 将中文字符转成字符串，去除空格和换行符
                                    val filteredValue = newValue.filter { it != ' ' && it != '\n' && it!='@'}.toString()

                                    newLexiconText = filteredValue // 符合要求，更新输入框的值
                                },
                                label = { Text(text = "请输入收藏夹名称",) },
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
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions= KeyboardActions(onDone = {

                                    //TODO
                                    if (!favoriteError) {
                                        if (lexiconList.contains(newLexiconText)) {
                                            scope.launch {
                                                showAddLexiconDialog=false
                                                newLexiconText = ""
                                                scaffoldState.snackbarHostState.showSnackbar("该收藏夹已存在，请重新输入")
                                            }
                                        } else {
                                            //TODO
                                            addFavoriteEvent.value=true
                                        }
                                    } else {
                                        scope.launch {
                                            showAddLexiconDialog=false
                                            newLexiconText = ""
                                            scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                        }
                                    }
                                    keyboard?.hide()
                                }),
                                isError = favoriteError&&newLexiconText!=""
                            )
                            Text(
                                text = if(favoriteError&&newLexiconText!="") "收藏夹名称长度应在2~8位之间" else "",
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 3.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                color = MyRed,
                                textAlign = TextAlign.Left
                            )
                        }
                        //TODO
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (!favoriteError) {
                                if (lexiconList.contains(newLexiconText)) {
                                    scope.launch {
                                        showAddLexiconDialog=false
                                        newLexiconText = ""
                                        scaffoldState.snackbarHostState.showSnackbar("该收藏夹已存在，请重新输入")
                                    }
                                } else {
                                    //TODO

                                    addFavoriteEvent.value=true
                                }
                            } else {
                                scope.launch {
                                    showAddLexiconDialog=false
                                    newLexiconText = ""
                                    scaffoldState.snackbarHostState.showSnackbar("请输入正确信息")
                                }
                            }
                        },enabled=!favoriteError) {
                            Text("确定",color= MyOrange)
                        }

                    },
                    dismissButton = {
                        TextButton(onClick = {
                            newLexiconText = "" // 清空新收藏夹文本框中的内容
                            showAddLexiconDialog = false
                        }) {
                            Text("取消",color= MyDeepGray)
                        }
                    }
                )
            }

        }
    }
}


@Composable
fun EntryButton(entry: String, onClick: () -> Unit, onDelete: () -> Unit) {
    val showDeleteDialog = remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xE208C9E2),
            contentColor = Color.White
        ),
    ) {
        Text(entry)
    }
}


