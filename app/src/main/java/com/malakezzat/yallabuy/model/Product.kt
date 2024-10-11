package com.malakezzat.yallabuy.model


data class Product(
    var id: Long? = null,
    var title: String,
    var body_html: String,
    var vendor: String,
    var product_type: String,
    var tags: String,
    var images: List<Image> = listOf(),  // Assuming Image is a separate data class for images.
    var image: Image,
    var variants: List<Variant> = listOf() ,// Add this for variants.
    var options : List<Option>
)

data class Variant(
    var id: Long = 0,
    var title: String = "",
    var price: String = "",
    var sku: String = "",
    var inventory_quantity  : Long = 0,
    var product_id : Long =0,
    var option1 : String ="",
    var option2 : String=""
)
data class Option( val id :Long,
    var product_id : Long ,
    var name : String,
    var values : List<String>
)
data class Image(
    var id: Long,
    var src: String // URL of the product image
)
data class ProductResponse(
    val product: Product
)

data class ProductsResponse(
    val products: List<Product>
)

data class VariantResponse(
    val variant: Variant
)
