package com.igdtuw.smartcanteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.activity.compose.BackHandler

data class FoodItem(val name: String, val price: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanteenApp()
        }
    }
}

@Composable
fun CanteenApp() {
    var screen by remember { mutableStateOf("menu") }
    var cart by remember { mutableStateOf(listOf<FoodItem>()) }
    var token by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }
    BackHandler {
        screen = when (screen) {
            "cart" -> "menu"
            "payment" -> "cart"
            "status" -> "menu"
            else -> "menu"
        }
    }

    when (screen) {
        "menu" -> MenuScreen(
            onAdd = { cart = cart + it },
            onGoToCart = { screen = "cart" }
        )
        "cart" -> CartScreen(
            cart = cart,
            onPay = {
                token = Random.nextInt(1000, 9999)
                screen = "payment"
            }
        )
        "payment" -> PaymentScreen(
            onUPI = {
                status = "PAID"
                screen = "status"
            },
            onCash = {
                status = "PENDING"
                screen = "status"
            }
        )
        "status" -> StatusScreen(token, status)
    }
}
@Composable
fun MenuScreen(onAdd: (FoodItem) -> Unit, onGoToCart: () -> Unit) {

    val foodList = listOf(
        FoodItem("Burger 🍔", 50),
        FoodItem("Pizza 🍕", 100),
        FoodItem("Sandwich 🥪", 40),
        FoodItem("Cold Drink 🥤", 30)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {

        Text(
            text = "🍽 Canteen",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Choose your food",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(foodList) { item ->
                FoodCard(item, onAdd)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onGoToCart,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp)   // 👈 yahan add karo
        ) {
            Text("Go to Cart 🛒", color = Color.Black)
        }
    }
}
@Composable
fun CartScreen(cart: List<FoodItem>, onPay: () -> Unit) {

    val total = cart.sumOf { it.price }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "🧾 Your Cart",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Total: ₹$total",
            color = Color(0xFF22C55E),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onPay,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text("Proceed to Payment")
        }
    }
}

@Composable
fun PaymentScreen(onUPI: () -> Unit, onCash: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choose Payment Method")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onUPI,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Pay via UPI")
        }

        Button(
            onClick = onCash,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Pay via Cash")
        }
    }
}
@Composable
fun StatusScreen(token: Int, status: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("🎟 Token #$token", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (status == "PAID") "✅ Payment Done" else "⏳ Waiting for Payment",
            color = Color(0xFF22C55E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("🍳 Preparing your order...", color = Color.Gray)
    }
}
@Composable
fun FoodCard(item: FoodItem, onAdd: (FoodItem) -> Unit) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = item.name,
                    color = Color.White
                )

                Text(
                    text = "₹${item.price}",
                    color = Color(0xFF22C55E)
                )
            }

            Button(
                onClick = { onAdd(item) },
                shape = RoundedCornerShape(50.dp)   // 👈 yahan bhi
            ) {
                Text("Add")
            }
        }
    }
}