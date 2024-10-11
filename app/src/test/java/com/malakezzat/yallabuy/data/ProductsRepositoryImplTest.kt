package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.sharedpref.FakeSharedDataSource
import com.malakezzat.yallabuy.model.Image
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test


class ProductsRepositoryImplTest{
    private val fakeRemoteDataSource =  FakeRemoteDataSource()
    private val fakeSharedDataSource = FakeSharedDataSource()
    //private val ProductsRepositoryImpl  = ProductsRepositoryImpl()
    private lateinit var productsRepositoryImpl: ProductsRepository
    @Before
    fun setUp() {
        productsRepositoryImpl = ProductsRepositoryImpl.getInstance(fakeRemoteDataSource,
            fakeSharedDataSource)
    }

    @Test
    fun getAllProducts_returnThreeProducts() = runTest {
        val result = productsRepositoryImpl.getAllProducts()
        result.collect{ productList->
            assertThat(productList.size,`is`(3))
        }
    }

    @Test
    fun getAllProducts_returnFirstProduct() = runTest {
        val result = productsRepositoryImpl.getAllProducts()
        result.collect{ productList->
            assertThat(productList.first(),`is`(FakeRemoteDataSource().productsList.first()))
        }
    }

    @Test
    fun getAllProducts_returnLastProduct() = runTest {
        val result = productsRepositoryImpl.getAllProducts()
        result.collect{ productList->
            assertThat(productList.last(),`is`(FakeRemoteDataSource().productsList.last()))
        }
    }

   @Test
   fun getProductsByCollectionId_returnFirstProductID()= runTest {
       val result = productsRepositoryImpl.getProductsByCollectionId(1)
       result.collect {product->
           assertThat(product[0].id,`is`(FakeRemoteDataSource().productsList[0].id) )
       }
   }

    @Test
    fun getProductsByCollectionId_returnFirstProductData()= runTest {
        val result = productsRepositoryImpl.getProductsByCollectionId(1)
        result.collect {product->
            assertThat(product[0],`is`(FakeRemoteDataSource().productsList[0]) )
        }
    }

    @Test
    fun getProductsByCollectionId_returnThreeProducts()= runTest {
        val result1 = productsRepositoryImpl.getProductsByCollectionId(1)
        result1.collect {product->
            assertThat(product[0],`is`(FakeRemoteDataSource().productsList[0]) )
        }
        val result2 = productsRepositoryImpl.getProductsByCollectionId(2)
        result2.collect {product->
            assertThat(product[1],`is`(FakeRemoteDataSource().productsList[1]) )
        }
        val result3 = productsRepositoryImpl.getProductsByCollectionId(3)
        result3.collect {product->
            assertThat(product[2],`is`(FakeRemoteDataSource().productsList[2]) )
        }
    }

    @Test
    fun getConversionRate_returnSuccessResult()= runTest {
        val result = productsRepositoryImpl.getConversionRate()
        result.collect {conversionRate->
            assertThat(conversionRate.result,`is`("success") )
        }
    }

    @Test
    fun getConversionRate_returnEGP()= runTest {
        val result = productsRepositoryImpl.getConversionRate()
        result.collect {conversionRate->
            assertThat(conversionRate.conversion_rates.EGP,`is`(instanceOf(Long::class.java)))
        }
    }

    @Test
    fun getConversionRate_returnUSDRate()= runTest {
        val result = productsRepositoryImpl.getConversionRate()
        result.collect {conversionRate->
            assertThat(conversionRate.conversion_rates.USD,`is`(instanceOf(Double::class.java)))
        }
    }



}