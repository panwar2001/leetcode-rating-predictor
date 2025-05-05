package com.panwar2001.leetcoderatingpredictorai.viewModels


import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.panwar2001.leetcoderatingpredictorai.core.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class UiState(
    val imgUrl: String="",
    val loading: Boolean=false
)
private const val STOP_TIMEOUT_MILLISECONDS: Long = 1_000


@HiltViewModel
class PredictionViewModel @Inject
constructor(private val dataRepository: DataRepository,
            private val connectivityManager: ConnectivityManager): ViewModel(){
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    var unableToPredict by mutableStateOf(false)

    fun predict(username: String): Boolean{
       return true
    }
}