package com.dolanj7.lunchilicious.data.client

import com.dolanj7.lunchilicious.data.entity.FoodOrderRetrofit
import com.dolanj7.lunchilicious.data.entity.LineItemRetrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderClient {
    @POST("lunchilicious/addorder")
    suspend fun addOrder(@Body order: FoodOrderRetrofit): FoodOrderRetrofit
    @POST("lunchilicious/addlineitems")
    suspend fun addLineItems(@Body items: List<LineItemRetrofit>)
    @GET("lunchilicious/order?")
    suspend fun getOrderById(@Query("orderId") id: String): FoodOrderRetrofit?
    @GET("lunchilicious/lineitems?")
    suspend fun getLineItemsById(@Query("orderId") id: String): List<LineItemRetrofit>?
}
