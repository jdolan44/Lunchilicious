package com.dolanj7.lunchilicious

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.dolanj7.lunchilicious.ui.theme.LunchiliciousTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunchiliciousTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm: MyViewModel by viewModels()
                    LunchiliciousUI(vm)

                }
            }
        }
    }
}

@Composable
fun LunchiliciousUI(vm: MyViewModel){
    Column(modifier = Modifier.padding(all = 10.dp)){
        if(vm.onOrderScreen){
            OrderScreen(vm.selected, vm.menu){
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

//ViewModel
//TODO make the viewModel its own file

class MyViewModel: ViewModel(){
    var menu by mutableStateOf(Menu())
    val selected by mutableStateOf(mutableListOf<Int>()) //TODO figure this out???
    var onOrderScreen by mutableStateOf(true)

    fun getTotalCost(): Double{
        var totalCost = 0.0
        for(id in selected){
            val item = menu.getItemById(id)
            totalCost +=item.unitPrice
        }
        return totalCost
    }

}
