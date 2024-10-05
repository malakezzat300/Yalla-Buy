// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.malakezzat.yallabuy.model

data class Category (
    val custom_collections: List<CustomCollection>
)

data class CustomCollection (
    val published_scope: String,
    val updated_at: String,
    val admin_graphql_api_id: String,
    val handle: String,
    val id: Long,
    val title: String,
    val published_at: String,
    val sort_order: String,
    val image: ImageCategory? = null,
    val body_html: String? = null
)

data class ImageCategory(
    val src: String,
    val width: Long,
    val created_at: String,
    val height: Long
)
