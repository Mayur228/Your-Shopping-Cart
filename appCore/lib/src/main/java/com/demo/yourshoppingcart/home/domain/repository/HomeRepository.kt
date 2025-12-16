package com.demo.yourshoppingcart.home.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.categoryEntity
import com.demo.yourshoppingcart.home.domain.entity.productEntity

interface HomeRepository {
    suspend fun getCategory(): Resource<List<categoryEntity>>
    suspend fun getAllCategoryItem(): Resource<List<productEntity>>
    suspend fun getSelectedCategoryItem(cat: String): Resource<List<productEntity>>
}