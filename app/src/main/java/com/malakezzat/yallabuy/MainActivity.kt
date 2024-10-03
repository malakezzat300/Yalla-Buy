package com.malakezzat.yallabuy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.malakezzat.yallabuy.ui.CreateAccountScreen
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.ProductsRepositoryImpl
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.data.remot.ProductsRemoteDataSourceImpl
import com.malakezzat.yallabuy.data.remot.RetrofitHelper
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSource
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSourceImp
import com.malakezzat.yallabuy.ui.NavigationApp
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme

class MainActivity : ComponentActivity() {

    private val repo by lazy {
        ProductsRepositoryImpl.getInstance(
            ProductsRemoteDataSourceImpl.
            getInstance( RetrofitHelper.getInstance().create(ProductService::class.java)),
            GlobalSharedPreferenceDataSourceImp(this.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        )
    }
    private val homeScreenViewModelFactory by lazy {
        HomeScreenViewModelFactory(repo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
    
        enableEdgeToEdge()
        setContent {
          YallaBuyTheme {
            NavigationApp(
                homeScreenViewModelFactory
            )
        }
        }
    }
}

