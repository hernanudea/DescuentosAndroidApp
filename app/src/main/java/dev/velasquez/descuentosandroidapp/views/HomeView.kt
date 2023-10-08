package dev.velasquez.descuentosandroidapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.velasquez.descuentosandroidapp.R
import dev.velasquez.descuentosandroidapp.components.CustomAlert
import dev.velasquez.descuentosandroidapp.components.CustomButton
import dev.velasquez.descuentosandroidapp.components.CustomTextField
import dev.velasquez.descuentosandroidapp.components.SpaceH
import dev.velasquez.descuentosandroidapp.components.TwoCustomCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name), color = Color.White
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }) {
        ContentHomeView(it)
    }
}

@Composable
fun ContentHomeView(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var price by remember { mutableStateOf("") }
        var discount by remember { mutableStateOf("") }
        var newPrice by remember { mutableStateOf(0.0) }
        var totalDiscount by remember { mutableStateOf(0.0) }
        var message by remember { mutableStateOf("") }

        TwoCustomCard(
            title1 = stringResource(id = R.string.totalPrice),
            title2 = stringResource(id = R.string.totalDiscount),
            number1 = newPrice, number2 = totalDiscount
        )
        CustomTextField(
            value = price,
            onValueChange = { price = it },
            label = stringResource(id = R.string.price),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        SpaceH()
        CustomTextField(
            value = discount,
            onValueChange = { discount = it },
            label = stringResource(id = R.string.discount),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        SpaceH(10.dp)
        CustomButton(text = stringResource(id = R.string.calc_discount)) {
            if (price.isEmpty() || discount.isEmpty()) {
                // message = stringResource(id = R.string.alert_message_empty)
                message = "Los campos precio y porcentaje son obligatorios"
            } else if (discount.toDouble() < 0 || discount.toDouble() > 100) {
                //message = stringResource(id = R.string.alert_message_percentage)
                message = "El porcentaje debe estar entre 0 y 100"
                discount = ""
            } else {
                newPrice = calculateNewPrice(price.toDouble(), discount.toDouble())
                totalDiscount = calculateTotalDiscount(price.toDouble(), discount.toDouble())
            }
        }
        SpaceH()
        CustomButton(text = stringResource(id = R.string.clear_fields), color = Color.Red) {
            price = ""
            discount = ""
            newPrice = 0.0
            totalDiscount = 0.0
        }
        if (message.isNotEmpty()) {
            CustomAlert(title = stringResource(id = R.string.alert_title),
                //message = stringResource(id = R.string.alert_message_empty),
                message = message,
                confirmText = stringResource(id = R.string.acept),
                onConfirm = { message = "" }) { }
        }
    }
}

fun calculateNewPrice(price: Double, discount: Double): Double {
    return price - (calculateTotalDiscount(price, discount))
}

fun calculateTotalDiscount(price: Double, percentageDiscount: Double): Double {
    return kotlin.math.round(price * percentageDiscount / 100)
}