package com.malakezzat.yallabuy.ui.shoppingcart.view

import android.provider.Telephony.Mms.Draft
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme
import com.malakezzat.yallabuy.R
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.sharedpref.CurrencyPreferences
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.AppliedDiscount
import com.malakezzat.yallabuy.model.DiscountCode
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.PriceRule
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.Variant
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.view.CustomTopBarHome
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.round

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel,
    navController: NavController,
) {

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var orderItems by remember { mutableStateOf( emptyList<LineItem>() ) }
    var draftOrder by remember { mutableStateOf( DraftOrder() ) }
    var isLoading by remember { mutableStateOf( false ) }
    val shoppingCartOrder by viewModel.shoppingCartDraftOrder.collectAsState()
    val visibleItems = remember { mutableStateListOf<LineItem>(*draftOrder.line_items.toTypedArray()) }
    var subtotal by remember { mutableDoubleStateOf(0.0) }
    var discountSaved by remember { mutableDoubleStateOf(0.0) }
    var total by remember { mutableDoubleStateOf(0.0) }
    val variantState by viewModel.variantId.collectAsState()
    var variant by remember { mutableStateOf(Variant()) }
    var discountAmount by remember { mutableDoubleStateOf(total - subtotal) }
    var variantSet by remember { mutableStateOf(mutableSetOf<Variant>() ) }
    var itemsCount by remember { mutableIntStateOf(0) }
    var emptyScreen by remember { mutableStateOf( true ) }
    var draftOrderForTotal by remember { mutableStateOf( DraftOrder() ) }
    var discountFirstTime by remember { mutableDoubleStateOf(0.0) }
    var orderItemsForCount by remember { mutableStateOf( emptyList<LineItem>() ) }

    LaunchedEffect(Unit) {
        viewModel.getDraftOrders()
    }

    when(variantState){
        is ApiState.Error -> Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(variantState as ApiState.Error).message}")
        ApiState.Loading -> {
        }
        is ApiState.Success -> {
            variant = (variantState as ApiState.Success).data.variant
            Log.i("quantityTest", "ShoppingItem: variantState ${variantSet}")
            variantSet.add(variant)

            emptyScreen = true
        }
    }

    when(shoppingCartOrder){
        is ApiState.Error ->{
            isLoading = false
            emptyScreen = false
            Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(shoppingCartOrder as ApiState.Error).message}")
        }
        ApiState.Loading -> {
            isLoading = true

        }
        is ApiState.Success -> {
            isLoading = false
            orderItems = (shoppingCartOrder as ApiState.Success).data.line_items
            draftOrder = (shoppingCartOrder as ApiState.Success).data
            LaunchedEffect (Unit){
                if(orderItems.isNotEmpty()) {
                    repeat(orderItems.size)  {
                        viewModel.getVariantById(orderItems[it].variant_id)
                    }
                }
            }

            emptyScreen = true
        }
    }
    if(draftOrderForTotal == DraftOrder()) {
        subtotal = calculateSubtotal(orderItems)
        orderItemsForCount = orderItems
    } else {
        subtotal = calculateSubtotal(draftOrderForTotal.line_items)
        orderItemsForCount = draftOrderForTotal.line_items
    }
