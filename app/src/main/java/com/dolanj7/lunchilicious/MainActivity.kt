package com.dolanj7.lunchilicious

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
            LunchiliciousTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 20.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //menu item list-- save this in the viewmodel!
                    val myViewModel: MyViewModel by viewModels()


                    LunchiliciousUI(myViewModel)

                }
            }
        }
    }
}

@Composable
fun LunchiliciousUI(myViewModel: MyViewModel){
    Column{
        CheckoutButton(myViewModel){
            myViewModel.onOrderScreen = !myViewModel.onOrderScreen
        }
        if(myViewModel.onOrderScreen){
            OrderScreen(myViewModel)
        }
        else{
            CartScreen(myViewModel)
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
        Text("$" + String.format("%.2f", item.unitPrice))
    }
}


@Composable
fun CartScreen(myViewModel: MyViewModel){

    LazyColumn{
        items(items = myViewModel.selected){ id ->
            val item = myViewModel.menu.getItemById(id)
            CartItem(item)
        }
    }

    Divider(modifier = Modifier.padding(vertical = 5.dp))
    //total cost
    myViewModel.updateTotalCost()
    Text("Total: $" + String.format("%.2f", myViewModel.totalCost))
}
@Composable
fun CheckoutButton(myViewModel: MyViewModel, onClick: () -> Unit) {
    Button(onClick = onClick){
        if(myViewModel.onOrderScreen){
            Text("Place Order")
        }
        else{
            Text("Continue Shopping")
        }
    }
}

@Composable
fun OrderScreen(myViewModel: MyViewModel){
    LazyColumn() {
        items(items = myViewModel.menu.getMenuList()) { item ->
            MenuCard(item, myViewModel)
        }
    }
}
@Composable
fun MenuCard(item: MenuItem, myViewModel: MyViewModel){
    var checked by remember { mutableStateOf(myViewModel.selected.contains(item.id)) }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
    ) {
        Row (modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(.7f)){
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "id: ${item.id}, ${item.type}"
                )
            }
            Text("$" + String.format("%.2f", item.unitPrice))
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    if(it){
                        myViewModel.selected.add(item.id)
                    }
                    else{
                        myViewModel.selected.remove(item.id)
                    }
                    checked = it
                })
        }
    }
}

//ViewModel
class MyViewModel: ViewModel(){
    var menu by mutableStateOf(Menu())
    val selected by mutableStateOf(mutableListOf<Int>())
    var onOrderScreen by mutableStateOf(true)
    var totalCost by mutableDoubleStateOf(0.0)

    fun updateTotalCost(){
        totalCost = 0.0
        for(id in selected){
            val item = menu.getItemById(id)

            totalCost +=item.unitPrice
        }
    }

}


data class MenuItem(val id: Int,
                    val type: String,
                    val name: String,
                    val description: String,
                    val unitPrice: Double)

class Menu(){
    private val menuItems = createList()

    fun getMenuList(): List<MenuItem>{
        return menuItems
    }

    fun getItemById(id: Int): MenuItem{
        for(item in menuItems){
            if(item.id == id){
                return item
            }
        }
        return MenuItem(-1, "", "error", "error", 0.0)
    }
    private fun createList(): List<MenuItem>{

        val items = mutableListOf<MenuItem>()

        items+=MenuItem(
            1,
            "Hot",
            "Burger",
            "with Lettuce and Tomato",
            6.99)

        items+=MenuItem(
            2,
            "Hot",
            "Hot Dog",
            "",
            3.99)

        items+=MenuItem(
            3,
            "Side",
            "French Fries",
            "",
            1.99)

        items+=MenuItem(
            4,
            "Side",
            "Salad",
            "with Ranch or Caesar dressing",
            2.49
        )

        items+=MenuItem(
            5,
            "Cold",
            "Sushi",
            "Spicy Tuna Roll",
            5.0
        )

        items+=MenuItem(
            6,
            "Hot",
            "Chicken Sandwich",
            "",
            3.66
        )

        return items
    }
}
