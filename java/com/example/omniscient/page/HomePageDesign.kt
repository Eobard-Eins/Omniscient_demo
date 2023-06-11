package com.example.omniscient.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.omniscient.R
import com.example.omniscient.composable.MainBottomNavigation
import com.example.omniscient.composable.SearchBar
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun homePage(navController: NavHostController){
    Scaffold(bottomBar ={
        MainBottomNavigation(navController)
    }) {
        ProvideWindowInsets() {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 200.dp),
                verticalArrangement = Arrangement.Center,//垂直居中
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    painter = painterResource(R.drawable.title_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillHeight
                )
                SearchBar(navController = navController)

            }
        }
    }
}
