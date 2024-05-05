package com.dolanj7.lunchilicious.data.client
import com.dolanj7.lunchilicious.data.entity.MenuItemRetrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MenuItemClient {
    @GET("lunchilicious/")
    suspend fun getMenuItems(): List<MenuItemRetrofit>

    @POST("lunchilicious/addmenuitem")
    suspend fun addMenuItem(@Body item: MenuItemRetrofit): MenuItemRetrofit
}