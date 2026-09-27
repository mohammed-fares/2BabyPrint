package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val productTitleAr: String,
    val apparelType: String,
    val selectedSize: String,
    val fabricColorId: String,
    val fabricColorNameAr: String,
    val unitPrice: Double,
    val quantity: Int = 1,
    val frontElementsSummary: String, // serialized preview string or elements count
    val backElementsSummary: String,
    val customTextPreview: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val city: String,
    val address: String,
    val totalAmount: Double,
    val discountAmount: Double,
    val itemsSummary: String,
    val itemsCount: Int,
    val paymentMethod: String,
    val status: String, // "قيد المراجعة", "جاري الطباعة الرقمية DTF/DTG", "تم الشحن والتسليم"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_designs")
data class SavedDesignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val designTitle: String,
    val productId: String,
    val productTitleAr: String,
    val fabricColorId: String,
    val selectedSize: String,
    val designElementsJson: String,
    val previewText: String,
    val timestamp: Long = System.currentTimeMillis()
)
