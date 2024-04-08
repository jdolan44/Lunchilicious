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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@Composable
fun LunchiliciousUI(vm: MyViewModel, menuVm: MenuViewModel){
    val menuList by menuVm.getMenuListStream().collectAsState(initial = emptyList())
    Column(modifier = Modifier.padding(all = 10.dp)){
        if(vm.onOrderScreen){
            OrderScreen(vm.selected, menuList){
                vm.onOrderScreen = !vm.onOrderScreen
            }
        }
        else{
            //TODO get list of selected items first, then pass into CartScreen
            val cartList = mutableListOf<MenuItem>()
            vm.selected.forEach{
                val item by menuVm.getItem(it).collectAsState(initial = null)
                if(item != null){
                    cartList.add(item!!)
                }
            }
            CartScreen(cartList, vm.getTotalCost()){
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

    fun getMenuListStream(): Flow<List<MenuItem>> {
        return menuRepo.getMenuListStream()
    }

    fun getItem(id: Int): Flow<MenuItem?>{
        return menuRepo.getItemStream(id)
    }

    suspend fun getCartItems(selected: MutableList<Int>){

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