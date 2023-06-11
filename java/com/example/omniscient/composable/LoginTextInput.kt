package com.example.omniscient.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omniscient.ui.theme.MyDeepGray
import com.example.omniscient.ui.theme.MyOrange
import com.example.omniscient.ui.theme.MyRed
import com.google.accompanist.insets.navigationBarsWithImePadding

sealed class inputType(
    val label:String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation,
    val errorInfo:String
){
    object Name:inputType(
        label="账号",
        icon= Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation= VisualTransformation.None,
        errorInfo = "账号应为8~11位纯数字"
    )
    object Password:inputType(
        label="密码",
        icon= Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation= PasswordVisualTransformation(),
        errorInfo = "密码应为6~15位由数字、字母、' _ ' 和 ' . ' 组成"
    )
    object Password_r:inputType(
        label="密码",
        icon= Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password),
        visualTransformation= PasswordVisualTransformation(),
        errorInfo = "密码应为6~15位由数字、字母、' _ ' 和 ' . ' 组成"
    )
    object PasswordAgain:inputType(
        label="确认密码",
        icon= Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password),
        visualTransformation= PasswordVisualTransformation(),
        errorInfo = "密码应为6~15位由数字、字母、' _ ' 和 ' . ' 组成"
    )
    object UserName:inputType(
        label="用户名",
        icon= Icons.Default.Face,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation= VisualTransformation.None,
        errorInfo = "用户名长度应在2~6位之间"
    )
}
@Composable
fun TextInput(s:MutableState<String>,inputType:inputType, focusRequester: FocusRequester?=null, keyboardActions: KeyboardActions,isError:Boolean){

    Column() {
        TextField(
            value = s.value,
            onValueChange = {s.value=it},
            modifier = Modifier
                .fillMaxWidth()
                .focusOrder(focusRequester ?: FocusRequester())
                .clip(RoundedCornerShape(30.dp))
                .border(1.dp, if (isError&&s.value!="") MyRed else MyDeepGray, RoundedCornerShape(30.dp)),
            leadingIcon = { Icon(imageVector = inputType.icon, null) },
            label = { Text(text = inputType.label) },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MyOrange,
                cursorColor = MyDeepGray,
                errorLabelColor = MyRed,
                errorLeadingIconColor = MyRed,
                errorCursorColor = MyRed,
                errorIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            keyboardOptions = inputType.keyboardOptions,
            visualTransformation = inputType.visualTransformation,
            keyboardActions=keyboardActions,
            isError = isError&&s.value!=""
        )
        Text(
            text = if(isError&&s.value!="") inputType.errorInfo else "",
            modifier = Modifier
                .padding(start = 20.dp,top=3.dp)
                .fillMaxWidth(),
            fontSize = 12.sp,
            color = MyRed,
            textAlign = TextAlign.Left
        )
    }

}
