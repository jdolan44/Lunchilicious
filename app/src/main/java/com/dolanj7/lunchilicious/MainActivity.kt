package com.dolanj7.lunchilicious

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dolanj7.lunchilicious.data.MenuDatabase
import com.dolanj7.lunchilicious.data.MenuRepositoryImpl
import com.dolanj7.lunchilicious.domain.MenuViewModel
import com.dolanj7.lunchilicious.ui.theme.LunchiliciousTheme
import com.dolanj7.lunchilicious.domain.MenuRepository

class MenuApplication : Application() {
    lateinit var menuRepository: MenuRepository
    override fun onCreate() {
        super.onCreate()
        menuRepository =
            MenuRepositoryImpl(MenuDatabase.getDatabase(this))
    }
}

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
                    val menuVm: MenuViewModel = viewModel(factory = MenuViewModel.Factory)
                    LunchiliciousUI(menuVm)
                }
            }
        }
    }
}

