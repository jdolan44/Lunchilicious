package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dolanj7.lunchilicious.data.entity.*
import com.dolanj7.lunchilicious.ui.theme.Purple40

@Composable
fun OrderScreen(cart: MutableList<MenuItem>,
                menuList: List<MenuItem>,
                onCheckoutClick: () -> Unit,
                onAddItemClick: () -> Unit,
                onRefreshClick: () -> Unit){
    Scaffold(
        topBar = { LunchiliciousTopBar(
            showBackButton = false,
            onSettingsClick = {},
            onBackClick = {}
        )},
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItemClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            CheckoutButtons(onCheckoutClick, onRefreshClick)
        }
    ) {
        Column(modifier = Modifier.padding(it)){
            MenuItemsList(cart, menuList, Modifier.weight(1f))
        }
    }
}

@Composable
fun MenuItemsList(cart: MutableList<MenuItem>,
                  menuList: List<MenuItem>,
                  modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier){
        items(items = menuList) { item ->
            MenuCard(item, cart.contains(item)){
                if(it){ cart.add(item) }
                else{ cart.remove(item) }
            }
        }
    }
}

@Composable
fun CheckoutButtons(
    onCheckoutClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(modifier= Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround){
        CheckoutButton("View Cart"){
            onCheckoutClick()
        }
        CheckoutButton("Refresh") {
            onRefreshClick()
        }
    }
}

@Composable
fun MenuCard(item: MenuItem, selected: Boolean, onCheckedChange: (Boolean) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)){
            ItemDetails(item)
            if(expanded){Text(item.description, modifier = Modifier.padding(top = 10.dp))}
            MenuCardButtons(selected, expanded, onCheckedChange){
                expanded = !expanded
            }
        }

    }
}
@Composable
fun ItemDetails(item: MenuItem) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ){
        Text(text = item.name, fontSize = 30.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.8f))
        CostDisplay(cost = item.unitPrice, fontSize = 20.sp)
    }
    Text(text = "${item.type}, id: ${item.id}", fontSize = 15.sp)
}
@Composable
fun MenuCardButtons(inCart: Boolean, descriptionExpanded: Boolean,
                    onCartButtonChange: (Boolean) -> Unit,
                    onDescriptionButtonChange: (Boolean) -> Unit){
    Row(modifier = Modifier.padding(top = 10.dp)){
        ToggleButton(
            selected = inCart,
            onCheckedChange = onCartButtonChange,
            enabledContent = {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
                Text("Add Item")
            },
            disabledContent = {
                Icon(Icons.Filled.Close, contentDescription = "Remove Item")
                Text("Remove Item")
            },
            modifier = Modifier
                .weight(.5f)
                .padding(end = 10.dp)
        )
        ToggleButton(
            selected = descriptionExpanded,
            onCheckedChange = onDescriptionButtonChange,
            enabledContent = {
                Text("Show Description")
            },
            disabledContent = {
                Text("Hide Description")
            },
            modifier = Modifier.weight(.5f)
        )
    }
}
@Composable
fun ToggleButton(selected: Boolean,
                 onCheckedChange: (Boolean) -> Unit,
                 enabledContent:  @Composable() (RowScope.() -> Unit),
                 disabledContent:  @Composable() (RowScope.() -> Unit),
                 modifier :Modifier = Modifier){
    var checked by remember { mutableStateOf(selected) }
    if(!checked){
        OutlinedButton(
            onClick = { checked = true
                        onCheckedChange(true) },
            border = BorderStroke(1.dp, Purple40),
            modifier = modifier
        ){
            enabledContent()
        }
    }
    else{
        Button(
            onClick = { checked = false
                        onCheckedChange(false) },
            modifier = modifier
        ) {
            disabledContent()
        }
    }
}

@Composable
@Preview
fun ToggleButtonPreview(){
    ToggleButton(true,
        onCheckedChange = {},
        enabledContent = {Text("Hello!")},
        disabledContent = {Text("Goodbye!")}
    )
}

@Composable
@Preview
fun MenuCardPreview(){
    val testItem = MenuItem(0, "test", "My Item", "description here!", 0.99)
    MenuCard(testItem, false){}
}


//Original composables (UNUSED)
@Composable
fun MenuCardOriginal(item: MenuItem, selected: Boolean, onCheckedChange: (Boolean) -> Unit){
    var expanded by remember{ mutableStateOf(false) }
    var checked by remember { mutableStateOf(selected) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row (modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            DescriptionToggle(expanded){ expanded = !expanded }
            ItemHeader(item, modifier = Modifier
                .padding(start = 10.dp)
                .weight(7f))

            CostDisplay(cost = item.unitPrice, modifier = Modifier.weight(2f))
            //order checkbox
            Checkbox(modifier = Modifier.weight(1f),
                checked = checked,
                onCheckedChange = {
                    onCheckedChange(it)
                    checked = it
                })

        }
        DescriptionBar(expanded, item.description)
    }
}

@Composable
fun DescriptionToggle(expanded: Boolean, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.size(30.dp, 30.dp),
        onClick = {onClick()},
    ) {
        if(expanded){
            Icon(Icons.Filled.KeyboardArrowUp, "Hide description.")
        }
        else{
            Icon(Icons.Filled.KeyboardArrowDown, "Show description.")
        }
    }
}

@Composable
fun ItemHeader(item: MenuItem, modifier: Modifier = Modifier){
    Column(modifier = modifier){
        Text(
            text = item.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text("id: ${item.id}, ${item.type}")
    }
}

@Composable
fun DescriptionBar(expanded: Boolean, description: String){
    if(expanded){
        Divider()
        Text(modifier = Modifier.padding(5.dp), text = description)
    }
}

@Composable
@Preview
fun MenuCardOriginalPreview(){
    val testItem = MenuItem(0, "test", "My Item", "description here!", 0.99)
    MenuCardOriginal(testItem, false){}
}