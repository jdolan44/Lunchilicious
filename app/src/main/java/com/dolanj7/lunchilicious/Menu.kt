package com.dolanj7.lunchilicious

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Entity(tableName = "menu_item")
data class MenuItem(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val name: String,
    val description: String,
    val unitPrice: Double
)
@Dao
interface MenuDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuItem) : Long
    @Update
    suspend fun update(item: MenuItem)
    @Delete
    suspend fun delete(item: MenuItem)
    @Query("Delete from menu_item")
    suspend fun deleteAll()
    @Query("SELECT * from menu_item WHERE id = :id")
    fun getItem(id: Long): Flow<MenuItem>
    @Query("SELECT * from menu_item ORDER BY id ASC")
    fun getMenuList(): Flow<List<MenuItem>>
}
@Entity(tableName = "food_order")
data class FoodOrder(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val totalCost: Double
)
@Dao
interface FoodOrderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: FoodOrder) : Long
    @Update
    suspend fun update(order: FoodOrder)
    @Delete
    suspend fun delete(order: FoodOrder)
    @Query("Delete from food_order")
    suspend fun deleteAll()
}
@Entity(tableName = "line_item", primaryKeys = ["oid", "lineNo"])
data class LineItem(
    val oid: Long = 0,
    val lineNo: Long,
    val itemId: Long = 0
)

@Dao
interface LineItemDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LineItem) : Long
    @Update
    suspend fun update(item: LineItem)
    @Delete
    suspend fun delete(item: LineItem)
    @Query("Delete from line_item")
    suspend fun deleteAll()
}
@Database(entities = [MenuItem::class, FoodOrder::class, LineItem::class], version = 3, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
    abstract fun foodOrderDao(): FoodOrderDao
    abstract fun lineItemDao(): LineItemDao
    companion object {
        @Volatile
        private var Instance: MenuDatabase? = null
        fun getDatabase(context: Context): MenuDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    MenuDatabase::class.java, "menu_database")
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            GlobalScope.launch(context = Dispatchers.IO){
                                val studentDao = getDatabase(context).menuDao()
                                prepopulateMenu(studentDao)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

        private suspend fun prepopulateMenu(menuDao: MenuDao) {
            menuDao.deleteAll()
            menuDao.insert(
                MenuItem(
                    1, "Hoagie",
                    "BLT Hoagie", "Cold, Onion, lettuce, tomato", 6.95
                )
            )
            menuDao.insert(
                MenuItem(
                    2, "Hoagie",
                    "Cheese Hoagie", "Cheese, mayo, lettuce, tomato", 6.95
                )
            )
            menuDao.insert(
                MenuItem(
                    3, "Pizza",
                    "Plain Pizza", "cheese and tomato", 9.50
                )
            )
            menuDao.insert(
                MenuItem(
                    4, "Side",
                    "Fries", "large hot fries", 2.95
                )
            )
            menuDao.insert(
                MenuItem(
                    5, "Side",
                    "Gravy Fries", "Fries with gravy on top", 3.95
                )
            )

            menuDao.insert(
                MenuItem(
                    6, "Burger",
                    "Cheeseburger", "with lettuce, tomato, onion, american cheese", 6.99
                )
            )

            menuDao.insert(
                MenuItem(
                    7, "Hot",
                    "Hot Dog", "with ketchup and mustard", 3.99)
            )

            menuDao.insert(
                MenuItem(
                    8, "Hot",
                    "Chicken Sandwich", "with lettuce and tomato", 1.99)
            )

            menuDao.insert(
                MenuItem(
                    9, "Side",
                    "Salad", "with Ranch or Caesar dressing",2.49)
            )

            menuDao.insert(
                MenuItem(
                    10, "Cold",
                    "Sushi","Spicy Tuna Roll", 5.0)
            )
        }
    }
}

interface MenuRepository {
    fun getMenuListStream(): Flow<List<MenuItem>>
    fun getItemStream(id: Long): Flow<MenuItem?>
    suspend fun insertItem(item: MenuItem): Long
    suspend fun deleteItem(item: MenuItem)
    suspend fun updateItem(item: MenuItem)
    suspend fun insertOrder(order: FoodOrder) : Long
    suspend fun insertLineItem(item: LineItem)
}

class MenuRepositoryImpl(private val menuDb: MenuDatabase) : MenuRepository {
    override fun getMenuListStream(): Flow<List<MenuItem>> =
        menuDb.menuDao().getMenuList()
    override fun getItemStream(id: Long): Flow<MenuItem?> =
        menuDb.menuDao().getItem(id)
    override suspend fun insertItem(item: MenuItem) =
        menuDb.menuDao().insert(item)
    override suspend fun deleteItem(item: MenuItem) =
        menuDb.menuDao().delete(item)
    override suspend fun updateItem(item: MenuItem) =
        menuDb.menuDao().update(item)

    override suspend fun insertOrder(order: FoodOrder): Long {
        return menuDb.foodOrderDao().insert(order)
    }
    override suspend fun insertLineItem(item: LineItem) {
        menuDb.lineItemDao().insert(item)
    }

}