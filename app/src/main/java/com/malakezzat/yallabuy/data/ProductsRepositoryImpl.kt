package com.malakezzat.yallabuy.data

import com.example.yallabuyadmin.coupons.model.CouponsRemoteDataSource
import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSource
import com.malakezzat.yallabuy.data.remote.coupons.DiscountCode
import com.malakezzat.yallabuy.data.remote.coupons.PriceRule
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSource
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl private constructor(
    private var productsRemoteDataSource: ProductsRemoteDataSource,
    private var globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource,
    private var couponsRemoteDataSource: CouponsRemoteDataSource
    ):ProductsRepository{

    companion object{
        private var instance: ProductsRepositoryImpl? = null
        fun getInstance( productsRemoteDataSource: ProductsRemoteDataSource,
                         globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource,
                         couponsRemoteDataSource: CouponsRemoteDataSource
                        ):ProductsRepositoryImpl{
            return instance ?: synchronized(this){
                val temp =ProductsRepositoryImpl(productsRemoteDataSource,
                    globalSharedPreferenceDataSource,
                    couponsRemoteDataSource)
                instance = temp
                temp

        }
    }
}

    override suspend fun getAllProducts(): Flow<List<Product>> {
        return productsRemoteDataSource.getAllProducts()
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> {
        return productsRemoteDataSource.getCategories()
    }

    override fun getPriceRules(): Flow<List<PriceRule>> {
        return couponsRemoteDataSource.getPriceRules()
    }

    override fun getDiscountCodes(priceRuleId: Long): Flow<List<DiscountCode>> {
        return couponsRemoteDataSource.getDiscountCodes(priceRuleId)
    }
}