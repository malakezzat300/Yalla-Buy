// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.malakezzat.yallabuy.model

data class Category (
    val customCollections: List<CustomCollection>
)

data class CustomCollection (
    val publishedScope: String,
    val updatedAt: String,
    val adminGraphqlAPIID: String,
    val handle: String,
    val id: Long,
    val title: String,
    val publishedAt: String,
    val sortOrder: String,
    val image: ImageCategory? = null,
    val bodyHTML: String? = null
)

data class ImageCategory(
    val src: String,
    val width: Long,
    val createdAt: String,
    val height: Long
)
