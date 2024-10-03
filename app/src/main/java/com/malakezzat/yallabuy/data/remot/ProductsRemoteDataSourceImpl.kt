package com.malakezzat.yallabuy.data.remot

class ProductsRemoteDataSourceImpl (var  productService: ProductService):
    ProductsRemoteDataSource {


    companion object {
        private var instance: ProductsRemoteDataSourceImpl? = null
        fun getInstance(productService: ProductService): ProductsRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                val temp = ProductsRemoteDataSourceImpl(productService)
                instance = temp
                temp
            }
        }
}
    }