package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.leetcoderatingpredictorai.Screens
import com.panwar2001.leetcoderatingpredictorai.viewModels.PredictionViewModel
import com.panwar2001.leetcoderatingpredictorai.viewModels.WeeklyContestViewModel
import kotlinx.coroutines.launch


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
            val weeklyContestViewModel: WeeklyContestViewModel= hiltViewModel()
            val isNetworkAvailable by weeklyContestViewModel.networkAvailable.observeAsState(initial = false)
            val contests by weeklyContestViewModel.contests.collectAsStateWithLifecycle()
            val predictionStatus by predictionViewModel.predictionStatus.collectAsStateWithLifecycle()
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val haptic = LocalHapticFeedback.current
            Scaffold(bottomBar = {
                BottomNavigationBar(
                    selectedTab = "Dashboard",
                    navigateTo = { navController.navigate(it)})
            }, snackbarHost =  {SnackbarHost(hostState = snackBarHostState)}){ paddingValue ->
                Box(modifier = Modifier.padding(paddingValue)) {
                    LeetCodeRatingPredictorScreen(
                        contests= contests,
                        isRefreshing = weeklyContestViewModel.loading,
                        reloadContest = weeklyContestViewModel::reloadContest,
                        predict = {
                            // internet not available
                            if(!isNetworkAvailable){
                               scope.launch {
                                   snackBarHostState.showSnackbar(
                                       message = "No internet connection!",
                                       duration = SnackbarDuration.Short,
                                       withDismissAction = true
                                   )
                               }
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }else {
                                predictionViewModel.predict(it, success = {
                                    if (it) navController.navigate(Screens.PredictionScreen.route)
                                    else {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                })
                            }
                        },
                        unableToPredict = predictionStatus.unableToPredict,
                        isPredicting = predictionStatus.loading,
                        setUnableToPredict = predictionViewModel::setUnableToPredict,
                        isNetworkAvailable = isNetworkAvailable
                    )
                }
            }
        }
        composable(route = Screens.PredictionScreen.route) {
            val contestData by predictionViewModel.contestData.collectAsState()
            val problemsSolved by predictionViewModel.problemsSolved.collectAsState()
            val contestMetaData by predictionViewModel.contestMetaData.collectAsState()
            val predictionStatus by predictionViewModel.predictionStatus.collectAsState()

                Scaffold(bottomBar = {
                    BottomNavigationBar(
                        selectedTab = "Prediction",
                        navigateTo = { navController.navigate(it) }
                    )
                }) { paddingValue ->
                    Box(modifier = Modifier.padding(paddingValue)) {
                        if (contestMetaData.rating == "") {
                            AwaitingPrediction()
                        } else {
                            RatingOverviewScreen(
                                contest = contestData,
                                problemsSolved = problemsSolved,
                                contestMetaData = contestMetaData,
                                ratingDelta = predictionStatus.ratingDelta
                            )
                        }
                    }
                }
            }
    }
}
