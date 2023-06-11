package com.example.omniscient.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.MyDeepGray

@Composable
fun SearchBarButton(navController: NavHostController){
    Button(modifier = Modifier
        .fillMaxHeight(0.5f).fillMaxWidth().padding(start = 20.dp),
        onClick = {
            /*TODO*/
            navController.navigate(RouteConfig.ROUTE_SearchPage)
        },
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MyDeepGray),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.background,
        ),
        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp,0.dp,0.dp),
        contentPadding = PaddingValues(start = 10.dp),
        //去除水波纹
        interactionSource = remember { NoRippleInteractionSource() },

        ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement= Arrangement.Start,verticalAlignment= Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 0.dp, end = 5.dp),
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "Search Button"
            )
            Text(
                text = "请输入词条进行检索",
                fontSize = 11.sp,
                color = MyDeepGray
            )
        }

    }
}