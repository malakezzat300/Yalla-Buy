package com.malakezzat.yallabuy.ui.categories.viewmodel

import com.malakezzat.yallabuy.data.FakeRepository
import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test


class CategoriesViewModelTest{
    lateinit var categoriesViewModel :CategoriesViewModel
    private lateinit var fakeRemoteDataSource : FakeRemoteDataSource
    private lateinit var repository : FakeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = FakeRepository()
        categoriesViewModel = CategoriesViewModel(repository)
}
    @Test
    fun getAllCategories_returnAllCategories() = runTest {
        categoriesViewModel.getAllCategories()
        advanceUntilIdle()

        val apiState = categoriesViewModel.categoriesList.first()
        assertThat(apiState, instanceOf(ApiState.Success::class.java))

        val successState = apiState as ApiState.Success<List<CustomCollection>>
        assertTrue(successState.data.isNotEmpty())

        val firstCategory = successState.data.first()
        assertThat(firstCategory.title, `is`("Winter Collection"))
    }


    }