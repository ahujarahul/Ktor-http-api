package com.example.models

import kotlinx.serialization.Serializable

// Serialization will allow us to generate the JSON representation of Customer object in API response.
@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

// in-memory data store
val customerDataStore = mutableListOf<Customer>()