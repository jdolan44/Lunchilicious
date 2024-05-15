package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dolanj7.lunchilicious.data.entity.MenuItem

@Composable
fun CheckoutButton(label: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier, enabled = enabled){
        Text(label)
    }
}

@Composable
fun CostDisplay(label: String = "", cost: Double, modifier: Modifier = Modifier, fontSize: TextUnit = 15.sp){
    val costString = String.format("%.2f", cost)
    Text(modifier = modifier, text = "$label $$costString", fontSize = fontSize)
}

@Composable
fun InputField(text:String, value: String, placeholder: String, onValueChanged: (String) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text) },
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(.7f),
        placeholder = { Text(placeholder) }

    )
}

@Composable
fun CartItem(item: MenuItem){
    Row(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(
            "id: ${item.id}, ${item.type}",
            modifier = Modifier.width(100.dp)
        )
        Text(
            item.name,
            modifier = Modifier.width(150.dp)
        )
        CostDisplay(cost = item.unitPrice)
    }
}