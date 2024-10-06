// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.malakezzat.yallabuy.model

data class SmartCollections (
    val smartCollections: List<SmartCollection>
)

data class SmartCollection (
    val image: ImageSmartCollection,
    val bodyHTML: String,
    val handle: String,
    val rules: List<Rule>,
    val title: String,
    val publishedScope: PublishedScope,
    val updatedAt: String,
    val disjunctive: Boolean,
    val adminGraphqlAPIID: String,
    val id: Long,
    val publishedAt: String,
    val sortOrder: SortOrder
)

data class ImageSmartCollection (
    val src: String,
    val width: Long,
    val createdAt: String,
    val height: Long
)

enum class PublishedScope {
    Web
}

data class Rule (
    val condition: String,
    val column: Column,
    val relation: Relation
)

enum class Column {
    Title
}

enum class Relation {
    Contains
}

enum class SortOrder {
    BestSelling
}
