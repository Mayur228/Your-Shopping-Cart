package com.demo.yourshoppingcart.home.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository

class GetSelectedCategoryItemUseCase(private val homeRepository: HomeRepository){
    suspend operator fun invoke(catId: String): Resource<HomeEntity.CategoryItemResponseEntity> {
        return homeRepository.getSelectedCategoryItem(catId)
    }
}
