package com.dolanj7.lunchilicious.ui

import androidx.compose.foundation.layout.Column
import com.dolanj7.lunchilicious.data.entity.MenuItem
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dolanj7.lunchilicious.domain.FormState

@Composable
fun AddItemScreen(saveItem: (MenuItem) -> Unit, onBackButtonClick: () -> Unit){
    Column(modifier = Modifier.padding(all = 10.dp)) {
        val formState: FormState = viewModel()
        InputField(
            text = "Name",
            value = formState.name,
            placeholder = "ex: Burger"
        ) { formState.name = it }
        InputField(
            text = "Type",
            value = formState.type,
            placeholder = "ex: Hot"
        ) { formState.type = it }
        InputField(
            text = "Description",
            value = formState.description,
            placeholder = ""
        ) { formState.description = it }
        InputField(
            text = "Unit Price",
            value = formState.price,
            placeholder = "ex: 2.99"
        ) { formState.price = it }

        CheckoutButton(label = "Save Item", modifier = Modifier.fillMaxWidth(.5f)) {
            val item = formState.toMenuItem()
            saveItem(item)
            onBackButtonClick()
        }

        CheckoutButton(label = "Reset", modifier = Modifier.fillMaxWidth(.5f)) {
            formState.reset()
        }
        CheckoutButton(
            label = "Back", modifier = Modifier.fillMaxWidth(.5f),
            onClick = onBackButtonClick
        )
    }
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