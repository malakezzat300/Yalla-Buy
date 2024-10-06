package com.malakezzat.yallabuy.model


data class Product(
    val id: Long? = null,
    val title: String,
    val body_html: String,
    val vendor: String,
    val product_type: String,
    val tags: String,
    val images: List<Image> = listOf(),  // Assuming Image is a separate data class for images.
    val image: Image,
    val variants: List<Variant> = listOf() // Add this for variants.
)

data class Variant(
    val id: Long,
    val title: String,
    val price: String,
    val sku: String
)

data class Image(
    val id: Long,
    val src: String // URL of the product image
)
data class ProductResponse(
    val product: Product
)

data class ProductsResponse(
    val products: List<Product>
)
