package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AgeCategory
import com.example.data.model.Product
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val currencyRate by viewModel.currencyRate.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // 1. Hero Banner with Soft Pastel Styling & CTA
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(190.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "بانر متجر 2BabyPrint",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Tender gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFF1E1E24).copy(alpha = 0.78f))
                                )
                            )
                    )
                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Surface(
                            color = BabyRosePeach,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "✨ للأمهات، الأطفال، والحضانات والمدارس 🏫",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "صممي ملابس طفلك بلمسة حب سهلة وفورية",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "قطن 100% ناعم • أحبار آمنة للرضع • خصم جملة للحضانات",
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                                colors = ButtonDefaults.buttonColors(containerColor = BabySunYellow),
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("start_design_hero_btn")
                            ) {
                                Text("🎨 صممي الآن (سهل وسريع)", color = Color(0xFF332000), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateTo(AppScreen.TEMPLATES) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("قوالب جاهزة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Simplified 3-Step Guide for Mothers & Teachers ("سهل للجميع")
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem(number = "1", title = "اختاري القطعة", emoji = "👕")
                    Text(text = "←", color = BabyRosePeach, fontWeight = FontWeight.Bold)
                    StepItem(number = "2", title = "اكتبي الاسم والرسمة", emoji = "✏️")
                    Text(text = "←", color = BabyRosePeach, fontWeight = FontWeight.Bold)
                    StepItem(number = "3", title = "طباعة وتوصيل للبيت", emoji = "🎁")
                }
            }
        }

        // 3. Nurseries & Schools Special Announcement Banner (NEW)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { viewModel.setCategory(AgeCategory.NURSERY_SCHOOL) }
                    .testTag("nursery_school_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SchoolTeal.copy(alpha = 0.10f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SchoolTeal.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(SchoolTeal.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏫", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "خدمة الحضانات والمدارس ورياض الأطفال",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SchoolTeal
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SchoolTeal
                            ) {
                                Text(
                                    text = "خصم حتى 35%",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "زي الحضانة الموحد • تيشرتات تخرج الروضة 2026 • مرايل الأنشطة الفنية • طباعة أسماء الطلاب وشعار المدرسة بالجملة.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "عرض منتجات الحضانات",
                        tint = SchoolTeal,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // 4. Trust Badges (Tender Pastel Style)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    TrustBadgeItem(
                        icon = Icons.Outlined.CheckCircle,
                        title = "قطن مصري 100%",
                        subtitle = "ناعم ولطيف للبشرة",
                        tint = BabyRosePeach
                    )
                }
                item {
                    TrustBadgeItem(
                        icon = Icons.Outlined.Eco,
                        title = "أحبار نباتية آمنة",
                        subtitle = "خالية من الكيماويات",
                        tint = BabyMintGreen
                    )
                }
                item {
                    TrustBadgeItem(
                        icon = Icons.Outlined.School,
                        title = "خصم خاص للحضانات",
                        subtitle = "طباعة قوائم الفصول",
                        tint = SchoolTeal
                    )
                }
                item {
                    TrustBadgeItem(
                        icon = Icons.Outlined.CardGiftcard,
                        title = "تغليف هدية مجاني",
                        subtitle = "علبة وبطاقة فاخرة",
                        tint = BabySunYellow
                    )
                }
            }
        }

        // 5. Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("product_search_input"),
                placeholder = { Text("ابحث عن سالوبيت، زي الحضانة، تخرج الروضة، تيشرت...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "بحث", tint = BabyRosePeach)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = BabyCardBorder,
                    focusedBorderColor = BabyRosePeach
                )
            )
        }

        // 6. Category Selector Pills (Including Nurseries & Schools)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = "📂 اختاري الفئة المطلوبة:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AgeCategory.values()) { category ->
                        val isSelected = category == selectedCategory
                        val chipColor = if (category == AgeCategory.NURSERY_SCHOOL) SchoolTeal else BabyRosePeach

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategory(category) },
                            label = {
                                Text(
                                    text = if (category == AgeCategory.NURSERY_SCHOOL) "🏫 ${category.titleAr}" else "${category.titleAr} (${category.ageRange})",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = chipColor,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("category_chip_${category.name}")
                        )
                    }
                }
            }
        }

        // 7. Catalog Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👕 منتجات جاهزة للتخصيص الفوري",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${filteredProducts.size} منتجات",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 8. Product Cards List
        items(filteredProducts) { product ->
            ProductCatalogCard(
                product = product,
                currencySymbol = currencySymbol,
                currencyRate = currencyRate,
                onCustomize = { viewModel.startCustomizing(product) },
                onDetails = { viewModel.showProductDetails(product) }
            )
        }

        // 9. Customer Reviews & Nursery Feedback Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐ تجارب الأمهات وإدارات الروضات والحضانات", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "(تقييم 4.97 / 5)", fontSize = 11.sp, color = BabyRosePeach, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TestimonialBubble(
                        name = "أ. منيرة الدوسري - مديرة روضة وبراعم المستقبل",
                        comment = "طلبنا زي موحد وتيشرتات تخرج لـ 45 طفل في الروضة، طبعوا شعار الروضة واسم كل طفل بدقة مذهلة وخامة القطن فاخرة وباردة، والتغليف أسعد أولياء الأمور جداً!"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TestimonialBubble(
                        name = "أم تميم - الرياض",
                        comment = "التطبيق تصميمه سهل ومريح جداً للأمهات، كتبت اسم ابني واخترت رسمة الدبدوب بضغطة زر ووصلتني القطعة بأحلى من الصورة، شكراً 2BabyPrint!"
                    )
                }
            }
        }
    }
}

@Composable
private fun StepItem(number: String, title: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BabyRosePeach.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "خطوة $number", fontSize = 10.sp, color = BabyRosePeach, fontWeight = FontWeight.Bold)
        Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TrustBadgeItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(17.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ProductCatalogCard(
    product: Product,
    currencySymbol: String,
    currencyRate: Double,
    onCustomize: () -> Unit,
    onDetails: () -> Unit
) {
    val displayPrice = String.format("%.1f", product.basePrice * currencyRate)
    val isSchool = product.category == AgeCategory.NURSERY_SCHOOL
    val accentColor = if (isSchool) SchoolTeal else BabyRosePeach

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mockup Thumbnail
            Box(
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accentColor.copy(alpha = 0.08f))
                    .clickable { onDetails() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.blankDrawableRes),
                    contentDescription = product.titleAr,
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                    contentScale = ContentScale.Fit
                )
                if (product.isBestSeller) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(accentColor, RoundedCornerShape(bottomEnd = 10.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isSchool) "موصى للحضانات" else "الأكثر طلباً",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info & CTAs
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.titleAr,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${product.category.titleAr} (${product.category.ageRange})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$displayPrice $currencySymbol",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "⭐ ${product.rating}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isSchool) {
                    Text(
                        text = "🏷️ خصم كميات حتى 35% للفصول",
                        fontSize = 10.sp,
                        color = SchoolTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetails,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("التفاصيل", fontSize = 11.sp)
                    }
                    Button(
                        onClick = onCustomize,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("customize_btn_${product.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تخصيص الآن", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TestimonialBubble(name: String, comment: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BabyRosePeach)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = comment, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
        }
    }
}
