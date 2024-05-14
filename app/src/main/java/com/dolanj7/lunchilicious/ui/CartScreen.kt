package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dolanj7.lunchilicious.data.entity.*

@Composable
fun CartScreen(cart: MutableList<MenuItem>,
               totalCost: Double,
               onBackClick: () -> Unit,
               onSettingsClick: () -> Unit,
               placeOrder: () -> Unit){
    Scaffold(
        topBar = { LunchiliciousTopBar(
            title = "Shopping Cart",
            showBackButton = true,
            onSettingsClick = onSettingsClick,
            onBackClick = onBackClick
        )}
    ){
        Column(modifier = Modifier.padding(it)){
            LazyColumn(modifier = Modifier.weight(8f)){
                items(items = cart){ item ->
                    CartItem(item)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 5.dp))
            CostDisplay(cost = totalCost, label = "Total: ", modifier = Modifier.weight(2f))
            CheckoutButton("Continue Shopping"){
                onBackClick()
            }
            CheckoutButton("Checkout"){
                placeOrder()
                //move back to order screen after checkout
                onBackClick()
            }
        }
    }
}

@Composable
fun CartItem(item: MenuItem){
    Row (modifier = Modifier.padding(vertical = 5.dp)){
        Text(
            "id: ${item.id}, ${item.type}",
            modifier = Modifier.width(100.dp)
        )
        Text(
            item.name,
            modifier = Modifier.width(150.dp)
        )
        CostDisplay(cost = item.unitPrice)
    }
}