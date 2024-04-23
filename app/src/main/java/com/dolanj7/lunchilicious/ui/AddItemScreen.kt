package com.dolanj7.lunchilicious.ui

import com.dolanj7.lunchilicious.data.entity.MenuItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddItemScreen(saveItem: (MenuItem) -> Unit, onBackButtonClick: () -> Unit){
    var name by rememberSaveable {mutableStateOf("")}
    var type by rememberSaveable {mutableStateOf("")}
    var description by rememberSaveable {mutableStateOf("")}
    var price by rememberSaveable {mutableStateOf("")}

    InputField(text = "Name", value = name, placeholder = "ex: Burger"){name = it}
    InputField(text = "Type", value = type, placeholder = "ex: Hot"){type = it}
    InputField(text = "Description", value = description, placeholder = ""){description = it}
    InputField(text = "Unit Price", value = price, placeholder = "ex: 2.99"){price = it}

    CheckoutButton(label = "Save Item", modifier = Modifier.fillMaxWidth(.5f)){
        //create a menuItem
        val item = MenuItem(
            type = type,
            name = name,
            description = description,
            unitPrice = price.toDoubleOrNull() ?: 0.0
        )
        //save to DB
        saveItem(item)
    }

    CheckoutButton(label = "Reset", modifier = Modifier.fillMaxWidth(.5f)) {
        name = ""
        type = ""
        description = ""
        price = ""
    }
    CheckoutButton(label = "Back", modifier = Modifier.fillMaxWidth(.5f),
        onClick = onBackButtonClick
    )
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