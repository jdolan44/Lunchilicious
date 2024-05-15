package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dolanj7.lunchilicious.data.entity.FoodOrderRetrofit
import com.dolanj7.lunchilicious.data.entity.LineItemRetrofit
import com.dolanj7.lunchilicious.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow

@Composable
fun OrderDetailsScreen(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    findOrder: (String) -> Flow<FoodOrderRetrofit?>,
    findLineItems: (String) -> Flow<List<LineItemRetrofit>?>,
    getItemById: (Long) -> Flow<MenuItem?>
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
            Text("Enter order ID and the details will appear below!")
            Divider(modifier = Modifier.padding(5.dp))
            if(id != ""){
                OrderDisplay(order)
                LineItemsDisplay(lineItems, getItemById)
            }
        }
    }
}

@Composable
fun OrderDisplay(order: Flow<FoodOrderRetrofit?>) {
    val collected by order.collectAsState(initial = null)
    collected?.let {
        Text("Order date: ${it.orderDate}")
        CostDisplay("Total cost:", it.totalCost, modifier = Modifier.padding(bottom = 20.dp))
        }
    ?: Text("No Order Found!")
}

@Composable
fun LineItemsDisplay(lineItems: Flow<List<LineItemRetrofit>?>, getItemById: (Long) -> Flow<MenuItem?>){
    val collected by lineItems.collectAsState(initial = emptyList())
    collected?.forEach{
        val item by getItemById(it.itemId.toLong()).collectAsState(initial = null)
        item?.let{ CartItem(it)}
            ?: Text("item not found error!")
    }?: Text("No line items found!")
}
