package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(selectedTab: String, navigateTo: (String)-> Unit){
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = selectedTab=="Dashboard",
            onClick = { /* Navigate to Dashboard */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Predictions") },
            label = { Text("Predictions") },
            selected = selectedTab=="Prediction",
            onClick = { /* Navigate to Predictions */ }
        )
    }
}