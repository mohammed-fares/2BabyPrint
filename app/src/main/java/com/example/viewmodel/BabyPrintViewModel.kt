package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    STUDIO,
    TEMPLATES,
    SAVED_DESIGNS,
    ORDERS,
    CART
}

data class StudioDesignState(
    val product: Product = sampleProducts[0],
    val fabricColorId: String = "white",
    val selectedSize: String = sampleProducts[0].defaultSize,
    val isFrontSide: Boolean = true,
    val frontElements: List<DesignElement> = listOf(
        DesignElement(
            id = "default_clip",
            type = DesignElementType.CLIPART,
            content = "👑",
            offsetX = 0f,
            offsetY = -35f,
            scale = 1.3f
        ),
        DesignElement(
            id = "default_text",
            type = DesignElementType.TEXT,
            content = "أمير العائلة الصغير",
            offsetX = 0f,
            offsetY = 15f,
            fontSizeSp = 22f,
            colorHex = 0xFFFF758F,
            fontStyle = ArabicFontStyle.PLAYFUL
        )
    ),
    val backElements: List<DesignElement> = emptyList(),
    val selectedElementId: String? = null,
    // School / Nursery bulk mode
    val isSchoolOrderMode: Boolean = false,
    val schoolOrNurseryName: String = "",
    val studentQuantity: Int = 1,
    val studentRosterNames: String = "" // Comma-separated or line-separated list of children
)

class BabyPrintViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BabyPrintRepository

    val cartItems: StateFlow<List<CartItemEntity>>
    val orders: StateFlow<List<OrderEntity>>
    val savedDesigns: StateFlow<List<SavedDesignEntity>>

    init {
        val db = BabyPrintDatabase.getDatabase(application)
        repository = BabyPrintRepository(db.babyPrintDao())
        cartItems = repository.allCartItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        orders = repository.allOrders.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        savedDesigns = repository.allSavedDesigns.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    // Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Home Catalog Filtering
    private val _selectedCategory = MutableStateFlow(AgeCategory.ALL)
    val selectedCategory: StateFlow<AgeCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setCategory(category: AgeCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredProducts = combine(_selectedCategory, _searchQuery) { cat, query ->
        sampleProducts.filter { product ->
            val matchesCategory = (cat == AgeCategory.ALL || product.category == cat)
            val matchesQuery = query.isBlank() ||
                    product.titleAr.contains(query, ignoreCase = true) ||
                    product.descriptionAr.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), sampleProducts)

    // Product Detail modal state
    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    fun showProductDetails(product: Product) {
        _selectedProductForDetail.value = product
    }

    fun dismissProductDetails() {
        _selectedProductForDetail.value = null
    }

    // Currency selection (Default SAR)
    private val _currencySymbol = MutableStateFlow("ر.س")
    val currencySymbol: StateFlow<String> = _currencySymbol.asStateFlow()

    private val _currencyRate = MutableStateFlow(1.0)
    val currencyRate: StateFlow<Double> = _currencyRate.asStateFlow()

    fun setCurrency(symbol: String, rate: Double) {
        _currencySymbol.value = symbol
        _currencyRate.value = rate
    }

    // Studio Design State
    private val _studioState = MutableStateFlow(StudioDesignState())
    val studioState: StateFlow<StudioDesignState> = _studioState.asStateFlow()

    fun startCustomizing(product: Product, isSchoolMode: Boolean = (product.category == AgeCategory.NURSERY_SCHOOL)) {
        val initialText = if (isSchoolMode) "حضانة أزهار الغد" else "اسم طفلك"
        val initialIcon = if (isSchoolMode) "🎓" else "👑"
        val initialColor = if (isSchoolMode) "pastel_blue" else "white"

        _studioState.update { current ->
            current.copy(
                product = product,
                fabricColorId = initialColor,
                selectedSize = product.defaultSize,
                selectedElementId = null,
                isSchoolOrderMode = isSchoolMode,
                frontElements = listOf(
                    DesignElement(
                        id = "default_clip",
                        type = DesignElementType.CLIPART,
                        content = initialIcon,
                        offsetX = 0f,
                        offsetY = -35f,
                        scale = 1.3f
                    ),
                    DesignElement(
                        id = "default_text",
                        type = DesignElementType.TEXT,
                        content = initialText,
                        offsetX = 0f,
                        offsetY = 15f,
                        fontSizeSp = 22f,
                        colorHex = if (isSchoolMode) 0xFF118AB2 else 0xFFFF758F,
                        fontStyle = if (isSchoolMode) ArabicFontStyle.BOLD_KIDS else ArabicFontStyle.PLAYFUL
                    )
                )
            )
        }
        _currentScreen.value = AppScreen.STUDIO
    }

    fun applyTemplate(template: TemplatePreset) {
        val matchingProduct = sampleProducts.find { it.apparelType == template.recommendedApparelType }
            ?: sampleProducts[0]

        val isSchoolMode = template.categoryName == "الحضانات والمدارس"

        _studioState.update {
            it.copy(
                product = matchingProduct,
                fabricColorId = template.defaultFabricColorId,
                frontElements = template.elements,
                selectedElementId = null,
                isFrontSide = true,
                isSchoolOrderMode = isSchoolMode
            )
        }
        _currentScreen.value = AppScreen.STUDIO
    }

    fun setFabricColor(colorId: String) {
        _studioState.update { it.copy(fabricColorId = colorId) }
    }

    fun setApparelSize(size: String) {
        _studioState.update { it.copy(selectedSize = size) }
    }

    fun toggleSide(isFront: Boolean) {
        _studioState.update { it.copy(isFrontSide = isFront, selectedElementId = null) }
    }

    fun selectElement(elementId: String?) {
        _studioState.update { it.copy(selectedElementId = elementId) }
    }

    fun setSchoolOrderMode(isSchool: Boolean) {
        _studioState.update { it.copy(isSchoolOrderMode = isSchool) }
    }

    fun setStudentRoster(roster: String) {
        _studioState.update { it.copy(studentRosterNames = roster) }
    }

    fun addTextElement(
        text: String,
        colorHex: Long = 0xFF2B2D42,
        fontStyle: ArabicFontStyle = ArabicFontStyle.PLAYFUL,
        fontSizeSp: Float = 22f,
        isCurved: Boolean = false
    ) {
        val newElement = DesignElement(
            id = "text_${UUID.randomUUID().toString().take(6)}",
            type = DesignElementType.TEXT,
            content = text,
            offsetX = 0f,
            offsetY = 0f,
            colorHex = colorHex,
            fontStyle = fontStyle,
            fontSizeSp = fontSizeSp,
            isCurved = isCurved
        )
        _studioState.update { current ->
            if (current.isFrontSide) {
                current.copy(
                    frontElements = current.frontElements + newElement,
                    selectedElementId = newElement.id
                )
            } else {
                current.copy(
                    backElements = current.backElements + newElement,
                    selectedElementId = newElement.id
                )
            }
        }
    }

    fun addClipartElement(clipart: ClipartItem) {
        val newElement = DesignElement(
            id = "clip_${UUID.randomUUID().toString().take(6)}",
            type = DesignElementType.CLIPART,
            content = clipart.iconEmoji,
            offsetX = 0f,
            offsetY = 0f,
            scale = 1.3f
        )
        _studioState.update { current ->
            if (current.isFrontSide) {
                current.copy(
                    frontElements = current.frontElements + newElement,
                    selectedElementId = newElement.id
                )
            } else {
                current.copy(
                    backElements = current.backElements + newElement,
                    selectedElementId = newElement.id
                )
            }
        }
    }

    fun updateSelectedElement(update: (DesignElement) -> DesignElement) {
        _studioState.update { current ->
            val selId = current.selectedElementId ?: return@update current
            if (current.isFrontSide) {
                val updated = current.frontElements.map { if (it.id == selId) update(it) else it }
                current.copy(frontElements = updated)
            } else {
                val updated = current.backElements.map { if (it.id == selId) update(it) else it }
                current.copy(backElements = updated)
            }
        }
    }

    fun deleteSelectedElement() {
        _studioState.update { current ->
            val selId = current.selectedElementId ?: return@update current
            if (current.isFrontSide) {
                current.copy(
                    frontElements = current.frontElements.filter { it.id != selId },
                    selectedElementId = null
                )
            } else {
                current.copy(
                    backElements = current.backElements.filter { it.id != selId },
                    selectedElementId = null
                )
            }
        }
    }

    fun moveElement(elementId: String, deltaX: Float, deltaY: Float) {
        _studioState.update { current ->
            val updateList = { list: List<DesignElement> ->
                list.map { elem ->
                    if (elem.id == elementId) {
                        elem.copy(
                            offsetX = (elem.offsetX + deltaX).coerceIn(-95f, 95f),
                            offsetY = (elem.offsetY + deltaY).coerceIn(-95f, 95f)
                        )
                    } else elem
                }
            }
            if (current.isFrontSide) {
                current.copy(frontElements = updateList(current.frontElements))
            } else {
                current.copy(backElements = updateList(current.backElements))
            }
        }
    }

    fun saveDraftToDatabase(title: String) {
        val state = _studioState.value
        val preview = state.frontElements.find { it.type == DesignElementType.TEXT }?.content
            ?: state.product.titleAr
        viewModelScope.launch {
            repository.saveDesign(
                SavedDesignEntity(
                    designTitle = title.ifBlank { "تصميم ${state.product.titleAr}" },
                    productId = state.product.id,
                    productTitleAr = state.product.titleAr,
                    fabricColorId = state.fabricColorId,
                    selectedSize = state.selectedSize,
                    designElementsJson = "Front:${state.frontElements.size}|Back:${state.backElements.size}",
                    previewText = preview
                )
            )
        }
    }

    // Add current studio customization to Cart
    fun addCurrentStudioDesignToCart(quantity: Int = 1) {
        val state = _studioState.value
        val colorName = availableFabricColors.find { it.id == state.fabricColorId }?.nameAr ?: "أبيض"
        val customText = state.frontElements.find { it.type == DesignElementType.TEXT }?.content
            ?: state.backElements.find { it.type == DesignElementType.TEXT }?.content ?: ""

        val entity = CartItemEntity(
            productId = state.product.id,
            productTitleAr = state.product.titleAr,
            apparelType = state.product.apparelType.titleAr,
            selectedSize = state.selectedSize,
            fabricColorId = state.fabricColorId,
            fabricColorNameAr = colorName,
            unitPrice = state.product.basePrice,
            quantity = quantity.coerceAtLeast(1),
            frontElementsSummary = "عناصر الواجهة: ${state.frontElements.size}",
            backElementsSummary = "عناصر الخلف: ${state.backElements.size}",
            customTextPreview = customText
        )

        viewModelScope.launch {
            repository.addToCart(entity)
            _currentScreen.value = AppScreen.CART
        }
    }

    // Batch Add for Nursery/School class roster
    fun addClassRosterToCart(names: List<String>) {
        val state = _studioState.value
        val colorName = availableFabricColors.find { it.id == state.fabricColorId }?.nameAr ?: "أبيض"

        viewModelScope.launch {
            names.filter { it.isNotBlank() }.forEach { studentName ->
                val entity = CartItemEntity(
                    productId = state.product.id,
                    productTitleAr = "${state.product.titleAr} - ${studentName.trim()}",
                    apparelType = state.product.apparelType.titleAr,
                    selectedSize = state.selectedSize,
                    fabricColorId = state.fabricColorId,
                    fabricColorNameAr = colorName,
                    unitPrice = state.product.basePrice,
                    quantity = 1,
                    frontElementsSummary = "زي مدرسي / حضانة",
                    backElementsSummary = "",
                    customTextPreview = studentName.trim()
                )
                repository.addToCart(entity)
            }
            _currentScreen.value = AppScreen.CART
        }
    }

    fun updateCartQuantity(item: CartItemEntity, newQty: Int) {
        viewModelScope.launch {
            if (newQty <= 0) {
                repository.removeCartItem(item.id)
            } else {
                repository.updateCartItem(item.copy(quantity = newQty))
            }
        }
    }

    fun removeCartItem(itemId: Long) {
        viewModelScope.launch {
            repository.removeCartItem(itemId)
        }
    }

    // Promo Code & Volume Discount
    private val _couponCode = MutableStateFlow("")
    val couponCode: StateFlow<String> = _couponCode.asStateFlow()

    private val _manualDiscountPercent = MutableStateFlow(0)
    val manualDiscountPercent: StateFlow<Int> = _manualDiscountPercent.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError: StateFlow<String?> = _couponError.asStateFlow()

    fun applyCoupon(code: String) {
        _couponCode.value = code
        val trimmed = code.trim().uppercase()
        when {
            trimmed == "BABY20" -> {
                _manualDiscountPercent.value = 20
                _couponError.value = null
            }
            trimmed == "SCHOOL30" || trimmed == "NURSERY30" -> {
                _manualDiscountPercent.value = 30
                _couponError.value = null
            }
            trimmed.isBlank() -> {
                _manualDiscountPercent.value = 0
                _couponError.value = null
            }
            else -> {
                _manualDiscountPercent.value = 0
                _couponError.value = "كوبون غير صالح. جربي BABY20 أو كود الحضانات SCHOOL30"
            }
        }
    }

    // Calculate dynamic wholesale bulk discount percentage
    fun calculateBulkDiscountPercent(totalItemCount: Int): Int {
        val manual = _manualDiscountPercent.value
        val autoBulk = when {
            totalItemCount >= 50 -> 35
            totalItemCount >= 25 -> 25
            totalItemCount >= 10 -> 15
            else -> 0
        }
        return maxOf(manual, autoBulk)
    }

    // Place Order
    private val _lastPlacedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastPlacedOrder: StateFlow<OrderEntity?> = _lastPlacedOrder.asStateFlow()

    fun checkout(
        customerName: String,
        phone: String,
        city: String,
        address: String,
        paymentMethod: String,
        total: Double,
        discount: Double,
        itemsCount: Int,
        summary: String
    ) {
        viewModelScope.launch {
            val orderNum = "2BP-${(1000..9999).random()}"
            val order = OrderEntity(
                orderNumber = orderNum,
                customerName = customerName,
                customerPhone = phone,
                city = city,
                address = address,
                totalAmount = total,
                discountAmount = discount,
                itemsSummary = summary,
                itemsCount = itemsCount,
                paymentMethod = paymentMethod,
                status = "جاري التجهيز والطباعة الرقمية (DTG/DTF)"
            )
            repository.placeOrder(order)
            _lastPlacedOrder.value = order
        }
    }

    fun dismissOrderSuccessDialog() {
        _lastPlacedOrder.value = null
    }

    fun resumeSavedDesign(saved: SavedDesignEntity) {
        val product = sampleProducts.find { it.id == saved.productId } ?: sampleProducts[0]
        _studioState.update {
            it.copy(
                product = product,
                fabricColorId = saved.fabricColorId,
                selectedSize = saved.selectedSize,
                selectedElementId = null,
                isFrontSide = true
            )
        }
        _currentScreen.value = AppScreen.STUDIO
    }

    fun deleteSavedDesign(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedDesign(id)
        }
    }
}
