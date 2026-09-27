package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CartItemEntity
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val currencyRate by viewModel.currencyRate.collectAsState()
    val couponCode by viewModel.couponCode.collectAsState()
    val couponError by viewModel.couponError.collectAsState()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()

    var couponInput by remember { mutableStateOf(couponCode) }
    var showCheckoutModal by remember { mutableStateOf(false) }

    val totalPiecesCount = cartItems.sumOf { it.quantity }
    val appliedDiscountPercent = viewModel.calculateBulkDiscountPercent(totalPiecesCount)

    // Calculations
    val subtotal = cartItems.sumOf { it.unitPrice * it.quantity }
    val discountAmount = subtotal * (appliedDiscountPercent / 100.0)
    val shippingCost = if (subtotal > 200.0 || cartItems.isEmpty()) 0.0 else 25.0
    val grandTotal = (subtotal - discountAmount + shippingCost).coerceAtLeast(0.0)

    val convertedSubtotal = String.format("%.1f", subtotal * currencyRate)
    val convertedDiscount = String.format("%.1f", discountAmount * currencyRate)
    val convertedShipping = String.format("%.1f", shippingCost * currencyRate)
    val convertedTotal = String.format("%.1f", grandTotal * currencyRate)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(BabyRosePeach.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛒", fontSize = 40.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "سلة التسوق فارغة حالياً",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "صممي ملابس طفلك أو اطلبي زي الحضانة والفصول الآن بسهولة!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("empty_cart_browse_btn")
                ) {
                    Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تصفح المنتجات")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سلة المشتريات ($totalPiecesCount قطع)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                        if (totalPiecesCount >= 10) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SchoolTeal.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "🏫 خصم كميات الجملة مفعل: $appliedDiscountPercent%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SchoolTeal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        currencySymbol = currencySymbol,
                        currencyRate = currencyRate,
                        onQuantityChange = { newQty -> viewModel.updateCartQuantity(item, newQty) },
                        onRemove = { viewModel.removeCartItem(item.id) }
                    )
                }

                // Volume Discount Tier Banner
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🏷️ سلم خصومات الجملة للحضانات والمدارس:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = SchoolTeal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "10 - 24 قطعة: 15% خصم", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "25 - 49 قطعة: 25% خصم", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "50+ قطعة: 35% خصم", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SchoolTeal)
                            }
                        }
                    }
                }

                // Promo Coupon Section
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "🎟️ هل لديك كوبون خصم؟", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponInput,
                                    onValueChange = { couponInput = it },
                                    placeholder = { Text("BABY20 أو كود المدرسة SCHOOL30") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("coupon_input_field"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { viewModel.applyCoupon(couponInput) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                                    modifier = Modifier.testTag("apply_coupon_btn")
                                ) {
                                    Text("تطبيق")
                                }
                            }
                            if (appliedDiscountPercent > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "✅ تم تفعيل خصم إجمالي $appliedDiscountPercent% بنجاح!",
                                    color = Color(0xFF2E7D32),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (couponError != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = couponError ?: "",
                                    color = Color.Red,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Price Breakdown Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "ملخص الفاتورة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            PriceRow("المجموع الفرعي:", "$convertedSubtotal $currencySymbol")
                            if (appliedDiscountPercent > 0) {
                                PriceRow("الخصم الإجمالي ($appliedDiscountPercent%):", "-$convertedDiscount $currencySymbol", isDiscount = true)
                            }
                            PriceRow(
                                "الشحن والتغليف:",
                                if (shippingCost == 0.0) "مجاني 🎁" else "$convertedShipping $currencySymbol"
                            )
                            Divider(modifier = Modifier.padding(vertical = 10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "الإجمالي النهائي:", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                Text(
                                    text = "$convertedTotal $currencySymbol",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = BabyRosePeach
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showCheckoutModal = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("proceed_checkout_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach)
                            ) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("إتمام الشراء والدفع الآمن", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Checkout Modal Dialog
        if (showCheckoutModal) {
            CheckoutDialog(
                totalFormatted = "$convertedTotal $currencySymbol",
                onDismiss = { showCheckoutModal = false },
                onConfirmOrder = { name, phone, city, address, payment ->
                    viewModel.checkout(
                        customerName = name,
                        phone = phone,
                        city = city,
                        address = address,
                        paymentMethod = payment,
                        total = grandTotal,
                        discount = discountAmount,
                        itemsCount = totalPiecesCount,
                        summary = cartItems.joinToString(", ") { "${it.productTitleAr} (${it.selectedSize})" }
                    )
                    showCheckoutModal = false
                }
            )
        }

        // Order Placed Success Dialog
        if (lastPlacedOrder != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissOrderSuccessDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎉 تم تأكيد طلبك بنجاح!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                text = {
                    Column {
                        Text("شكراً لتسوقك من 2BabyPrint!")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "رقم الطلب: ${lastPlacedOrder?.orderNumber}",
                            fontWeight = FontWeight.Bold,
                            color = BabyRosePeach
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("حالة الطلب: جاري تجهيز وطباعة القطع بأعلى جودة.")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("تم إرسال تفاصيل الفاتورة والتتبع إلى رقمك.")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.dismissOrderSuccessDialog()
                            viewModel.navigateTo(AppScreen.ORDERS)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach)
                    ) {
                        Text("عرض تفاصيل طلبي")
                    }
                }
            )
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItemEntity,
    currencySymbol: String,
    currencyRate: Double,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val itemPrice = String.format("%.1f", item.unitPrice * item.quantity * currencyRate)

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().testTag("cart_item_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visual badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BabyRosePeach.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (item.productTitleAr.contains("حضانة") || item.productTitleAr.contains("تخرج")) "🏫" else "👶", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productTitleAr,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "المقاس: ${item.selectedSize} • اللون: ${item.fabricColorNameAr}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (item.customTextPreview.isNotBlank()) {
                    Text(
                        text = "طباعة: \"${item.customTextPreview}\"",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BabyRosePeach
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$itemPrice $currencySymbol",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BabyRosePeach
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "حذف", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilledIconButton(
                        onClick = { onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(26.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "${item.quantity}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilledIconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(26.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = BabyRosePeach)
                    ) {
                        Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDiscount) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CheckoutDialog(
    totalFormatted: String,
    onDismiss: () -> Unit,
    onConfirmOrder: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("أم ريان / روضة براعم المستقبل") }
    var phone by remember { mutableStateOf("0501234567") }
    var city by remember { mutableStateOf("الرياض") }
    var address by remember { mutableStateOf("حي النرجس، شارع الملك سلمان") }
    var selectedPayment by remember { mutableStateOf("مدى (Mada)") }

    val paymentOptions = listOf(
        "مدى (Mada)",
        "أبل باي (Apple Pay)",
        "تمارا (4 دفعات بدون فوائد)",
        "بطاقة ائتمانية (Visa / MC)",
        "فاتورة رسمية للحضانات والمدارس",
        "الدفع عند الاستلام (COD)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("بيانات التوصيل والدفع", fontWeight = FontWeight.Bold, fontSize = 15.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم المستلم (الأم أو الحضانة)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("checkout_name_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الجوال") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("المدينة") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("العنوان والحي بالتفصيل") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("طريقة الدفع:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                paymentOptions.forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPayment == method,
                            onClick = { selectedPayment = method }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = method, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "المبلغ المطلوب سداده: $totalFormatted",
                        fontWeight = FontWeight.Bold,
                        color = BabyRosePeach,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onConfirmOrder(name, phone, city, address, selectedPayment)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                modifier = Modifier.testTag("confirm_order_final_btn")
            ) {
                Text("تأكيد ودفع الطلب")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
