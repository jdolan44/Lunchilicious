package com.dolanj7.lunchilicious

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

@Composable
fun LunchiliciousUI(vm: MenuViewModel){
    val menuList by vm.getMenuListStream().collectAsState(initial = emptyList())
    Column(modifier = Modifier.padding(all = 10.dp)){
        if(vm.onOrderScreen){
            OrderScreen(vm.selected, menuList){
                vm.onOrderScreen = !vm.onOrderScreen
            }
        }
        else{
            val cartList = getCart(vm)
            CartScreen(cartList, getTotalCost(cartList)){
                vm.onOrderScreen = !vm.onOrderScreen
            }
        }
    }
}
@Composable
fun getCart(vm: MenuViewModel): MutableList<MenuItem>{
    val cartList = mutableListOf<MenuItem>()
    vm.selected.forEach{
        val item by vm.getItem(it).collectAsState(initial = null)
        if(item != null){ cartList.add(item!!) }
    }
    return cartList
}

fun getTotalCost(items: MutableList<MenuItem>): Double{
    var totalCost = 0.0
    items.forEach{
        totalCost +=it.unitPrice
    }
    return totalCost
}

@Composable
fun CheckoutButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier){
        Text(label)
    }
}

class MenuViewModel(private val menuRepo : MenuRepository): ViewModel(){
    val selected by mutableStateOf(mutableListOf<Long>())
    var onOrderScreen by mutableStateOf(true)

    fun insertItem(){
        viewModelScope.launch(){
            val item = MenuItem(1, "test", "test food", "desc", 1.00)
            menuRepo.insertItem(item)
        }
    }

    fun getMenuListStream(): Flow<List<MenuItem>> {
        return menuRepo.getMenuListStream()
    }

    fun getItem(id: Long): Flow<MenuItem?>{
        return menuRepo.getItemStream(id)
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