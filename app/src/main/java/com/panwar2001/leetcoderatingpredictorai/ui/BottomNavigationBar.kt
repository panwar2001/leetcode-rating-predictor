package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.panwar2001.leetcoderatingpredictorai.R
import com.panwar2001.leetcoderatingpredictorai.Screens

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    navigateTo: (String)-> Unit,
    isPredicting: Boolean = false){
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = selectedTab=="Dashboard",
            onClick = { if(selectedTab!="Dashboard") navigateTo(Screens.DashboardScreen.route) },
            enabled = !isPredicting
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Predictions") },
            label = { Text(stringResource(R.string.prediction)) },
            selected = selectedTab=="Prediction",
            onClick = { if(selectedTab!="Prediction") navigateTo(Screens.PredictionScreen.route) },
            enabled = !isPredicting
        )
    }
}