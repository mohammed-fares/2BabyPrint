package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BabyRosePeach
import com.example.ui.theme.BabySunYellow
import com.example.ui.theme.SchoolTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyPrintTopBar(
    viewModel: BabyPrintViewModel,
    cartCount: Int,
    savedDraftsCount: Int
) {
    var showCurrencyMenu by remember { mutableStateOf(false) }
    val currencySymbol by viewModel.currencySymbol.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Announcement banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BabyRosePeach)
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✨ خصم 20% بكود BABY20 | خصم كميات حتى 35% للحضانات والمدارس 🏫",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Main top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Brand Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { viewModel.navigateTo(AppScreen.HOME) }
                        .testTag("brand_logo_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BabyRosePeach.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👶",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "2BabyPrint",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = BabyRosePeach
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SchoolTeal.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "وحضانات",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SchoolTeal,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "طباعة ملابس الأطفال والحضانات والمدارس",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Actions: Currency Switcher, Saved drafts, Cart badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Currency Menu Button
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showCurrencyMenu = true }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("currency_selector_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currencySymbol,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "تغيير العملة",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showCurrencyMenu,
                            onDismissRequest = { showCurrencyMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("ر.س - ريال سعودي (SAR)") },
                                onClick = {
                                    viewModel.setCurrency("ر.س", 1.0)
                                    showCurrencyMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("د.إ - درهم إماراتي (AED)") },
                                onClick = {
                                    viewModel.setCurrency("د.إ", 0.98)
                                    showCurrencyMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("ج.م - جنيه مصري (EGP)") },
                                onClick = {
                                    viewModel.setCurrency("ج.م", 13.0)
                                    showCurrencyMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("$ - دولار أمريكي (USD)") },
                                onClick = {
                                    viewModel.setCurrency("$", 0.27)
                                    showCurrencyMenu = false
                                }
                            )
                        }
                    }

                    // Saved Drafts shortcut
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.SAVED_DESIGNS) },
                        modifier = Modifier.testTag("saved_designs_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (savedDraftsCount > 0) {
                                    Badge(containerColor = BabySunYellow) {
                                        Text("$savedDraftsCount", color = Color(0xFF332000), fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder,
                                contentDescription = "تصاميمي المحفوظة",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Cart button
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.CART) },
                        modifier = Modifier.testTag("top_cart_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(containerColor = BabyRosePeach) {
                                        Text("$cartCount", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "سلة المشتريات",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
