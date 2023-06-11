package com.example.omniscient.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omniscient.page.ColorfulColum
import com.example.omniscient.ui.theme.MyBlack

@Composable
fun KeyWord(word:String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                5.dp,
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 10.dp,
                    bottomEnd = 10.dp
                )
            )
            .background(MaterialTheme.colors.background, RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 10.dp,
                bottomEnd = 10.dp)
            )

    ) {
        ColorfulColum()
        Row(
            Modifier
                .fillMaxSize()
                .padding(4.dp, 4.dp),verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier.weight(4f),
                fontSize = 36.sp,
                color = MyBlack,
                text = word,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}