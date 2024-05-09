package com.dolanj7.lunchilicious

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dolanj7.lunchilicious.domain.MenuViewModel
import com.dolanj7.lunchilicious.ui.AddItemScreen
import com.dolanj7.lunchilicious.ui.CartScreen
import com.dolanj7.lunchilicious.ui.OrderScreen

@Composable
fun LunchiliciousNav(vm: MenuViewModel){
    val navController = rememberNavController()
    val menuList by vm.getMenuListStream().collectAsState(initial = emptyList())

    NavHost(navController = navController,
            startDestination = "order"
    ){
        composable("order"){
            OrderScreen(vm.cart,
                menuList,
                onCheckoutClick = {navController.navigate("cart")},
                onAddItemClick = {navController.navigate("additem")}
            )
        }
        composable("cart"){
            val totalCost = vm.getTotalCost()
            CartScreen(vm.cart, totalCost,
                screenSwitch = {navController.popBackStack("order", false)},
                placeOrder = {
                    vm.placeOrder(vm.cart, totalCost)
                }
            )
        }
        composable("additem"){
            AddItemScreen(
                saveItem = {vm.insertMenuItem(it)},
                onBackButtonClick = {navController.popBackStack("order", false)}
            )
        }
    }
}