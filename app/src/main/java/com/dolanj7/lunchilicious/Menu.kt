package com.dolanj7.lunchilicious

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

        items+=MenuItem(
            1,
            "Hot",
            "Burger",
            "with Lettuce and Tomato",
            6.99)

        items+=MenuItem(
            2,
            "Hot",
            "Hot Dog",
            "",
            3.99)

        items+=MenuItem(
            3,
            "Side",
            "French Fries",
            "",
            1.99)

        items+=MenuItem(
            4,
            "Side",
            "Salad",
            "with Ranch or Caesar dressing",
            2.49
        )

        items+=MenuItem(
            5,
            "Cold",
            "Sushi",
            "Spicy Tuna Roll",
            5.0
        )

        items+=MenuItem(
            6,
            "Hot",
            "Chicken Sandwich",
            "",
            3.66
        )

        return items
    }
}