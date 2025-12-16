package com.demo.yourshoppingcart.home.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.categoryEntity
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetCategoryUseCase(private val homeRepository: HomeRepository){
    suspend operator fun invoke(): Resource<List<categoryEntity>> {
        return homeRepository.getCategory()
    }
}
