package com.demo.yourshoppingcart.home.data.model

import com.demo.yourshoppingcart.home.domain.entity.HomeEntity

abstract class HomeModel {

    data class CategoryResponse(
        val category: List<Category>
    ){
        companion object {
            @Throws(IllegalArgumentException::class)
            fun toEntity(response: CategoryResponse): HomeEntity.CategoryResponseEntity {
                val categoryData = response.category.map {
                    Category.toEntity(it)
                }
                return HomeEntity.CategoryResponseEntity(
                    categoryList = categoryData
                )
            }
        }
    }

    data class Category(
        val catId: String,
        val catName: String,
        val catImg: String
    ){
        companion object {
            @Throws(IllegalArgumentException::class)
            fun toEntity(data: Category): HomeEntity.CategoryEntity {
                return HomeEntity.CategoryEntity(
                   id = data.catId,
                    name = data.catName,
                    img = data.catImg
                )
            }
        }
    }

   /* data class SelectedCategoryItemRequest(val catId: String){
        companion object {
            @Throws(IllegalArgumentException::class)
            fun fromEntity(entity: HomeEntity.SelectedCategoryItemRequestEntity): SelectedCategoryItemRequest {
                return SelectedCategoryItemRequest(
                    entity.catId
                )
            }
        }
    }*/

    data class CategoryItemResponse(
        val itemList: List<Item>
    ) {
        companion object {
            @Throws(IllegalArgumentException::class)
            fun toEntity(response: CategoryItemResponse): HomeEntity.CategoryItemResponseEntity {
                val itemData = response.itemList.map {
                    Item.toEntity(it)
                }
                return HomeEntity.CategoryItemResponseEntity(
                    itemList = itemData
                )
            }
        }
    }

    data class Item(
        val itemId: String,
        val itemName: String,
        val itemImg: String,
        val itemDes: String,
        val itemPrice: String,
        val cat: String
    ) {
        companion object {
            @Throws(IllegalArgumentException::class)
            fun toEntity(data: Item): HomeEntity.ItemEntity {
                return HomeEntity.ItemEntity(
                    id = data.itemId,
                    name = data.itemName,
                    img = data.itemImg,
                    des = data.itemDes,
                    price = data.itemPrice,
                    category = data.cat
                )
            }
        }
    }

}