package com.dolanj7.lunchilicious

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dolanj7.lunchilicious.domain.MenuViewModel
import com.dolanj7.lunchilicious.ui.AddItemScreen
import com.dolanj7.lunchilicious.ui.CartScreen
import com.dolanj7.lunchilicious.ui.OrderDetailsScreen
import com.dolanj7.lunchilicious.ui.OrderScreen
import com.dolanj7.lunchilicious.ui.SettingsScreen

@Composable
fun LunchiliciousNavHost(vm: MenuViewModel, navController: NavHostController){
    val menuList by vm.getMenuListStream().collectAsState(initial = emptyList())
    val onSettingsClick = {navController.navigate("settings")}
    NavHost(navController = navController,
            startDestination = "order"
    ){
        composable("order"){
            OrderScreen(vm.cart,
                menuList,
                onCheckoutClick = {navController.navigate("cart")},
                onAddItemClick = {navController.navigate("additem")},
                onRefreshClick = {vm.refresh()},
                onSettingsClick = onSettingsClick
            )
        }
        composable("cart"){
            val totalCost = vm.getTotalCost()
            CartScreen(vm.cart, totalCost,
                onBackClick = {navController.popBackStack("order", false)},
                placeOrder = {
                    vm.placeOrder(vm.cart, totalCost)
                },
                onSettingsClick = onSettingsClick
            )
        }
        composable("additem"){
            AddItemScreen(
                saveItem = {vm.insertMenuItem(it)},
                onBackButtonClick = {navController.popBackStack("order", false)},
                onSettingsClick = onSettingsClick
            )
        }
        composable("findorder"){
            OrderDetailsScreen(
                onSettingsClick = {navController.popBackStack("settings", false)},
                onBackClick = {navController.popBackStack("settings", false)},
                findOrder = {vm.getOrderById(it)},
                findLineItems = {vm.getLineItemsById(it)},
                getItemById = {vm.getMenuItemStream(it)}
            )
        }
        composable("settings"){
            SettingsScreen(
                onFindOrderClick = {navController.navigate("findorder")},
                onBackClick = {navController.popBackStack("order", false)}
                )
        }
    }
}