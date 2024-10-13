package com.malakezzat.yallabuy

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.FirebaseApp
import com.malakezzat.yallabuy.data.ProductsRepositoryImpl
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSourceImpl
import com.malakezzat.yallabuy.data.remote.RetrofitHelper
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSourceImp
import com.malakezzat.yallabuy.ui.NavigationApp
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModelFactory
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModelFactory
import com.malakezzat.yallabuy.ui.categories.viewmodel.CategoriesViewModelFactory
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModelFactory
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModelFactory
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModelFactory
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModelFactory
import com.malakezzat.yallabuy.ui.profile.viewmodel.ProfileScreenViewModelFactory
import com.malakezzat.yallabuy.ui.search.SearchViewModelFactory
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModelFactory
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme
import com.malakezzat.yallabuy.ui.wishlist.WishlistViewModelFactory

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
    private val profileScreenViewModelFactory by lazy {
        ProfileScreenViewModelFactory(repo)
    }
    private val productsByCollectionIdViewModelFactory by lazy {
        ProductsByCollectionIdViewModelFactory(repo)
    }
    private val ordersViewModelFactory by lazy {
        OrdersViewModelFactory(repo)
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
        PaymentViewModelFactory(repo)
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
    private val wishlistViewModelFactory by lazy {
        WishlistViewModelFactory(repo)
    }
    private val settingsViewModelFactory by lazy {
        SettingsViewModelFactory(repo)
    }
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase and other components
        FirebaseApp.initializeApp(this)

        // Initialize ConnectivityManager
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Register Network Callback
        registerNetworkCallback()

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
                    productsByCollectionIdViewModelFactory,
                    wishlistViewModelFactory,
                    ordersViewModelFactory,
                    settingsViewModelFactory,
                    profileScreenViewModelFactory
                )
            }
        }
    }

    // Register the network callback to listen for connectivity changes
    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Network is available, notify UI or handle logic here
                println("Network is available")
            }

            override fun onLost(network: Network) {
                // Network is lost, notify UI or handle logic here
                println("Network is lost")
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val isConnected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if (isConnected) {
                    println("Network capabilities changed: Internet available")
                } else {
                    println("Network capabilities changed: No Internet")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister network callback if needed to avoid memory leaks
        connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }
}

