package com.example.omniscient.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.omniscient.composable.SearchBar
import com.example.omniscient.composable.TopBar
import com.example.omniscient.ui.theme.MyDeepGray

@Composable
fun SearchPage(navController: NavHostController){
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                str = "搜索",
                HomeButtonPosAtStart = false
            )
        }
    ){
        Column(Modifier.padding(top = 20.dp)) {
            SearchBar(navController = navController)
            Divider(
                color= MyDeepGray.copy(alpha = 0.3f),
                thickness = 2.dp,
                modifier = Modifier.padding(top = 20.dp),
            )
        }

    }
}