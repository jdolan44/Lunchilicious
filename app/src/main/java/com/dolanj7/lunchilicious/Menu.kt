package com.dolanj7.lunchilicious

import android.content.Context
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
    @PrimaryKey
    val id: Int = 0,
    val type: String,
    val name: String,
    val description: String,
    val unitPrice: Double
)
@Dao
interface MenuDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuItem)
    @Update
    suspend fun update(item: MenuItem)
    @Delete
    suspend fun delete(item: MenuItem)
    @Query("Delete from menu_item")
    suspend fun deleteAll()
    @Query("SELECT * from menu_item WHERE id = :id")
    fun getItem(id: Int): Flow<MenuItem>
    @Query("SELECT * from menu_item ORDER BY id ASC")
    fun getMenuList(): Flow<List<MenuItem>>
}

//TODO look at step 7 for adding multiple tables!
@Database(entities = [MenuItem::class], version = 1, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
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
// moving to a new thread
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

            menuDao.insert(MenuItem(
                10, "Cold",
                "Sushi","Spicy Tuna Roll", 5.0)
            )
        }
    }
}

interface MenuRepository {
    fun getMenuListStream(): Flow<List<MenuItem>>
    fun getItemStream(id: Int): Flow<MenuItem?>
    suspend fun insertItem(item: MenuItem)
    suspend fun deleteItem(item: MenuItem)
    suspend fun updateItem(item: MenuItem)
}

class MenuRepositoryImpl(private val menuDb: MenuDatabase) : MenuRepository {
    override fun getMenuListStream(): Flow<List<MenuItem>> =
        menuDb.menuDao().getMenuList()
    override fun getItemStream(id: Int): Flow<MenuItem?> =
        menuDb.menuDao().getItem(id)
    override suspend fun insertItem(student: MenuItem) =
        menuDb.menuDao().insert(student)
    override suspend fun deleteItem(student: MenuItem) =
        menuDb.menuDao().delete(student)
    override suspend fun updateItem(student: MenuItem) =
        menuDb.menuDao().update(student)
}

class Menu(){
    private val menuItems = createList()

    fun getMenuList(): List<MenuItem>{
        return menuItems
    }

    fun getItem(id: Int): MenuItem{
        for(item in menuItems){
            if(item.id == id){
                return item
            }
        }
        return MenuItem(-1, "", "error", "error", 0.0)
    }
    private fun createList(): List<MenuItem>{

        val items = mutableListOf<MenuItem>()

        items.add(
            MenuItem(
                1, "Hoagie",
                "BLT Hoagie", "Cold, Onion, lettuce, tomato", 6.95
            )
        )
        items.add(
            MenuItem(
                2, "Hoagie",
                "Cheese Hoagie", "Cheese, mayo, lettuce, tomato", 6.95
            )
        )
        items.add(
            MenuItem(
                3, "Pizza",
                "Plain Pizza", "cheese and tomato", 9.50
            )
        )
        items.add(
            MenuItem(
                4, "Side",
                "Fries", "large hot fries", 2.95
            )
        )
        items.add(
            MenuItem(
                5, "Side",
                "Gravy Fries", "Fries with gravy on top", 3.95
            )
        )

        items.add(
            MenuItem(
            6, "Burger",
            "Cheeseburger", "with lettuce, tomato, onion, american cheese", 6.99
            )
        )

        items.add(
            MenuItem(
            7, "Hot",
            "Hot Dog", "with ketchup and mustard", 3.99)
        )

        items.add(
            MenuItem(
            8, "Hot",
            "Chicken Sandwich", "with lettuce and tomato", 1.99)
        )

        items.add(
            MenuItem(
            9, "Side",
            "Salad", "with Ranch or Caesar dressing",2.49)
        )

        items.add(MenuItem(
            10, "Cold",
            "Sushi","Spicy Tuna Roll", 5.0)
        )

        return items
    }
}