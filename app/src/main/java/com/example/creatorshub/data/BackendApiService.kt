package com.example.creatorshub.data

import com.example.creatorshub.models.CreateOrderRequest
import com.example.creatorshub.models.GenericResponse
import com.example.creatorshub.models.OrderModel
import com.example.creatorshub.models.ServiceResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Spring Boot backend API — for Services and Orders.
 * Base URL: http://10.0.2.2:8080/  (Android emulator host alias)
 * Change to your machine's local IP if running on a physical device.
 * Requires Authorization: Bearer <spring_boot_token>
 */
interface BackendApiService {

    // 📋 GET all services (public endpoint)
    @GET("api/v1/services")
    fun getServices(): Call<List<ServiceResponse>>

    // 🔍 GET single service by ID
    @GET("api/v1/services/{id}")
    fun getServiceById(@Path("id") id: Long): Call<ServiceResponse>

    // 🛒 POST create a new order (requires auth)
    @POST("api/v1/orders")
    fun createOrder(@Body request: CreateOrderRequest): Call<GenericResponse>

    // 📜 GET order history for current user
    @GET("api/v1/orders/my")
    fun getMyOrders(): Call<List<OrderModel>>
}