//    LaunchedEffect (subtotal,draftOrder) {
//        total = subtotal + (subtotal * (draftOrder.applied_discount?.value?.toDouble() ?: 0.0)) * -1 / 100
//        discountAmount = total - subtotal
//    }

    LaunchedEffect(draftOrder) {
        discountFirstTime = (draftOrder.applied_discount?.value?.toDouble() ?: 0.0)
    }

    LaunchedEffect (subtotal,draftOrderForTotal,discountFirstTime) {
        if(draftOrderForTotal == DraftOrder()) {
            total = subtotal + (subtotal * discountFirstTime) * -1 / 100
        } else {
            total = subtotal + (subtotal * (draftOrderForTotal.applied_discount?.value?.toDouble() ?: 0.0)) * -1 / 100
        }
        discountAmount = total - subtotal
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            VoucherBottomSheet(viewModel,draftOrder){ updatedDraftOrder ->
                draftOrder = updatedDraftOrder
                draftOrderForTotal = updatedDraftOrder
            }
        },
        content = {


            if (orderItems.isNotEmpty() && emptyScreen) {
                Scaffold(
                    topBar = { CustomTopBar(bottomSheetState,navController) },
                    containerColor = Color.White,
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)

                        ) {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(bottom = 230.dp)
                            ) {


                                if(orderItems.isNotEmpty()){
                                    items(
                                        count = orderItems.size,
                                        key = { item -> orderItems[item].variant_id }
                                    ) { index ->
                                        itemsCount = 0
                                        orderItemsForCount.forEach { itemsCount += it.quantity }
                                        val orderItem = orderItems[index]
                                        ShoppingItem(viewModel,orderItem,draftOrder,variantSet,subtotal,{ updatedDraftOrder ->
                                            subtotal = calculateSubtotal(orderItems)
                                            draftOrder = updatedDraftOrder
                                            draftOrderForTotal = updatedDraftOrder
                                        },
                                        navController,
                                            itemsCount)

                                    }
                                }
                            }

                            ShoppingView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp),
                                subtotal.toString(),
                                discountAmount.toString(),
                                total.toString(),
                                itemsCount,
                                navController
                            )
                        }
                    }
                )
            } else {
                if(!isLoading) {
                    ShoppingEmpty(navController)
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = AppColors.Teal

                        )
                    }

                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTopBar(sheetState: ModalBottomSheetState,navController : NavController) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.padding(2.dp))
            IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = "My Cart",
                style = TextStyle(fontSize = 20.sp),
                color = AppColors.Teal,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
        ) {
            Text(
                text = "Voucher Code",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = AppColors.RoseLight
            )
        }
    }
}

