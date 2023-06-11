package com.example.omniscient.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.omniscient.config.RouteConfig
import com.example.omniscient.ui.theme.*
import com.google.accompanist.insets.navigationBarsPadding
import java.lang.Integer.max

@Composable
fun VerticalButtonList(navController: NavController,associationTerms:List<String>){
    Column(
        Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(100.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .background(
                MaterialTheme.colors.background,
                RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp, start = 10.dp),
            text = "关联词条",
            fontSize = 16.sp,
            color = MyDeepGray
        )
        Row(Modifier.padding(start = 8.dp, top = 3.dp, bottom = 6.dp)) {
            Spacer(modifier = Modifier.background(color = MyLightGray).size(160.dp, 0.6.dp))
        }

        LazyRow(
            Modifier.padding(bottom = 20.dp),
            contentPadding= PaddingValues(1.dp)
        ){
            items(associationTerms.sorted()){
                Button(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 2.dp, end = 2.dp),
                    onClick = {
                        navController.navigate("${RouteConfig.ROUTE_ResultPage}/${it}")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MyLightOrange
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(start = 6.dp,end=6.dp),
                        text = it,
                        color = MyWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
    }
}