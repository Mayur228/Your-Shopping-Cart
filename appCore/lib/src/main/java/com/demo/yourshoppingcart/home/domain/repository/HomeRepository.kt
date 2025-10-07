package com.demo.yourshoppingcart.home.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity

interface HomeRepository {
    suspend fun getCategory(): Resource<HomeEntity.CategoryResponseEntity>
    suspend fun getAllCategoryItem(): Resource<HomeEntity.CategoryItemResponseEntity>
    suspend fun getSelectedCategoryItem(cat: String): Resource<HomeEntity.CategoryItemResponseEntity>
}