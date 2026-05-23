package com.example.creatorshub.screens.services

interface ServiceDetailContract {

    interface View {
        fun showBookingLoading()
        fun showBookingSuccess()
        fun showBookingError(message: String)
        fun enableBookButton()
        fun disableBookButton()
    }

    interface Presenter {
        fun bookService(serviceId: Long, instructions: String)
    }
}
