package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BabyPrintBottomNav
import com.example.ui.components.BabyPrintTopBar
import com.example.ui.components.ProductDetailDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Ensure RTL layout for Arabic-first experience
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BabyPrintApp()
                }
            }
        }
    }
}

@Composable
fun BabyPrintApp(viewModel: BabyPrintViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val savedDrafts by viewModel.savedDesigns.collectAsState()
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val currencyRate by viewModel.currencyRate.collectAsState()

    // Handle back button: return to Home if on another screen
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val cartTotalCount = cartItems.sumOf { it.quantity }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            BabyPrintTopBar(
                viewModel = viewModel,
                cartCount = cartTotalCount,
                savedDraftsCount = savedDrafts.size
            )
        },
        bottomBar = {
            BabyPrintBottomNav(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                cartBadgeCount = cartTotalCount
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.STUDIO -> StudioScreen(viewModel = viewModel)
                AppScreen.TEMPLATES -> TemplatesScreen(viewModel = viewModel)
                AppScreen.SAVED_DESIGNS -> SavedDesignsScreen(viewModel = viewModel)
                AppScreen.ORDERS -> OrdersScreen(viewModel = viewModel)
                AppScreen.CART -> CartScreen(viewModel = viewModel)
            }

            // Product Details Modal Dialog
            selectedProductForDetail?.let { product ->
                ProductDetailDialog(
                    product = product,
                    currencySymbol = currencySymbol,
                    currencyRate = currencyRate,
                    onCustomize = {
                        viewModel.startCustomizing(product)
                        viewModel.dismissProductDetails()
                    },
                    onDismiss = { viewModel.dismissProductDetails() }
                )
            }
        }
    }
}
