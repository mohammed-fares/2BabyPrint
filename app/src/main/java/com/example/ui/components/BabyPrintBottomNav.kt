package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BabyRosePeach
import com.example.viewmodel.AppScreen

data class NavItem(
    val screen: AppScreen,
    val titleAr: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun BabyPrintBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    cartBadgeCount: Int = 0
) {
    val items = listOf(
        NavItem(AppScreen.HOME, "الرئيسية", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavItem(AppScreen.STUDIO, "الاستوديو", Icons.Filled.ColorLens, Icons.Outlined.ColorLens, "nav_studio"),
        NavItem(AppScreen.TEMPLATES, "قوالب جاهزة", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_templates"),
        NavItem(AppScreen.ORDERS, "طلباتي", Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong, "nav_orders"),
        NavItem(AppScreen.CART, "السلة", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart, "nav_cart")
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("main_bottom_nav"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen == AppScreen.CART && cartBadgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = BabyRosePeach) {
                                    Text("$cartBadgeCount", fontSize = 10.sp)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.titleAr
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.titleAr
                        )
                    }
                },
                label = {
                    Text(
                        text = item.titleAr,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BabyRosePeach,
                    selectedTextColor = BabyRosePeach,
                    indicatorColor = BabyRosePeach.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
