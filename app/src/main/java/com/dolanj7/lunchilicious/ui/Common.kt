package com.dolanj7.lunchilicious.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier){
        Text(label)
    }
}

@Composable
fun CostDisplay(label: String = "", cost: Double, modifier: Modifier = Modifier, fontSize: TextUnit = 15.sp){
    val costString = String.format("%.2f", cost)
    Text(modifier = modifier, text = "$label $$costString", fontSize = fontSize)
}