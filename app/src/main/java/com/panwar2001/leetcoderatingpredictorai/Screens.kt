package com.panwar2001.leetcoderatingpredictorai

sealed class Screens(val route : String) {
    object DashboardScreen: Screens("Dashboard")
    object PredictionScreen: Screens("Prediction")
}