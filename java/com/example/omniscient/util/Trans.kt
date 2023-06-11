package com.example.omniscient.util

import androidx.compose.runtime.Composable
import com.example.omniscient.unit.Repository

data class INFO<T>(
    val value: T?,
    val hasError:Boolean
)

@Composable
fun <T : Any> trans(
    block: suspend Repository.() -> Res<T>
): INFO<T> {
    val (postUiState, refreshPost, clearError) = produceUiState<Repository,T>(Repository){
        block()
    }
    return INFO<T>(postUiState.value.data, postUiState.value.hasError)
}
