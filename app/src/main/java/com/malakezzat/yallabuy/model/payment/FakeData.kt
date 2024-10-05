package com.malakezzat.paymenttest2.model

object FakeData{
    val orderRequest = OrderRequest(
        auth_token = "token",
        delivery_details = DeliveryDetails(
            apartment = "123",
            email = "test@gmail.com",
            floor = "1",
            phone_number = "0123456789",
            shipping_method = "delivery",
            city = "Cairo",
            country = "Egypt",
            first_name = "John",
            last_name = "Doe",
            state = "Cairo",
            street = "Main St",
            postal_code = "12345"
        ),
        amount_cents = 100,
        currency = "EGP",
        items = listOf(Item(name = "Product", amount_cents = 100, quantity = 1))
    )

    val paymentKeyRequest = PaymentKeyRequest(
        1000,
        "EGP",
        " ",
        BillingData(
            "7",
            "test@gmail,com",
            "2",
            "firstName",
            "street",
            "building",
            "0123456789",
            "shipping",
            "71511",
            "assiut",
            "egypt",
            "lastName",
            "assiut"
        ),
        123456,
        "lockOrder")

}