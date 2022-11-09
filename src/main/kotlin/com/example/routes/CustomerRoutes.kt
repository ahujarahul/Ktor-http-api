package com.example.routes

import com.example.models.Customer
import com.example.models.customerDataStore
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * 'customerRouting' is an extension function to define all our custom routes.
 * This function needs to be registered with Ktor. Refer @see [Routing.kt]
 */
fun Route.customerRouting() {

    /**
     * listen to all the endpoints with '/customer'
     */
    route("/customer") {
        // respond to different types of HTTP request methods

        /**
         * this will respond with a list of all the customers when the end point is only '/customer'
         */
        get {

            if (customerDataStore.isNotEmpty()) {
                /**
                 * here the ContentNegotiation plugin comes into play to serialize from Customer object to JSON.
                 * Refer @see [Serialization.kt]
                 */
                call.respond(customerDataStore)
            } else {
                call.respondText(
                    "No customer found. Please try again.",
                    status = HttpStatusCode.OK
                )
            }
        }

        /**
         * this will respond with a particular customer matching the 'id' when the end point is, example: '/customer/1'
         */
        get("{id?}") {

            // fetch the request parameter using its name and check whether it exists in the request, else it is a BadRequest.
            val id = call.parameters["id"] ?: return@get call.respondText(
                "ERROR: Missing id. Please provide customer id in the request.",
                status = HttpStatusCode.BadRequest
            )

            // no customer found by 'id'
            val customer = customerDataStore.find { customer ->
                customer.id == id
            } ?: return@get call.respondText(
                "ERROR: No customer found with id: $id. Please try again with a different id.",
                status = HttpStatusCode.NotFound
            )

            // customer found
            call.respond(customer)
        }

        /**
         * receive an object of type Customer from a POST request
         */
        post {

            /**
             * here the ContentNegotiation plugin comes into play to deserialize from JSON to Customer object.
             * Refer @see [Serialization.kt]
             */
            val newCustomer = call.receive<Customer>()
            // insert customer
            customerDataStore.add(newCustomer)
            call.respondText(
                "Customer stored successfully.",
                status = HttpStatusCode.Created
            )
        }

        delete("{id?}") {

            val id = call.parameters["id"] ?: return@delete call.respondText (
                "ERROR: Missing id. Please provide customer id in the request.",
                status = HttpStatusCode.BadRequest
            )

            when(customerDataStore.removeIf { customer -> customer.id == id }) {
                true -> {
                    call.respondText(
                        "Customer record deleted.",
                        status = HttpStatusCode.Accepted
                    )
                }
                false -> {
                    call.respondText(
                        "ERROR: No customer found with id: $id. Please try again with a different id.",
                        status = HttpStatusCode.NotFound
                    )
                }
            }
        }
    }
}

