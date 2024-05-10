package com.dolanj7.lunchilicious.data.client

import com.dolanj7.lunchilicious.data.entity.FoodOrderRetrofit
import com.dolanj7.lunchilicious.data.entity.LineItemRetrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderClient {
    @POST("lunchilicious/addorder")
    suspend fun addOrder(@Body order: FoodOrderRetrofit): FoodOrderRetrofit
    @POST("lunchilicious/addlineitems")
    suspend fun addLineItems(@Body items: List<LineItemRetrofit>)
}
