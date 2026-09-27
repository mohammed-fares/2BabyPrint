package com.example.data.local

import kotlinx.coroutines.flow.Flow

class BabyPrintRepository(private val dao: BabyPrintDao) {
    val allCartItems: Flow<List<CartItemEntity>> = dao.getAllCartItems()
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val allSavedDesigns: Flow<List<SavedDesignEntity>> = dao.getAllSavedDesigns()

    suspend fun addToCart(item: CartItemEntity): Long {
        return dao.insertCartItem(item)
    }

    suspend fun updateCartItem(item: CartItemEntity) {
        dao.updateCartItem(item)
    }

    suspend fun removeCartItem(id: Long) {
        dao.deleteCartItemById(id)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    suspend fun placeOrder(order: OrderEntity): Long {
        val orderId = dao.insertOrder(order)
        dao.clearCart()
        return orderId
    }

    suspend fun saveDesign(design: SavedDesignEntity): Long {
        return dao.insertSavedDesign(design)
    }

    suspend fun deleteSavedDesign(id: Long) {
        dao.deleteSavedDesignById(id)
    }
}
