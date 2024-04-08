package com.dolanj7.lunchilicious

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderScreen(selectedIDs: MutableList<Long>, menuList: List<MenuItem>, screenSwitch: () -> Unit){
    Column{
        //TODO make this look better
        Text("Lunchilicious (database version)", modifier = Modifier.weight(1f))
        LazyColumn(modifier = Modifier.weight(15f)) {
            items(items = menuList) { item ->
                MenuCard(item, selectedIDs.contains(item.id)){
                    if(it){ selectedIDs.add(item.id) }
                    else{ selectedIDs.remove(item.id) }
                }
            }
        }
        CheckoutButton("View Cart"){
            screenSwitch()
        }
    }
}


@Composable
fun MenuCard(item: MenuItem, selected: Boolean, onCheckedChange: (Boolean) -> Unit){
    var expanded by remember{ mutableStateOf(false) }
    var checked by remember { mutableStateOf(selected) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row (modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            DescriptionToggle(expanded){ expanded = !expanded }
            ItemHeader(item, modifier =  Modifier.padding(start = 10.dp).weight(7f))

            //cost display
            //TODO make this its own method (and reuse it for cart screen)
            Text(modifier = Modifier.weight(2f), text = "$" + String.format("%.2f", item.unitPrice))

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
