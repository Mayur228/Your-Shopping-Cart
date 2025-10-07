package com.demo.yourshoppingcart.home.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.home.data.source.HomeSource
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class HomeRepositoryImpl (private val source: HomeSource): HomeRepository {
    override suspend fun getCategory(): Resource<HomeEntity.CategoryResponseEntity> {
        try {
            val data = source.getCategory()
            return Resource.Data(HomeModel.CategoryResponse.toEntity(data))
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }

    override suspend fun getAllCategoryItem(): Resource<HomeEntity.CategoryItemResponseEntity> {
        try {
            val data = source.getAllCategoryItem()
            return Resource.Data(HomeModel.CategoryItemResponse.toEntity(data))
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }

    override suspend fun getSelectedCategoryItem(cat: String): Resource<HomeEntity.CategoryItemResponseEntity> {
        try {
            val data = source.getSelectedCategoryItem(cat)
            return Resource.Data(HomeModel.CategoryItemResponse.toEntity(data))
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }
}