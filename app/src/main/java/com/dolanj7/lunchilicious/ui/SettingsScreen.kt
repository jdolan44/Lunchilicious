package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(onFindOrderClick: () -> Unit, onBackClick: () -> Unit){

    Scaffold(topBar = {
        LunchiliciousTopBar(
        title = "Actions Menu",
        showSettingsBar = false,
        onBackClick = onBackClick
    )}){
        Column(modifier = Modifier.padding(it)){
            CheckoutButton(label = "Find Order Details", onClick = onFindOrderClick)
        }
    }
}