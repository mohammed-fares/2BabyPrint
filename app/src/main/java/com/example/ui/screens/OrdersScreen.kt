package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.theme.BabyCardBorder
import com.example.ui.theme.BabyMintGreen
import com.example.ui.theme.BabyRosePeach
import com.example.ui.theme.BabySunYellow
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val currencyRate by viewModel.currencyRate.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(BabyRosePeach.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📦", fontSize = 36.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "لا توجد طلبات سابقة حتى الآن",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "صممي أول قطعة مميزة لطفلك واستمتعي بخصم 20%",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("ابدأي التسوق الآن")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "طلباتي السابقة وتتبع الطباعة",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                items(orders) { order ->
                    OrderTrackingCard(
                        order = order,
                        currencySymbol = currencySymbol,
                        currencyRate = currencyRate
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderTrackingCard(
    order: OrderEntity,
    currencySymbol: String,
    currencyRate: Double
) {
    val dateStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar")).format(Date(order.timestamp))
    val priceFormatted = String.format("%.1f", order.totalAmount * currencyRate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.orderNumber}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Order # and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = BabyRosePeach)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = order.orderNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2E7D32).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = order.status,
                        color = Color(0xFF2E7D32),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timeline tracker
            ProductionTimeline(currentStage = 2)

            Spacer(modifier = Modifier.height(12.dp))

            // Details
            Text(
                text = "القطع: ${order.itemsSummary}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "المستلم: ${order.customerName} - ${order.city} (${order.address})",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "تاريخ الطلب: $dateStr",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "طريقة الدفع: ${order.paymentMethod}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "الإجمالي: $priceFormatted $currencySymbol",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = BabyRosePeach
                )
            }
        }
    }
}

@Composable
private fun ProductionTimeline(currentStage: Int) {
    val stages = listOf(
        "استلام وتجهيز Vector" to "✅",
        "طباعة DTG/DTF" to "🖨️",
        "تغليف الهدية" to "🎁",
        "التوصيل" to "🚚"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        stages.forEachIndexed { index, (label, emoji) ->
            val isPassed = index + 1 <= currentStage
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = emoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = if (isPassed) FontWeight.Bold else FontWeight.Normal,
                    color = if (isPassed) BabyRosePeach else Color.Gray
                )
            }
        }
    }
}