@Composable
fun ShoppingItem(
    viewModel: ShoppingCartViewModel,
    item: LineItem,
    draftOrder: DraftOrder,
    variantSet: Set<Variant>,
    subtotal : Double,
    onItemUpdated: (DraftOrder) -> Unit,
    navController: NavController,
    itemsCount: Int
) {
    var isMinusEnabled by remember { mutableStateOf(true) }
    var isPlusEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    CurrencyConverter.initialize(context)
    var itemQuantity by remember { mutableStateOf(item.quantity) }
    var updatedDraftOrder by remember { mutableStateOf(DraftOrder()) }
    var isDeleted by remember { mutableStateOf(false) }
    var updateJob by remember { mutableStateOf<Job?>(null) }


    AnimatedVisibility(
        visible = !isDeleted,
        exit = fadeOut(animationSpec = tween(durationMillis = 500)) + shrinkVertically(animationSpec = tween(durationMillis = 500))
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1.2f)
                        .padding(8.dp)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(shape = RoundedCornerShape(10)),
                        painter = rememberAsyncImagePainter(item.properties[0].value),
                        contentDescription = "ad",
                        contentScale = ContentScale.FillBounds,
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                        CurrencyConverter.changeCurrency(item.price.toDouble())?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Text(
                            text = "size: ${item.properties[1].value} \\ color: ${item.properties[2].value}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )

                    }
                    if (item.quantity == 1) {
                        isMinusEnabled = false
                    } else {
                        isMinusEnabled = true
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Minus Button
                        IconButton(
                            onClick = {

                                val newQuantity = (itemQuantity - 1).coerceAtLeast(1)
                                itemQuantity = newQuantity
                                val updatedDraftItems = draftOrder.line_items.map { currentItem ->
                                    if (currentItem == item) {
                                        currentItem.copy(quantity = newQuantity)
                                    } else {
                                        currentItem
                                    }
                                }
                                updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                                val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)
                                Log.i("updateTest", "ShoppingItem: - clicked")
                                updateJob?.cancel()

                                updateJob = coroutineScope.launch {
                                    delay(1000)
                                    draftOrder.id?.let {
                                        Log.i("updateTest", "ShoppingItem: - updated")
                                        viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                                    }
                                }
                                onItemUpdated(updatedDraftOrder)

                            },
                            enabled = isMinusEnabled
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_minus),
                                contentDescription = "minus",
                            )
                        }

                        Text(
                            text = itemQuantity.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        // Plus Button
                        IconButton(
                            onClick = {
                                 if (itemsCount >= 10) {
                                    Toast.makeText(
                                        context,
                                        "Maximum items count reached",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    val newQuantity = (itemQuantity + 1).coerceAtMost(
                                        variantSet.find { it.id == item.variant_id }?.inventory_quantity?.toInt()
                                            ?: 1
                                    )
                                     Log.i("quantityTest", "ShoppingItem: variantSet ${variantSet}")
                                     Log.i("quantityTest", "ShoppingItem: variant_id ${item.variant_id}")
                                     Log.i("quantityTest", "ShoppingItem: max ${variantSet.find { it.id == item.variant_id }?.inventory_quantity?.toInt()}")


                                     if (itemQuantity == variantSet.find { it.id == item.variant_id }?.inventory_quantity?.toInt()) {
                                        Toast.makeText(context, "Out of Stock", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    itemQuantity = newQuantity
//                                val max = variantSet.find { it.id == item.variant_id }?.inventory_quantity
                                    val updatedDraftItems =
                                        draftOrder.line_items.map { currentItem ->
                                            if (currentItem == item) {
                                                currentItem.copy(quantity = newQuantity)
                                            } else {
                                                currentItem
                                            }
                                        }
                                    updatedDraftOrder =
                                        draftOrder.copy(line_items = updatedDraftItems)
                                    val updatedDraftItemsRequest =
                                        DraftOrderRequest(updatedDraftOrder)
                                    Log.i("updateTest", "ShoppingItem: + clicked")

                                    updateJob?.cancel()

                                    updateJob = coroutineScope.launch {
                                        delay(1000)
                                        draftOrder.id?.let {
                                            Log.i("updateTest", "ShoppingItem: + updated")
                                            viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                                        }
                                    }
                                    onItemUpdated(updatedDraftOrder)
                                }
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add),
                                contentDescription = "add",
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(
                            onClick = {
                                showDialog = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            DeleteConfirmationDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onConfirmDelete = {
                    showDialog = false
                    isDeleted = true
                    if (draftOrder.line_items.size > 1) {
                        val updatedDraftItems = draftOrder.line_items.filter { it != item }
                        updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                        val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)
                        draftOrder.id?.let {
                            viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                        }

                    } else {
                        draftOrder.id?.let { viewModel.deleteDraftOrder(it) }
                        navController.popBackStack(Screen.ShoppingScreen.route, inclusive = true)
                        navController.navigate(Screen.ShoppingScreen.route)
                    }
                    onItemUpdated(updatedDraftOrder)

                }
            )
        }
    }
}

//order info section
@Composable
fun ShoppingView(
    modifier: Modifier,
    subtotal : String,
    discount: String,
    total : String,
    itemsCount : Int,
    navController: NavController
) {
    val context = LocalContext.current
    CurrencyConverter.initialize(context)

    Box(
        modifier = modifier

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Order Info",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtotal", style = MaterialTheme.typography.bodyLarge)
                CurrencyConverter.changeCurrency(subtotal.toDouble())
                    ?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Discount", style = MaterialTheme.typography.bodyLarge)
                CurrencyConverter.changeCurrency(discount.toDouble())
                    ?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                CurrencyConverter.changeCurrency(total.toDouble())?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { navController.navigate(Screen.PaymentMethodScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "Checkout ($itemsCount)",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}



@Composable
fun ShoppingEmpty(navController: NavController){
    Scaffold(
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty_cart_image),
                    contentDescription = "Empty Cart",
                    modifier = Modifier.size(280.dp)
                )

                Text(
                    text = "Your cart is empty",
                    color = AppColors.Teal,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Looks like you have not added anything in your cart.Go ahead and explore top categories.",
                    color = AppColors.GrayDark,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Screen.CategoriesScreen.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Explore Categories", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))


//        Button(
//            onClick = { /* Action for button click */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//                .height(56.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
//            shape = RoundedCornerShape(10.dp)
//        ) {
//            Text(
//                text = "Explore Categories",
//                color = Color.White,
//                fontSize = 16.sp
//            )
//        }
            }
        })
        //  bottomBar = { BottomNavigationBar(navController) }

}

@Composable
fun VoucherBottomSheet(
    viewModel: ShoppingCartViewModel,
    draftOrder: DraftOrder,
    onItemUpdated: (DraftOrder) -> Unit
    ) {
    var voucherCode by remember { mutableStateOf("") }
    var discountApplied by remember { mutableStateOf(false) }
    var discountMessage by remember { mutableStateOf("") }
    val priceRulesState by viewModel.priceRules.collectAsState()
    val discountCodesState by viewModel.discountCodes.collectAsState()
    var priceRules by remember { mutableStateOf(listOf<PriceRule>()) }
    val discountCodes by remember { mutableStateOf(mutableSetOf<DiscountCode>()) }
    var discountCode by remember { mutableStateOf(DiscountCode()) }
    val priceRuleState by viewModel.priceRule.collectAsState()
    var priceRule by remember { mutableStateOf(PriceRule()) }

    when(priceRulesState){
        is ApiState.Error -> Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(priceRulesState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            priceRules = (priceRulesState as ApiState.Success).data.price_rules
            LaunchedEffect(Unit) {
                priceRules.forEach {
                    it.id?.let { it1 -> viewModel.getDiscountCodes(it1) }
                }
            }
        }
    }

    when(discountCodesState){
        is ApiState.Error -> Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(discountCodesState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            val tempDiscountList = (discountCodesState as ApiState.Success).data.discount_codes
            tempDiscountList.forEach {
                discountCodes.add(it)
            }
        }
    }

    when(priceRuleState){
        is ApiState.Error -> Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(priceRuleState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            priceRule = (priceRuleState as ApiState.Success).data.price_rule
            val newDraftOrder = draftOrder.apply { this.applied_discount = AppliedDiscount(value = (priceRule.value * -1).toString()) }
            LaunchedEffect(Unit) {
                draftOrder.id?.let { viewModel.updateDraftOrder(it,DraftOrderRequest(newDraftOrder))
                    viewModel.getDraftOrders()}
            }
            discountMessage = "${(priceRule.value * -1)}% discount applied!"
            discountApplied = true
            onItemUpdated(newDraftOrder)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Voucher Code",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = voucherCode,
                onValueChange = { voucherCode = it },
                label = { Text("Enter voucher code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {

                        if (discountCodes.any { it.code == voucherCode }) {
                            discountCode = discountCodes.find { it.code == voucherCode }!!
                            viewModel.getPriceRule(discountCode.price_rule_id)

                        } else {
                                discountMessage = "Invalid voucher code"
                                discountApplied = false
                        }

                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal),
                shape = RoundedCornerShape(30.dp),
                enabled = !discountApplied
            ) {
                Text(
                    text = "Apply Voucher",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            if (discountApplied) {
                Text(
                    text = discountMessage,
                    color = AppColors.MintGreen,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else if (discountMessage.isNotEmpty()) {
                Text(
                    text = discountMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

fun calculateSubtotal(orderItems: List<LineItem>): Double {
    var newSubtotal = 0.0
    for (item in orderItems) {
        newSubtotal += item.price.toDouble() * item.quantity
    }
    return newSubtotal
}

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Confirmation", color = AppColors.Teal)
            },
            text = {
                Text("Are you sure you want to delete this item? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmDelete()
                        onDismiss()
                    }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss()
                    }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}
