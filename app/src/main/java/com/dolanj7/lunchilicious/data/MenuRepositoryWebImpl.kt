package com.dolanj7.lunchilicious.data

import com.dolanj7.lunchilicious.data.client.MenuItemClient
import com.dolanj7.lunchilicious.data.entity.FoodOrder
import com.dolanj7.lunchilicious.data.entity.LineItem
import com.dolanj7.lunchilicious.data.entity.MenuItem
import com.dolanj7.lunchilicious.data.entity.MenuItemRetrofit
import com.dolanj7.lunchilicious.domain.MenuRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuRepositoryWebImpl(private val menuDb: MenuDatabase) : MenuRepository {

    val baseUrl = "http://aristotle.cs.scranton.edu/"
    private val menuClient: MenuItemClient

    init {
        // create a retrofit object
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        // create a Client object for MarsApiService
        menuClient = retrofit.create(MenuItemClient::class.java)
        populateDBFromWeb()
    }
    private fun populateDBFromWeb(){
        GlobalScope.launch{
            menuClient.getMenuItems().map{
                val item = convertMenuItem(it)
                menuDb.menuItemDao().insert(item)
            }
        }
    }
    private fun convertMenuItem(item: MenuItemRetrofit): MenuItem{
        return MenuItem(item.id.toLong(), item.type, item.name, item.description, item.unitPrice)
    }

    private fun convertMenuItem(item: MenuItem): MenuItemRetrofit{
        return MenuItemRetrofit(-1, item.name, item.type, item.description, item.unitPrice)
    }

    override fun getMenuListStream(): Flow<List<MenuItem>>{
        return menuDb.menuItemDao().getMenuList()
    }
    override fun getItemStream(id: Long): Flow<MenuItem?> =
        menuDb.menuItemDao().getItem(id)
    override suspend fun insertItem(item: MenuItem): Long{
        val retroItem = menuClient.addMenuItem(convertMenuItem(item))
        return menuDb.menuItemDao().insert(convertMenuItem(retroItem))
    }
    override suspend fun deleteItem(item: MenuItem) =
        menuDb.menuItemDao().delete(item)
    override suspend fun updateItem(item: MenuItem) =
        menuDb.menuItemDao().update(item)

    override suspend fun insertOrder(order: FoodOrder): Long {
        return menuDb.foodOrderDao().insert(order)
    }
    override suspend fun insertLineItem(item: LineItem) {
        menuDb.lineItemDao().insert(item)
    }


}