package com.panwar2001.leetcoderatingpredictorai.viewModels

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.panwar2001.leetcoderatingpredictorai.core.ContestHistoryRepository
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.OptionalInt
import javax.inject.Inject

@HiltViewModel
class WeeklyContestViewModel @Inject
constructor(
    private val contestHistoryRepository: ContestHistoryRepository,
    contestDao: ContestDao,
    private val connectivityManager: ConnectivityManager
            ): ViewModel(){
    val contests: StateFlow<List<ContestEntity>> = contestDao.retrieveAllContest()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    var loading by mutableStateOf(false)
    private val _networkAvailable = MutableLiveData<Boolean>()
    val networkAvailable: LiveData<Boolean> get() = _networkAvailable



    // Store the NetworkCallback so that we can unregister it later
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _networkAvailable.postValue(true)  // Network is available, set LiveData to true
            viewModelScope.launch{
                contestHistoryRepository.loadContestDataIfEmptyDB()
            }
        }

        override fun onLost(network: Network) {
            _networkAvailable.postValue(false)  // Network is lost, set LiveData to false
        }
    }

    init {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            networkCallback
        )
    }

    /**
     * Don't forget to unregister the callback when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun reloadContest(){
        viewModelScope.launch {
            loading=true
            contestHistoryRepository.loadNewContestData(name = Optional.presentIfNotNull("loft_plyr"))
            loading=false
        }
    }
}