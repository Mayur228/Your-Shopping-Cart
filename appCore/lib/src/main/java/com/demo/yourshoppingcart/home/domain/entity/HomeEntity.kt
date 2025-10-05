package com.demo.yourshoppingcart.home.domain.entity

abstract class HomeEntity {

    data class CategoryResponseEntity(
        val categoryList: List<CategoryEntity>
    )

    data class CategoryEntity(
        val id: String,
        val name: String,
        val img: String
    )

    data class SelectedCategoryItemRequestEntity(
        val catId: String
    )

    data class CategoryItemResponseEntity(
        val itemList: List<ItemEntity>
    )

    data class ItemEntity(
        val id: String,
        val name: String,
        val img: String,
        val des: String,
        val price: String,
        val category: String
    )
}