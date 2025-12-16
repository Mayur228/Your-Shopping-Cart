package com.demo.yourshoppingcart.home.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.productEntity
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetSelectedCategoryItemUseCase(private val homeRepository: HomeRepository){
    suspend operator fun invoke(catId: String): Resource<List<productEntity>> {
        return homeRepository.getSelectedCategoryItem(catId)
    }
}
