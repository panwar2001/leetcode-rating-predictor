package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.leetcoderatingpredictorai.Screens
import com.panwar2001.leetcoderatingpredictorai.viewModels.PredictionViewModel


/**
 * Composable that has navigation host and graph for navigating among different composable screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationController(
    navController: NavHostController = rememberNavController(),
    predictionViewModel: PredictionViewModel = hiltViewModel<PredictionViewModel>()
) {
    NavHost(navController = navController, startDestination = Screens.DashboardScreen.route) {
        composable(route = Screens.DashboardScreen.route) {
//            val isNetworkAvailable by predictionViewModel.networkAvailable.observeAsState(initial = false)
            Scaffold(bottomBar = {
                BottomNavigationBar(
                    selectedTab = "Dashboard",
                    navigateTo = { if(it!="Dashboard") navController.navigate(it)})
            }){ paddingValue ->
                Box(modifier = Modifier.padding(paddingValue)) {
                    LeetCodeRatingPredictorScreen(

                    )
                }
            }
        }
        composable(route = Screens.PredictionScreen.route) {
            val uiState by predictionViewModel.uiState.collectAsState()
            Scaffold(bottomBar = {
                BottomNavigationBar(
                    selectedTab = "Prediction",
                    navigateTo = {if(it!="Prediction") navController.navigate(it)}
                )
            }){ paddingValue ->
                    Box(modifier = Modifier.padding(paddingValue)) {
                        RatingOverviewScreen()
                    }
                }
            }
    }
}
