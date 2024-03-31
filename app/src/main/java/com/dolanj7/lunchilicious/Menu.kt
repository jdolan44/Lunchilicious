package com.dolanj7.lunchilicious

data class MenuItem(val id: Int,
                    val type: String,
                    val name: String,
                    val description: String,
                    val unitPrice: Double)

class Menu(){
    private val menuItems = createList()

    fun getMenuList(): List<MenuItem>{
        return menuItems
    }

    fun getItemById(id: Int): MenuItem{
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