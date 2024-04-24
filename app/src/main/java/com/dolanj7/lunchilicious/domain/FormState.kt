package com.dolanj7.lunchilicious.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dolanj7.lunchilicious.data.entity.MenuItem

//possible next steps: turn this into a hashmap of fields?
//then it could be reused
class FormState: ViewModel() {
    var name by mutableStateOf("")
    var type by mutableStateOf("")
    var description by mutableStateOf("")
    var price by mutableStateOf("")

    fun toMenuItem(): MenuItem {
        return MenuItem(
            type = type,
            name = name,
            description = description,
            unitPrice = price.toDoubleOrNull() ?: 0.0
        )
    }

    fun reset(){
        name = ""
        type = ""
        description = ""
        price = ""
    }
}