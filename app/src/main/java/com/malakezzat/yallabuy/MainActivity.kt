package com.malakezzat.yallabuy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.yallabuyadmin.coupons.model.CouponsRemoteDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.malakezzat.paymenttest2.PaymentRemoteDataSourceImpl
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.ProductsRepositoryImpl
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSourceImpl
import com.malakezzat.yallabuy.data.remote.RetrofitHelper
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSourceImp
import com.malakezzat.yallabuy.ui.NavigationApp
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModelFactory
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModelFactory
import com.malakezzat.yallabuy.ui.categories.viewmodel.CategoriesViewModelFactory
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModelFactory
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModelFactory
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCategoryViewModelFactory
import com.malakezzat.yallabuy.ui.search.SearchViewModelFactory
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val repo by lazy {
        ProductsRepositoryImpl.getInstance(
            ProductsRemoteDataSourceImpl.
            getInstance( RetrofitHelper.getInstance().create(ProductService::class.java)),
            GlobalSharedPreferenceDataSourceImp(this.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)),
            CouponsRemoteDataSource(RetrofitHelper.getInstance().create(ProductService::class.java))
        )
    }
    private val paymentRemoteDataSource by lazy {
        PaymentRemoteDataSourceImpl()
    }
    private val homeScreenViewModelFactory by lazy {
        HomeScreenViewModelFactory(repo)
    }
    private val productsByCategoryViewModelFactory by lazy {
        ProductsByCategoryViewModelFactory(repo)
    }
    private val categoriesScreenViewModelFactory by lazy {
        CategoriesViewModelFactory(repo)
    }
    private val signUpViewModelFactory by lazy {
        SignUpViewModelFactory(repo)
    }
    private val logInViewModelFactory by lazy {
        LogInViewModelFactory(repo)
    }
    private val paymentViewModelFactory by lazy {
        PaymentViewModelFactory(paymentRemoteDataSource)
    }
    private val searchViewModelFactory by lazy {
        SearchViewModelFactory(repo)
    }
    private val shoppingCartViewModelFactory by lazy {
        ShoppingCartViewModelFactory(repo)
    }
    private val productInfoViewModelFactory by lazy {
        ProductInfoViewModelFactory(repo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
          YallaBuyTheme {
            NavigationApp(
                homeScreenViewModelFactory,
                signUpViewModelFactory,
                logInViewModelFactory,
                paymentViewModelFactory,
                searchViewModelFactory,
                shoppingCartViewModelFactory,
                productInfoViewModelFactory,
                categoriesScreenViewModelFactory,
                productsByCategoryViewModelFactory
            )
        }
        }
    }
}

