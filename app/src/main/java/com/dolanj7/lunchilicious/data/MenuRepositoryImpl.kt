package com.dolanj7.lunchilicious.data

import com.dolanj7.lunchilicious.data.entity.*
import com.dolanj7.lunchilicious.domain.MenuRepository
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar

class MenuRepositoryImpl(private val menuDb: MenuDatabase) : MenuRepository {

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

    override fun getMenuListStream(): Flow<List<MenuItem>> =
        menuDb.menuItemDao().getMenuList()
    override fun getItemStream(id: Long): Flow<MenuItem?> =
        menuDb.menuItemDao().getItem(id)
    override suspend fun insertItem(item: MenuItem) =
        menuDb.menuItemDao().insert(item)
    override suspend fun deleteItem(item: MenuItem) =
        menuDb.menuItemDao().delete(item)
    override suspend fun updateItem(item: MenuItem) =
        menuDb.menuItemDao().update(item)

    override suspend fun placeOrder(items: List<MenuItem>, totalCost: Double){
        val oid = generateOrderId()
        val order = FoodOrder(oid, generateOrderDate(), totalCost)
        menuDb.foodOrderDao().insert(order)
        //add line items
        var x: Long= 1
        for(item in items){
            menuDb.lineItemDao().insert(LineItem(
                oid,
                x,
                item.id,
                1
            ))
            x++
        }
    }

    override suspend fun refresh() {
        //does nothing for this db...
    }

    override fun getOrderById(id: String): Flow<FoodOrder?> =
        menuDb.foodOrderDao().getFoodOrder(id)


    override fun getLineItemsById(id: String): Flow<List<LineItem>?> =
        menuDb.lineItemDao().getLineItems(id)

}