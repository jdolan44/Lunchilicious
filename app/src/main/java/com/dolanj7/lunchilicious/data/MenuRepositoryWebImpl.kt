package com.dolanj7.lunchilicious.data

import android.util.Log
import com.dolanj7.lunchilicious.data.client.MenuItemClient
import com.dolanj7.lunchilicious.data.client.OrderClient
import com.dolanj7.lunchilicious.data.entity.FoodOrderRetrofit
import com.dolanj7.lunchilicious.data.entity.LineItemRetrofit
import com.dolanj7.lunchilicious.data.entity.MenuItem
import com.dolanj7.lunchilicious.data.entity.MenuItemRetrofit
import com.dolanj7.lunchilicious.domain.MenuRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar

class MenuRepositoryWebImpl(private val menuDb: MenuDatabase) : MenuRepository {

    val baseUrl = "http://aristotle.cs.scranton.edu/"
    private val menuClient: MenuItemClient
    private val orderClient: OrderClient

    init {
        // create a retrofit object
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        menuClient = retrofit.create(MenuItemClient::class.java)
        orderClient = retrofit.create(OrderClient::class.java)
        //GlobalScope.launch{
        //    refresh()
        //}
    }
    override suspend fun refresh(){
        menuClient.getMenuItems().map {
            val item = convertMenuItem(it)
            menuDb.menuItemDao().insert(item)
        }
        Log.i("REFRESH", "refresh success!")
    }
    private fun convertMenuItem(item: MenuItemRetrofit): MenuItem{
        return MenuItem(item.id.toLong(), item.type, item.name, item.description, item.unitPrice)
    }

    private fun convertMenuItem(item: MenuItem): MenuItemRetrofit{
        return MenuItemRetrofit(-1, item.name, item.type, item.description, item.unitPrice)
    }
    private fun generateOrderId(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MM-dd-HH-mm-ss")
        return "dolanj7-"+formatter.format(time)
    }

    private fun generateOrderDate(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
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

    override suspend fun placeOrder(items: List<MenuItem>, totalCost: Double) {
        val orderId = generateOrderId()
        val orderDate = generateOrderDate()
        val order = FoodOrderRetrofit(orderId, orderDate, totalCost)
        //generate line items
        var lineNumber = 0
        val lineItems: List<LineItemRetrofit> = items.map{
            lineNumber++
            LineItemRetrofit(orderId, lineNumber, it.id.toInt(), 1)
        }
        //add order and lineItems to web services
        orderClient.addOrder(order)
        orderClient.addLineItems(lineItems)
    }

}