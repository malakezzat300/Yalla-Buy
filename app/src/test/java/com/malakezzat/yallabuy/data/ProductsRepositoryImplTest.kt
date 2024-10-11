package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.sharedpref.FakeSharedDataSource
import kotlinx.coroutines.test.runTest
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
    fun getAllProducts_return_fake_list() = runTest {
        val result = productsRepositoryImpl.getAllProducts()
        result.collect{ prductList->
            assertThat(prductList.size,`is`(3))
        }
    }
   @Test
   fun getProductsByCollectionId_return_fake_list()= runTest {
       val result = productsRepositoryImpl.getProductsByCollectionId(1)
       result.collect {prduct->
           assertThat(prduct.get(0).id,`is`(1) )
       }
   }

}