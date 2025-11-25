package com.demo.yourshoppingcart.ui.checkout.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.payment.data.model.PaymentModel

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onAdd: (PaymentModel.Card) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    // Expiry input handler
    fun formatExpiry(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length >= 3 -> "${digits.take(2)}/${digits.drop(2).take(2)}"
            digits.length >= 1 -> digits
            else -> ""
        }
    }

    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }
    val isExpiryValid = expiry.matches(Regex("(0[1-9]|1[0-2])/([0-9]{2})")) // MM/YY
    val isNameValid = name.isNotBlank()

    val isValid = isCardNumberValid && isCvvValid && isExpiryValid && isNameValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Card") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Card Holder Name") },
                    singleLine = true,
                    isError = !isNameValid && name.isNotEmpty()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 16 && it.all { ch -> ch.isDigit() }) cardNumber = it
                    },
                    label = { Text("Card Number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isCardNumberValid && cardNumber.isNotEmpty()
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = {
                            if (it.length <= 3 && it.all { ch -> ch.isDigit() }) cvv = it
                        },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isCvvValid && cvv.isNotEmpty()
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { expiry = formatExpiry(it) },
                        label = { Text("Expiry (MM/YY)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isExpiryValid && expiry.isNotEmpty()
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname (Optional)") },
                    singleLine = true
                )

                if (!isValid && (cardNumber.isNotEmpty() || cvv.isNotEmpty() || expiry.isNotEmpty())) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Please check all fields are correct: Card 16 digits, CVV 3 digits, Expiry MM/YY",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAdd(
                        PaymentModel.Card(
                            cardHolderName = name,
                            cardNumber = cardNumber.toLong(),
                            cvv = cvv.toInt(),
                            expiryDate = expiry,
                            nickName = nickname
                        )
                    )
                },
                enabled = isValid
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}