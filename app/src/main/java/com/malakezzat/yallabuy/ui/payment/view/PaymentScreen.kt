package com.malakezzat.yallabuy.ui.payment.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel
import com.malakezzat.yallabuy.ui.settings.view.AddressDialog
import com.malakezzat.yallabuy.ui.theme.AppColors
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme
import kotlinx.coroutines.delay
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(viewModel: PaymentViewModel, navController: NavController) {
    var address by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Cash on Delivery") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var expiration by remember { mutableStateOf(TextFieldValue("")) }
    val userAddressesState by viewModel.userAddresses.collectAsState()
    var userAddresses by remember { mutableStateOf(listOf<Address>()) }
    var addressesList by remember { mutableStateOf(listOf<String>()) }
    val defaultAddressState by viewModel.defaultAddressEvent.collectAsState()
    var defaultAddress by remember { mutableStateOf(Address()) }
    var isLoading by remember { mutableStateOf(false) }
    var navigateAfterDelay by remember { mutableStateOf(false) }
    val currentMonth = LocalDate.now().monthValue
    val currentYear = LocalDate.now().year % 100
    var vaildData by remember { mutableStateOf(false) }
    val shoppingCartOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
    var draftOrder by remember { mutableStateOf(DraftOrder()) }
    var showAddressDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val userId by remember {
        mutableStateOf(
            context.getSharedPreferences(
                "MySharedPrefs",
                Context.MODE_PRIVATE
            ).getLong("USER_ID", 0L)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getUserAddresses(userId)
        viewModel.getDraftOrders()
    }

    LaunchedEffect(navigateAfterDelay) {
        if (navigateAfterDelay) {
            delay(2000)
            navController.navigate(Screen.CheckoutScreen.route)
        }
    }


    when (userAddressesState) {
        is ApiState.Error -> Log.i(
            "paymentTest",
            "PaymentMethodScreen: userAddressesState ${(userAddressesState as ApiState.Error).message}"
        )

        ApiState.Loading -> {}
        is ApiState.Success -> {
            userAddresses = (userAddressesState as ApiState.Success).data.addresses
            getDefaultAddress(userAddresses).let {
                if (it != null) {
                    defaultAddress = it
                    LaunchedEffect(Unit) {
                        address = defaultAddress.address1.toString()
                    }
                }
            }
            addressesList = getAddressesList(userAddresses)
        }
    }

    when(shoppingCartOrderState){
        is ApiState.Error ->{
            Log.i("completeOrderTest", "shoppingCartOrderState: draftOrder ${(shoppingCartOrderState as ApiState.Error).message}") }
        ApiState.Loading -> {
        }
        is ApiState.Success -> {
            draftOrder = (shoppingCartOrderState as ApiState.Success).data
        }
    }

    Scaffold(
        topBar = { CustomTopBar(navController) },
        containerColor = Color.White,
        content = { paddingValues ->
            var addressLabel = "address"
            if (addressesList.isEmpty()) {
                addressLabel = "Make a new Address \uD83D\uDC49"
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isDropdownExpanded,
                        onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text(addressLabel) },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                            modifier = Modifier.menuAnchor(),
                            shape = RoundedCornerShape(30.dp),
                            readOnly = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                                disabledLabelColor = LocalContentColor.current.copy(
                                    LocalContentAlpha.current
                                ),
                                unfocusedBorderColor = AppColors.Teal
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            addressesList.forEach { addressOption ->
                                DropdownMenuItem(
                                    text = { Text(text = addressOption) },
                                    onClick = {
                                        address = addressOption
                                        userAddresses.find {
                                            it.address1 == addressOption
                                        }?.id?.let { viewModel.setDefaultAddress(userId, it) }
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = {
                            showAddressDialog = true
                        },
                        modifier = Modifier
                            .width(80.dp)
                            .height(64.dp)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                    ) {
                        Text("New")
                    }

                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Payment Method \uD83D\uDCB2",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Cash on Delivery",
                        onClick = { selectedPaymentMethod = "Cash on Delivery" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AppColors.Teal,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(text = "Cash on Delivery \uD83D\uDCB5")

                    RadioButton(
                        selected = selectedPaymentMethod == "Credit Card",
                        onClick = { selectedPaymentMethod = "Credit Card" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AppColors.Teal,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(text = "Credit Card \uD83D\uDCB3")
                }
                if (selectedPaymentMethod == "Credit Card") {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = cardHolderName,
                        onValueChange = { cardHolderName = it },
                        label = {  Text(
                            text = buildAnnotatedString {
                                append("Card Holder Name ")
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append("*")
                                }
                            },
                            fontSize = 18.sp
                        ) },
                        placeholder = { Text("Enter card holder name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        isError = cardHolderName.isEmpty(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.Teal,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = AppColors.Teal,
                            unfocusedLabelColor = AppColors.Teal,
                            errorBorderColor = Color.Red
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = {
                            if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                                cardNumber = it
                            }
                        },
                        label = { Text(
                            text = buildAnnotatedString {
                                append("Card Number ")
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append("*")
                                }
                            },
                            fontSize = 18.sp
                        ) },
                        placeholder = { Text("4111 1111 1111 1111") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = cardNumber.length != 16,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.Teal,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = AppColors.Teal,
                            unfocusedLabelColor = AppColors.Teal,
                            errorBorderColor = Color.Red
                        )
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = expiration,
                            onValueChange = { input ->
                                var text = input.text
                                var newCursorPos = input.selection.end

                                if (text.length == 2 && !text.contains("/")) {
                                    val month = text.toIntOrNull()
                                    if (month != null && month in 1..12) {
                                        text = text.padStart(2, '0') + "/"
                                        newCursorPos = text.length
                                    }
                                }

                                if (text.length <= 5) {
                                    expiration = TextFieldValue(
                                        text = text,
                                        selection = TextRange(newCursorPos)
                                    )
                                }

                                if (text.length == 5) {
                                    val year = text.takeLast(2).toIntOrNull()
                                    if (year != null && year >= 24) {
                                        expiration = TextFieldValue(text, TextRange(text.length))
                                    } else {
                                        expiration = TextFieldValue(
                                            text.dropLast(1),
                                            TextRange(text.length - 1)
                                        )
                                    }
                                }
                            },
                            label = { Text(
                                text = buildAnnotatedString {
                                    append("Expiration ")
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color.Red,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )
                                    ) {
                                        append("*")
                                    }
                                },
                                fontSize = 18.sp
                            ) },
                            placeholder = { Text("MM/YY") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = expiration.text.length != 5,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = AppColors.Teal,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = AppColors.Teal,
                                unfocusedLabelColor = AppColors.Teal,
                                errorBorderColor = Color.Red
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = {
                                if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                    cvv = it
                                }
                            },
                            label = { Text(
                                text = buildAnnotatedString {
                                    append("CVV ")
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color.Red,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )
                                    ) {
                                        append("*")
                                    }
                                },
                                fontSize = 18.sp
                            ) },
                            placeholder = { Text("123") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = cvv.length != 3,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = AppColors.Teal,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = AppColors.Teal,
                                unfocusedLabelColor = AppColors.Teal,
                                errorBorderColor = Color.Red
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (address.isNotBlank()) {
                                if (selectedPaymentMethod == "Credit Card") {
                                    if (cardHolderName.isBlank()){
                                        vaildData = false
                                        Toast.makeText(
                                            context,
                                            "Please Enter Card Holder Name",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        vaildData = true
                                    }
                                    if(cardNumber.isNotBlank()) {
                                        if ((cardNumber.length == 16).not()) {
                                            vaildData = false
                                            Toast.makeText(
                                                context,
                                                "Please Enter a valid Card Number",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            vaildData = true
                                        }
                                    } else {
                                        vaildData = false
                                        Toast.makeText(
                                            context,
                                            "Please Enter a Card Number",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    if(expiration.text.isNotBlank() && expiration.text.length == 5) {
                                        Log.i("dateTest", "PaymentMethodScreen: giving month ${expiration.text.substring(0, 2)}")
                                        Log.i("dateTest", "PaymentMethodScreen: currentMonth ${currentMonth}")
                                        Log.i("dateTest", "PaymentMethodScreen: giving year ${expiration.text.takeLast(2)}")
                                        Log.i("dateTest", "PaymentMethodScreen: currentYear ${currentYear}")

                                        if ((((expiration.text.substring(0, 2)
                                                .toInt() > currentMonth) &&
                                                    (expiration.text.substring(0, 2)
                                                        .toInt() <= 12)
                                                    && (expiration.text.takeLast(2)
                                                .toInt() == currentYear)) ||
                                                    ((expiration.text.substring(0, 2).toInt() <= 12) &&
                                            (expiration.text.takeLast(2).toInt() > currentYear))).not()
                                        ) {
                                            vaildData = false
                                            Toast.makeText(
                                                context,
                                                "Please Enter a valid Expiration Date",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            vaildData = true
                                        }
                                    } else {
                                        vaildData = false
                                        Toast.makeText(
                                            context,
                                            "Please Enter a valid Expiration Date",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    if (cvv.isNotBlank()) {
                                        if ((cvv.length == 3).not()) {
                                            vaildData = false
                                            Toast.makeText(
                                                context,
                                                "Please Enter a valid Expiration CVV",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            vaildData = true
                                        }
                                    } else {
                                        vaildData = false
                                        Toast.makeText(
                                            context,
                                            "Please Enter a CVV",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                    if(vaildData) {
                                        isLoading = true
                                        navigateAfterDelay = true
                                    }
                                } else {
                                    if(draftOrder.total_price.toDouble() < 10000.0) {
                                        isLoading = true
                                        navigateAfterDelay = true
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Exceeded the cash purchase limit. Choose credit.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "You Should Have at least one Address",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = AppColors.Teal,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Checkout")
                        }
                    }

        }
            if (showAddressDialog) {
                AddressDialog(
                    showDialog = showAddressDialog,
                    onDismiss = {
                        showAddressDialog = false
                    },
                    onWriteAddress = {
                        navController.navigate(Screen.AddressScreen.createRoute(""))
                        showAddressDialog = false
                    },
                    onGetFromMap = {
                        val latitude = 27.18039285293778
                        val longitude = 31.186714348461493
                        navController.navigate("mapScreen/$latitude/$longitude")
                        showAddressDialog = false
                    }
                )

            }
        }

    )

}

@Composable
fun CustomTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.navigate(Screen.ShoppingScreen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "back Icon",
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "Payment Method",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold, color = AppColors.Teal
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

fun getDefaultAddress(addresses: List<Address>): Address? {
    return addresses.find { it.default == true }
}

fun getAddressesList(addresses: List<Address>): List<String> {
    return addresses.map { it.address1.toString() }
}