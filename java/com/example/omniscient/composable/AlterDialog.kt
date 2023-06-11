package com.example.omniscient.composable

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun addAlterDialog(openDialog: MutableState<Boolean>, s:String) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "标题") },
            text = {
                Text(
                    text = s
                )
            }, confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "确认")
                }
            }, dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "取消")
                }
            })
    }
}
