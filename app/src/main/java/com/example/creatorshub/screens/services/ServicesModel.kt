package com.example.creatorshub.screens.services

import com.example.creatorshub.models.ServiceModel

/**
 * Model implementation for Services Screen using online demo images.
 */
class ServicesModel : ServicesContract.Model {
    override fun getServices(): List<ServiceModel> {
        return listOf(
            ServiceModel("Website UI", "https://images.unsplash.com/photo-1460925895917-afdab827c52f?q=80&w=600"),
            ServiceModel("App UI", "https://images.unsplash.com/photo-1551650975-87deedd944c3?q=80&w=600"),
            ServiceModel("Roblox Scripting", "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=600"),
            ServiceModel("Poster", "https://images.unsplash.com/photo-1561070791-2526d30994b5?q=80&w=600"),
            ServiceModel("3D Animation", "https://images.unsplash.com/photo-1626814026160-2237a3c3e7f6?q=80&w=600"),
            ServiceModel("Logo Design", "https://images.unsplash.com/photo-1626785774573-4b799315345d?q=80&w=600"),
            ServiceModel("3D Modelling", "https://images.unsplash.com/photo-1614850523296-d8c1af93d400?q=80&w=600"),
            ServiceModel("Web Development", "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=600")
        )
    }
}
