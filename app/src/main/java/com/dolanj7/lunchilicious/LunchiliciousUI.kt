package com.dolanj7.lunchilicious

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dolanj7.lunchilicious.domain.MenuViewModel
import com.dolanj7.lunchilicious.ui.AddItemScreen
import com.dolanj7.lunchilicious.ui.CartScreen
import com.dolanj7.lunchilicious.ui.OrderScreen

@Composable
fun LunchiliciousUI(vm: MenuViewModel){
    val menuList by vm.getMenuListStream().collectAsState(initial = emptyList())
    Column(modifier = Modifier.padding(all = 10.dp)){
        when (vm.currentScreen) {
            "order" -> {
                OrderScreen(vm.cart,
                    menuList,
                    onCheckoutClick = {vm.currentScreen = "cart"},
                    onAddItemClick = {vm.currentScreen = "addItem"}
                )
            }
            "cart" -> {
                val totalCost = vm.getTotalCost()
                CartScreen(vm.cart, totalCost,
                    screenSwitch = {vm.currentScreen = "order"},
                    placeOrder = {
                        vm.placeOrder(vm.cart, totalCost)
                    })
            }
            "addItem" -> {
                AddItemScreen(
                    saveItem = {vm.insertMenuItem(it)},
                    onBackButtonClick = {vm.currentScreen = "order"}
                )
            }
        }
    }
}

