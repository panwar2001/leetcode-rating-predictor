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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.leetcoderatingpredictorai.Screens
import com.panwar2001.leetcoderatingpredictorai.viewModels.PredictionViewModel
import com.panwar2001.leetcoderatingpredictorai.viewModels.WeeklyContestViewModel


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
            val weeklyContestViewModel: WeeklyContestViewModel= hiltViewModel()
            val contests by weeklyContestViewModel.contests.collectAsStateWithLifecycle()

            Scaffold(bottomBar = {
                BottomNavigationBar(
                    selectedTab = "Dashboard",
                    navigateTo = { navController.navigate(it)})
            }){ paddingValue ->
                Box(modifier = Modifier.padding(paddingValue)) {
                    LeetCodeRatingPredictorScreen(
                        contests= contests,
                        isRefreshing = weeklyContestViewModel.loading,
                        reloadContest = weeklyContestViewModel::reloadContest,
                        predict = {
                            if(predictionViewModel.predict(it)){
                                navController.navigate(Screens.PredictionScreen.route)
                            }
                        },
                        unableToPredict = predictionViewModel.unableToPredict
                    )
                }
            }
        }
        composable(route = Screens.PredictionScreen.route) {
            val uiState by predictionViewModel.uiState.collectAsState()
            Scaffold(bottomBar = {
                BottomNavigationBar(
                    selectedTab = "Prediction",
                    navigateTo = {navController.navigate(it)}
                )
            }){ paddingValue ->
                    Box(modifier = Modifier.padding(paddingValue)) {
                        RatingOverviewScreen(

                        )
                    }
                }
            }
    }
}
