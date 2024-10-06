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

//    val paymentKeyRequest = PaymentKeyRequest(
//        1000,
//        "EGP",
//        " ",
//        BillingData(
//            "7",
//            "test@gmail,com",
//            "2",
//            "firstName",
//            "street",
//            "building",
//            "0123456789",
//            "shipping",
//            "71511",
//            "assiut",
//            "egypt",
//            "lastName",
//            "assiut"
//        ),
//        "4849430",
//        "lockOrder")


    private val billingData = BillingData(
        apartment = "803",
        email = "email@example.com",
        floor = "42",
        first_name = "John",
        last_name = "Doe",
        phone_number = "01123456789",
        shipping_method = "express",
        street = "123 Street",
        building = "12",
        postal_code = "12345",
        city = "Cairo",
        country = "EG"
    )

    val paymentKeyRequest = PaymentKeyRequest(
        amount_cents = 1000,
        currency = "EGP",
        order_id = "YOUR_ORDER_ID",
        billing_data = billingData
    )
}