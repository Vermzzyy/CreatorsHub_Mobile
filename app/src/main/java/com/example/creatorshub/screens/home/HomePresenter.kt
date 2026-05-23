package com.example.creatorshub.screens.home

import android.content.Context
import com.example.creatorshub.data.BackendClient
import com.example.creatorshub.data.RetrofitClient
import com.example.creatorshub.data.SessionManager
import com.example.creatorshub.models.OrderModel
import com.example.creatorshub.models.ProfileResponse
import com.example.creatorshub.models.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    private val view: HomeContract.View,
    private val model: HomeModel,
    private val context: Context
) : HomeContract.Presenter {

    override fun loadDashboard() {
        val session = SessionManager(context)

        // Auth guard
        if (session.getToken().isNullOrEmpty()) {
            view.navigateToLogin()
            return
        }

        loadProfile()
        loadFeaturedServices()
        loadRecentOrders()
    }

    // ── Profile → "Welcome, [Name]!" ─────────────────────────────────────────
    private fun loadProfile() {
        // Supabase RLS filters automatically to the current user via Bearer token
        // No userId filter needed — just call profile("*")
        RetrofitClient.create(context).profile("*")
            .enqueue(object : Callback<List<ProfileResponse>> {
                override fun onResponse(
                    call: Call<List<ProfileResponse>>,
                    response: Response<List<ProfileResponse>>
                ) {
                    val profile = response.body()?.firstOrNull()
                    val name = profile?.firstName?.takeIf { it.isNotEmpty() }
                        ?: profile?.email?.substringBefore("@")
                        ?: "User"
                    view.showWelcome("Welcome, $name! 👋")
                }

                override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                    view.showWelcome("Welcome! 👋")
                }
            })
    }

    // ── Featured: first 2 services from backend ──────────────────────────────
    private fun loadFeaturedServices() {
        view.showFeaturedLoading()

        BackendClient.create(context).getServices()
            .enqueue(object : Callback<List<ServiceResponse>> {
                override fun onResponse(
                    call: Call<List<ServiceResponse>>,
                    response: Response<List<ServiceResponse>>
                ) {
                    view.hideFeaturedLoading()
                    if (response.isSuccessful) {
                        val services = response.body() ?: emptyList()
                        view.showFeaturedServices(services.takeLast(2).reversed())
                    }
                    // Silently ignore errors — featured is non-critical on home
                }

                override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                    view.hideFeaturedLoading()
                }
            })
    }

    // ── Recent orders (max 2) ────────────────────────────────────────────────
    private fun loadRecentOrders() {
        view.showOrdersLoading()

        BackendClient.create(context).getMyOrders()
            .enqueue(object : Callback<List<OrderModel>> {
                override fun onResponse(
                    call: Call<List<OrderModel>>,
                    response: Response<List<OrderModel>>
                ) {
                    view.hideOrdersLoading()

                    when {
                        response.isSuccessful -> {
                            val orders = response.body() ?: emptyList()
                            if (orders.isEmpty()) {
                                view.showOrdersEmpty("No orders yet. Book a service to get started!")
                            } else {
                                view.showRecentOrders(orders.take(2))
                            }
                        }
                        response.code() == 401 || response.code() == 403 ->
                            view.showOrdersEmpty("Session expired or unauthorized. Please re-login.")
                        else ->
                            view.showOrdersEmpty("Could not load orders (${response.code()}).")
                    }
                }

                override fun onFailure(call: Call<List<OrderModel>>, t: Throwable) {
                    view.hideOrdersLoading()
                    view.showOrdersEmpty("Could not reach server.")
                }
            })
    }
}