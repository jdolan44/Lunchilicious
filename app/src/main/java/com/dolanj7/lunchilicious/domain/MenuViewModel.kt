package com.dolanj7.lunchilicious.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dolanj7.lunchilicious.data.entity.FoodOrder
import com.dolanj7.lunchilicious.data.entity.LineItem
import com.dolanj7.lunchilicious.MenuApplication
import com.dolanj7.lunchilicious.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MenuViewModel(private val menuRepo : MenuRepository): ViewModel() {
    val cart = mutableStateListOf<MenuItem>()
    var onOrderScreen by mutableStateOf(true)

    fun insertItem() {
        viewModelScope.launch {
            val item = MenuItem(1, "test", "test food", "desc", 1.00)
            menuRepo.insertItem(item)
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
        return menuRepo.getMenuListStream()
    }

    fun placeOrder(items: MutableList<MenuItem>, totalCost: Double) {
        viewModelScope.launch {
            val oid = menuRepo.insertOrder(FoodOrder(totalCost = totalCost))
            var lineNo: Long = 1
            items.forEach {
                val lineItem = LineItem(oid, lineNo, it.id)
                lineNo += 1
                menuRepo.insertLineItem(lineItem)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                            MenuApplication).menuRepository
                MenuViewModel(menuRepo = myRepository)
            }
        }
    }
}