package com.dolanj7.lunchilicious.data

import com.dolanj7.lunchilicious.data.entity.*
import com.dolanj7.lunchilicious.domain.MenuRepository
import kotlinx.coroutines.flow.Flow

class MenuRepositoryImpl(private val menuDb: MenuDatabase) : MenuRepository {
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

    override suspend fun insertOrder(order: FoodOrder): Long {
        return menuDb.foodOrderDao().insert(order)
    }
    override suspend fun insertLineItem(item: LineItem) {
        menuDb.lineItemDao().insert(item)
    }

}