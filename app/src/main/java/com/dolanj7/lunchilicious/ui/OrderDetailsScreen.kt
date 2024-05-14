package com.dolanj7.lunchilicious.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dolanj7.lunchilicious.data.entity.FoodOrderRetrofit
import com.dolanj7.lunchilicious.data.entity.LineItemRetrofit
import kotlinx.coroutines.flow.Flow

@Composable
fun OrderDetailsScreen(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    findOrder: (String) -> Flow<FoodOrderRetrofit?>,
    findLineItems: (String) -> Flow<List<LineItemRetrofit>?>
){
    var id by rememberSaveable { mutableStateOf("") }
    val order: Flow<FoodOrderRetrofit?> = findOrder(id)
    val lineItems: Flow<List<LineItemRetrofit>?> = findLineItems(id)

    Scaffold(topBar = { LunchiliciousTopBar(
        title = "Find Order Details",
        onSettingsClick = onSettingsClick,
        onBackClick = onBackClick
    )}){
        Column(modifier = Modifier.padding(it)) {
            InputField(text = "Order ID", value = id, placeholder = "", onValueChanged = {id = it})
            Text("Enter order ID and the details will appear here!")
            if(id != ""){
                OrderDisplay(order)
                LineItemsDisplay(lineItems)
            }
        }
    }
}

@Composable
fun OrderDisplay(order: Flow<FoodOrderRetrofit?>) {
    val collected by order.collectAsState(initial = null)
    if(collected == null){
        Text("no order found!")
    }
    else{
        Text(collected.toString())
    }
}

@Composable
fun LineItemsDisplay(lineItems: Flow<List<LineItemRetrofit>?>){
    val collected by lineItems.collectAsState(initial = emptyList())
    if((collected?.size ?: 0) == 0){
        Text("No line items found!")
    }
    else{
        Text(collected.toString())
    }
}
