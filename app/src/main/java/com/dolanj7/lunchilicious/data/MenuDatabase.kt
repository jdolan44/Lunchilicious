package com.dolanj7.lunchilicious.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dolanj7.lunchilicious.data.dao.FoodOrderDao
import com.dolanj7.lunchilicious.data.dao.LineItemDao
import com.dolanj7.lunchilicious.data.dao.MenuItemDao
import com.dolanj7.lunchilicious.data.entity.FoodOrder
import com.dolanj7.lunchilicious.data.entity.LineItem
import com.dolanj7.lunchilicious.data.entity.MenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Database(entities = [MenuItem::class, FoodOrder::class, LineItem::class], version = 5, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun foodOrderDao(): FoodOrderDao
    abstract fun lineItemDao(): LineItemDao
    companion object {
        @Volatile
        private var Instance: MenuDatabase? = null
        fun getDatabase(context: Context, prepopulate: Boolean = false): MenuDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    MenuDatabase::class.java, "menu_database")
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            GlobalScope.launch(context = Dispatchers.IO){
                                if(prepopulate){
                                    val studentDao = getDatabase(context).menuItemDao()
                                    prepopulateMenu(studentDao)
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

        private suspend fun prepopulateMenu(menuItemDao: MenuItemDao) {
            //menuItemDao.deleteAll()
            menuItemDao.insert(
                MenuItem(
                    1, "Hoagie",
                    "BLT Hoagie", "Cold, Onion, lettuce, tomato", 6.95
                )
            )
            menuItemDao.insert(
                MenuItem(
                    2, "Hoagie",
                    "Cheese Hoagie", "Cheese, mayo, lettuce, tomato", 6.95
                )
            )
            menuItemDao.insert(
                MenuItem(
                    3, "Pizza",
                    "Plain Pizza", "cheese and tomato", 9.50
                )
            )
            menuItemDao.insert(
                MenuItem(
                    4, "Side",
                    "Fries", "large hot fries", 2.95
                )
            )
            menuItemDao.insert(
                MenuItem(
                    5, "Side",
                    "Gravy Fries", "Fries with gravy on top", 3.95
                )
            )

            menuItemDao.insert(
                MenuItem(
                    6, "Burger",
                    "Cheeseburger", "with lettuce, tomato, onion, american cheese", 6.99
                )
            )

            menuItemDao.insert(
                MenuItem(
                    7, "Hot",
                    "Hot Dog", "with ketchup and mustard", 3.99)
            )

            menuItemDao.insert(
                MenuItem(
                    8, "Hot",
                    "Chicken Sandwich", "with lettuce and tomato", 1.99)
            )

            menuItemDao.insert(
                MenuItem(
                    9, "Side",
                    "Salad", "with Ranch or Caesar dressing",2.49)
            )

            menuItemDao.insert(
                MenuItem(
                    10, "Cold",
                    "Sushi","Spicy Tuna Roll", 5.0)
            )
        }
    }
}