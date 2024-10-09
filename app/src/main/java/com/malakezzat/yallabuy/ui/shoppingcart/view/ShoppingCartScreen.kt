package com.malakezzat.yallabuy.ui.shoppingcart.view

import android.provider.Telephony.Mms.Draft
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Variant
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
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
    var discountAmount by remember { mutableDoubleStateOf(0.0) }
    var discountSaved by remember { mutableDoubleStateOf(0.0) }
    var total by remember { mutableDoubleStateOf(0.0) }
    val variantState by viewModel.variantId.collectAsState()
    var variant by remember { mutableStateOf(Variant()) }
    var variantSet by remember { mutableStateOf(mutableSetOf<Variant>() ) }
    var itemsCount by remember { mutableIntStateOf(0) }

    when(variantState){
        is ApiState.Error -> Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(variantState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            variant = (variantState as ApiState.Success).data.variant
            variantSet.add(variant)
        }
    }

    when(shoppingCartOrder){
        is ApiState.Error ->{
            isLoading = false
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
                    viewModel.getVariantById(orderItems.get(0).variant_id)
                }
            }
            subtotal = calculateSubtotal(orderItems)

        }
    }

    LaunchedEffect(orderItems){
        itemsCount = orderItems.size
    }

    LaunchedEffect (subtotal,draftOrder) {
        discountAmount = draftOrder.applied_discount?.amount?.toDouble() ?: 0.0
        total = draftOrder.subtotal_price.toDouble()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            VoucherBottomSheet(viewModel,draftOrder)
        },
        content = {
            if (orderItems.isNotEmpty()) {
                Scaffold(
                    topBar = { CustomTopBar(bottomSheetState,navController) },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)

                        ) {
                            if(isLoading) {
                                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(bottom = 230.dp)
                            ) {

                                if(orderItems.isNotEmpty()){
                                    items(orderItems.size) { index ->
                                        val orderItem = orderItems[index]
                                        ShoppingItem(viewModel,orderItem,draftOrder,variantSet,subtotal,{
                                            subtotal = calculateSubtotal(orderItems)
                                            total = subtotal
                                        },
                                        navController)

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
                                itemsCount
                            )
                        }
                    }
                )
            } else {
                if(!isLoading) {
                    ShoppingEmpty()
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
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = {
                    navController.navigate(Screen.HomeScreen.route)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "back"
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = "My Cart",
                style = TextStyle(fontSize = 20.sp),
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
                color = AppColors.MintGreen
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
    onItemUpdated: () -> Unit,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    CurrencyConverter.initialize(context)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(12.dp),
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
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Minus Button
                    IconButton(
                        onClick = {
                            val newQuantity = (item.quantity - 1).coerceAtLeast(1)
                            item.quantity = newQuantity
                            if (item.quantity == 1) {
                                Toast.makeText(context, "Min Limit reached", Toast.LENGTH_SHORT).show()
                            }

                            val updatedDraftItems = draftOrder.line_items.map { currentItem ->
                                if (currentItem == item) {
                                    currentItem.copy(quantity = newQuantity)
                                } else {
                                    currentItem
                                }
                            }
                            val updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                            val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)

                            coroutineScope.launch {
                                delay(200)
                                draftOrder.id?.let {
                                    viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                                }
                            }
                            onItemUpdated()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = "minus",
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Plus Button
                    IconButton(
                        onClick = {
                            if (subtotal <= 1000) {
                                val newQuantity = (item.quantity + 1).coerceAtMost(
                                    checkMaxQuantity(
                                        item.variant_id,
                                        variantSet
                                    )
                                )
                                item.quantity = newQuantity
                                if (item.quantity == checkMaxQuantity(item.variant_id, variantSet)) {
                                    Toast.makeText(context, "Max Limit reached", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                val updatedDraftItems = draftOrder.line_items.map { currentItem ->
                                    if (currentItem == item) {
                                        currentItem.copy(quantity = newQuantity)
                                    } else {
                                        currentItem
                                    }
                                }
                                val updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                                val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)

                                coroutineScope.launch {
                                    delay(200)
                                    draftOrder.id?.let {
                                        viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                                    }
                                }
                                onItemUpdated()
                            } else {
                                Toast.makeText(context, "Maximum payment reached", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
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
                if (draftOrder.line_items.size > 1) {
                    val updatedDraftItems = draftOrder.line_items.filter { it != item }
                    val updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                    val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)
                    draftOrder.id?.let {
                        viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                    }
                } else {
                    draftOrder.id?.let { viewModel.deleteDraftOrder(it) }
                    navController.popBackStack(Screen.ShoppingScreen.route, inclusive = true)
                    navController.navigate(Screen.ShoppingScreen.route)
                }
                onItemUpdated()
            }
        )
    }
}


@Composable
fun ShoppingView(
    modifier: Modifier,
    subtotal : String,
    discount: String,
    total : String,
    itemsCount : Int
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
                onClick = { /* Action for button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
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
fun ShoppingEmpty(){
    Column(
        modifier = Modifier
            .fillMaxSize()
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
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Looks like you have not added anything in your cart. Go ahead and explore top categories.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentWidth(),
            textAlign = TextAlign.Center
        )


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
}

@Composable
fun VoucherBottomSheet(viewModel: ShoppingCartViewModel,draftOrder: DraftOrder) {
    var voucherCode by remember { mutableStateOf("") }
    var discountApplied by remember { mutableStateOf(false) }
    var discountMessage by remember { mutableStateOf("") }

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
                        var discount = ""
                        if (voucherCode.endsWith("YALLABUY10")) {
                            discount = "10"
                            discountMessage = "10% discount applied!"
                            discountApplied = true
                        } else if (voucherCode.endsWith("YALLABUY30")) {
                            discount = "30"
                            discountMessage = "30% discount applied!"
                            discountApplied = true
                        } else if (voucherCode.endsWith("YALLABUY50")) {
                            discount = "50"
                            discountMessage = "50% discount applied!"
                            discountApplied = true
                        } else {
                                discountMessage = "Invalid voucher code"
                                discountApplied = false
                        }
                        if(discountApplied){
                            val newDraftOrder = draftOrder.apply { this.applied_discount = AppliedDiscount(value = discount) }
                            draftOrder.id?.let { viewModel.updateDraftOrder(it,DraftOrderRequest(newDraftOrder))
                                viewModel.getDraftOrders()}
                        }
                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
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
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Confirmation")
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
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun checkMaxQuantity(variantId : Long,variantSet : Set<Variant>) : Int{
    for(item in variantSet){
        if(item.id == variantId){
            if(item.inventory_quantity < 2){
                return 1
            } else {
                return (item.inventory_quantity / 2).toInt()
            }
        }
    }
    return 1
}

/*
@Composable
fun ShoppingScreenPreview() {
    YallaBuyTheme {
        ShoppingCartScreen()
    }
}*/
