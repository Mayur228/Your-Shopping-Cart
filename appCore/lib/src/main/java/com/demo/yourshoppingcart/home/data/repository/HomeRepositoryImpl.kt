package com.demo.yourshoppingcart.home.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.source.HomeSource
import com.demo.yourshoppingcart.home.domain.entity.categoryEntity
import com.demo.yourshoppingcart.home.domain.entity.productEntity
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class HomeRepositoryImpl (private val source: HomeSource): HomeRepository {
    override suspend fun getCategory(): Resource<List<categoryEntity>> {
        try {
            val data = source.getCategory()
            return Resource.Data(data)
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }

    override suspend fun getAllCategoryItem(): Resource<List<productEntity>> {
        try {
            val data = source.getAllCategoryItem()
            return Resource.Data(data)
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }

    override suspend fun getSelectedCategoryItem(cat: String): Resource<List<productEntity>> {
        try {
            val data = source.getSelectedCategoryItem(cat)
            return Resource.Data(data)
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }
}