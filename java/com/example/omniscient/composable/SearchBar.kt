package com.example.omniscient.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.MyDeepGray

@Composable
fun SearchBar(navController: NavHostController){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center//水平居中
    ) {
        var textValue by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(30.dp, 0.dp),
            value = textValue,
            onValueChange = { textValue = it },
            placeholder = { Text("请输入词条进行检索")},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {navController.navigate("${RouteConfig.ROUTE_ResultPage}/${textValue}")}),
            leadingIcon = {
                IconButton(
                    onClick = {
                        /*TODO 跳转功能页*/
                        if(textValue!="") navController.navigate("${RouteConfig.ROUTE_ResultPage}/${textValue}")
                    },
                    modifier = Modifier.size(20.dp),
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "Search Button"
                    )
                }
            },
            trailingIcon = {
                if (textValue.isNotEmpty()) {
                    IconButton(
                        onClick = { textValue = "" },
                        modifier = Modifier.size(20.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.clear_icon),
                            contentDescription = "Search Button"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = MyDeepGray,
                unfocusedBorderColor = MyDeepGray,
                cursorColor = MyDeepGray
            )
        )
    }
}