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
fun LunchiliciousUI(vm: MyViewModel, menuVm: MenuViewModel){
    val menuList by menuVm.getMenuList().collectAsState(initial = emptyList())
    Column(modifier = Modifier.padding(all = 10.dp)){
        if(vm.onOrderScreen){
            OrderScreen(vm.selected, menuList){
                vm.onOrderScreen = !vm.onOrderScreen
            }
        }
        else{
            CartScreen(vm.selected, vm.menu, vm.getTotalCost()){
                vm.onOrderScreen = !vm.onOrderScreen
            }
        }
    }
}

@Composable
fun CheckoutButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier){
        Text(label)
    }
}

class MyViewModel: ViewModel(){
    var menu by mutableStateOf(Menu())
    val selected by mutableStateOf(mutableListOf<Int>())
    var onOrderScreen by mutableStateOf(true)

    fun getTotalCost(): Double{
        var totalCost = 0.0
        for(id in selected){
            val item = menu.getItem(id)
            totalCost +=item.unitPrice
        }
        return totalCost
    }

}

class MenuViewModel(private val menuRepo : MenuRepository): ViewModel(){
    fun insertItem(){
        viewModelScope.launch(){
            val item = MenuItem(1, "test", "test food", "desc", 1.00)
            menuRepo.insertItem(item)
        }
    }

    fun getMenuList(): Flow<List<MenuItem>> {
        return menuRepo.getMenuListStream()
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