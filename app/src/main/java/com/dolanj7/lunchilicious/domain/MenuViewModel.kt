package com.dolanj7.lunchilicious.domain

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dolanj7.lunchilicious.MenuApplication
import com.dolanj7.lunchilicious.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MenuViewModel(private val menuRepository : MenuRepository): ViewModel() {
    val cart = mutableStateListOf<MenuItem>()

    fun insertMenuItem(item: MenuItem) {
        viewModelScope.launch {
            menuRepository.insertItem(item)
        }
    }

    fun getTotalCost(): Double{
        var totalCost = 0.0
        cart.forEach{
            totalCost +=it.unitPrice
        }
        return totalCost
    }

    fun getMenuListStream(): Flow<List<MenuItem>> {
        return menuRepository.getMenuListStream()
    }

    fun getMenuItemStream(id: Long): Flow<MenuItem?>{
        return menuRepository.getItemStream(id)
    }

    fun placeOrder(items: MutableList<MenuItem>, totalCost: Double) {
        viewModelScope.launch {
            menuRepository.placeOrder(items, totalCost)
        }
    }

    fun refresh(){
        viewModelScope.launch {
            menuRepository.refresh()
        }
    }

    fun getOrderById(id: String): Flow<FoodOrder?> = menuRepository.getOrderById(id)

    fun getLineItemsById(id: String): Flow<List<LineItem>?> = menuRepository.getLineItemsById(id)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                        MenuApplication)
                val myRepo =application.menuRepo
                MenuViewModel(myRepo)
            }
        }
    }
}