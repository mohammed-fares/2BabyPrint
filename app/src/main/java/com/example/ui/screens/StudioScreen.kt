package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.MockupCanvas
import com.example.ui.components.PrintPreview300DpiDialog
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    val studioState by viewModel.studioState.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val currencyRate by viewModel.currencyRate.collectAsState()

    // Modals & Sheets
    var showTextDialog by remember { mutableStateOf(false) }
    var showClipartSheet by remember { mutableStateOf(false) }
    var show300DpiDialog by remember { mutableStateOf(false) }
    var showSaveDraftDialog by remember { mutableStateOf(false) }
    var showRosterDialog by remember { mutableStateOf(false) }
    var draftTitleInput by remember { mutableStateOf("") }
    var rosterInput by remember {
        mutableStateOf("ريان، سارة، تميم، ليلى، عمر، جود، كريم، نورة، حمزة، تالا")
    }

    val activeFabricColor = availableFabricColors.find { it.id == studioState.fabricColorId }
        ?: availableFabricColors[0]
    val activeElements = if (studioState.isFrontSide) studioState.frontElements else studioState.backElements
    val selectedElement = activeElements.find { it.id == studioState.selectedElementId }

    val calculatedPrice = String.format("%.1f", studioState.product.basePrice * currencyRate)
    val isSchool = studioState.isSchoolOrderMode
    val accentColor = if (isSchool) SchoolTeal else BabyRosePeach

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Studio Mode Switcher: Single Child vs School/Nursery Bulk
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = studioState.product.titleAr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${studioState.product.apparelType.titleAr} • مقاس: ${studioState.selectedSize}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$calculatedPrice $currencySymbol",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor
                        )
                        Text(
                            text = if (isSchool) "خصم كميات متاح" else "شامل الطباعة",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle Single Child vs School / Nursery Mode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterChip(
                        selected = !isSchool,
                        onClick = { viewModel.setSchoolOrderMode(false) },
                        label = { Text("👶 تصميم طفل واحد (للأمهات)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BabyRosePeach,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    FilterChip(
                        selected = isSchool,
                        onClick = { viewModel.setSchoolOrderMode(true) },
                        label = { Text("🏫 طلب حضانة أو مدرسة (جملة)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SchoolTeal,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 2. School / Nursery Class Roster Action Banner (if School Mode)
        if (isSchool) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                color = SchoolTeal.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, SchoolTeal.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "📋 طباعة قائمة أسماء طلاب الفصل",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = SchoolTeal
                        )
                        Text(
                            text = "أدخلي أسماء الأطفال لإنشاء زي لكل طفل مع خصم جملة تلقائي 15% - 35%",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { showRosterDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolTeal),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("open_roster_dialog_btn")
                    ) {
                        Text("إدخال الأسماء", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Front / Back Toggle & 300 DPI High-Res Preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    FilterChip(
                        selected = studioState.isFrontSide,
                        onClick = { viewModel.toggleSide(true) },
                        label = { Text("الواجهة الأمامية", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentColor,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("switch_side_front")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = !studioState.isFrontSide,
                        onClick = { viewModel.toggleSide(false) },
                        label = { Text("الظهر (الخلف)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentColor,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("switch_side_back")
                    )
                }
            }

            OutlinedButton(
                onClick = { show300DpiDialog = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_300dpi_preview")
            ) {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("معاينة 300 DPI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // 4. Interactive Garment Canvas
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            MockupCanvas(
                product = studioState.product,
                fabricColor = activeFabricColor.color,
                isFrontSide = studioState.isFrontSide,
                elements = activeElements,
                selectedElementId = studioState.selectedElementId,
                onSelectElement = { viewModel.selectElement(it) },
                onMoveElement = { id, dx, dy -> viewModel.moveElement(id, dx, dy) },
                onDeleteElement = { viewModel.deleteSelectedElement() },
                onRotateElement = { rot ->
                    viewModel.updateSelectedElement { it.copy(rotation = it.rotation + rot) }
                },
                modifier = Modifier.testTag("interactive_mockup_canvas")
            )
        }

        // 5. Quick Color Palette Carousel (Soft Pastels & School Shades)
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 لون القماش: ${activeFabricColor.nameAr}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "خامات قطنية 100% باردة",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(availableFabricColors) { colorOpt ->
                    val isSelected = colorOpt.id == studioState.fabricColorId
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(colorOpt.color)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) accentColor else Color(0xFFD6DBE1),
                                shape = CircleShape
                            )
                            .clickable { viewModel.setFabricColor(colorOpt.id) }
                            .testTag("color_pill_${colorOpt.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "محدد",
                                tint = if (colorOpt.id == "white" || colorOpt.id == "pastel_yellow") Color.Black else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6. Sizes Selector
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text(text = "📏 مقاس القطعة:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(studioState.product.sizes) { size ->
                    val isSelected = size == studioState.selectedSize
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) accentColor else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setApparelSize(size) }
                    ) {
                        Text(
                            text = size,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 7. Selected Element Quick Controls
        if (selectedElement != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "تحكم بالعنصر: ${selectedElement.content.take(15)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        IconButton(
                            onClick = { viewModel.deleteSelectedElement() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = Color.Red)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilledTonalButton(
                            onClick = {
                                viewModel.updateSelectedElement { it.copy(scale = (it.scale + 0.15f).coerceAtMost(2.5f)) }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("تكبير +", fontSize = 11.sp)
                        }
                        FilledTonalButton(
                            onClick = {
                                viewModel.updateSelectedElement { it.copy(scale = (it.scale - 0.15f).coerceAtLeast(0.5f)) }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("تصغير -", fontSize = 11.sp)
                        }
                        FilledTonalButton(
                            onClick = {
                                viewModel.updateSelectedElement { it.copy(rotation = (it.rotation + 45f) % 360f) }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("تدوير ↻", fontSize = 11.sp)
                        }
                        FilledTonalButton(
                            onClick = {
                                viewModel.updateSelectedElement { it.copy(offsetX = 0f, offsetY = 0f) }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("توسيط 🎯", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 8. Studio Quick Action Buttons (Add Text, Clipart, Templates)
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "🛠️ أدوات التخصيص السريعة:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Add Text (Child name or School name)
                Button(
                    onClick = { showTextDialog = true },
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("studio_add_text_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(imageVector = Icons.Default.TextFields, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isSchool) "شعار/اسم الروضة" else "كتابة الاسم", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Add Clipart
                Button(
                    onClick = { showClipartSheet = true },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("studio_add_clipart_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BabySkyBlue)
                ) {
                    Icon(imageVector = Icons.Default.EmojiEmotions, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("الرسومات", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Browse Templates
                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.TEMPLATES) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("قوالب جاهزة", fontSize = 11.sp)
                }
            }
        }

        // 9. Primary Checkout & Save Actions
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        draftTitleInput = "تصميم ${studioState.product.titleAr}"
                        showSaveDraftDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("studio_save_draft_btn"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.BookmarkAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حفظ مسودة", fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.addCurrentStudioDesignToCart() },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("studio_add_to_cart_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("إضافة إلى السلة", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Modal: Class Roster Input for Nurseries/Schools
    if (showRosterDialog) {
        AlertDialog(
            onDismissRequest = { showRosterDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏫 قائمة أسماء طلاب الفصل والحضانة", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "اكتبي أو الصقي أسماء الأطفال (مفصولة بفاصلة أو سطر جديد)، وسيقوم التطبيق بإعداد قطعة مخصصة لكل طفل تلقائياً!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = rosterInput,
                        onValueChange = { rosterInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("roster_input_field"),
                        placeholder = { Text("ريان، سارة، عمر، ليلى...") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val studentCount = rosterInput.split(",", "،", "\n").count { it.isNotBlank() }
                    val discount = when {
                        studentCount >= 50 -> 35
                        studentCount >= 25 -> 25
                        studentCount >= 10 -> 15
                        else -> 0
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SchoolTeal.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "عدد الطلاب: $studentCount أطفال | خصم الجملة المحتسب: $discount%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SchoolTeal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val namesList = rosterInput.split(",", "،", "\n").map { it.trim() }.filter { it.isNotBlank() }
                        if (namesList.isNotEmpty()) {
                            viewModel.addClassRosterToCart(namesList)
                            showRosterDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolTeal),
                    modifier = Modifier.testTag("confirm_class_roster_btn")
                ) {
                    Text("إضافة كل الطلاب إلى السلة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRosterDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Modal: 300 DPI Preview
    if (show300DpiDialog) {
        PrintPreview300DpiDialog(
            product = studioState.product,
            fabricColorName = activeFabricColor.nameAr,
            elements = activeElements,
            onDismiss = { show300DpiDialog = false }
        )
    }

    // Modal: Add / Customize Text
    if (showTextDialog) {
        AddTextDialog(
            isSchoolMode = isSchool,
            onDismiss = { showTextDialog = false },
            onAdd = { text, colorHex, fontStyle, fontSize, isCurved ->
                viewModel.addTextElement(text, colorHex, fontStyle, fontSize, isCurved)
                showTextDialog = false
            }
        )
    }

    // Modal: Clipart & Stickers Sheet
    if (showClipartSheet) {
        ClipartSelectionSheet(
            isSchoolMode = isSchool,
            onDismiss = { showClipartSheet = false },
            onSelectClipart = { clipart ->
                viewModel.addClipartElement(clipart)
                showClipartSheet = false
            }
        )
    }

    // Modal: Save Draft Dialog
    if (showSaveDraftDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDraftDialog = false },
            title = { Text("حفظ التصميم كمسودة", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("أدخل اسماً لهذا التصميم لحفظه والعودة لتعديله لاحقاً:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftTitleInput,
                        onValueChange = { draftTitleInput = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveDraftToDatabase(draftTitleInput)
                        showSaveDraftDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("حفظ المسودة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDraftDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
private fun AddTextDialog(
    isSchoolMode: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String, Long, ArabicFontStyle, Float, Boolean) -> Unit
) {
    var textInput by remember { mutableStateOf(if (isSchoolMode) "حضانة أزهار الغد" else "اسم طفلك") }
    var selectedFont by remember { mutableStateOf(if (isSchoolMode) ArabicFontStyle.BOLD_KIDS else ArabicFontStyle.PLAYFUL) }
    var selectedColorHex by remember { mutableStateOf(if (isSchoolMode) 0xFF118AB2 else 0xFFFF758F) }
    var fontSize by remember { mutableStateOf(24f) }
    var isCurved by remember { mutableStateOf(false) }

    val sampleColorOptions = listOf(
        0xFFFF758F to "وردي باستيل",
        0xFF4EA8DE to "أزرق سماوي",
        0xFF118AB2 to "كحلي مدرسي",
        0xFFFFD166 to "أصفر شمس",
        0xFF2B2D42 to "كحلي داكن",
        0xFF70D6BC to "أخضر نعناعي",
        0xFFC77DFF to "لافندر ملكي"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isSchoolMode) "كتابة اسم الروضة أو شعار المدرسة" else "كتابة اسم الطفل أو العبارة",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text(if (isSchoolMode) "اسم الحضانة / المدرسة / الفصل" else "الاسم المراد طباعته") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("text_input_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("نوع الخط العربي:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ArabicFontStyle.values().forEach { font ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedFont = font }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFont == font,
                            onClick = { selectedFont = font }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${font.displayName} (${font.sampleText})", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("لون خط الطباعة:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleColorOptions) { (hex, _) ->
                        val isSelected = selectedColorHex == hex
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) Color.Black else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorHex = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("حجم الخط: ${fontSize.toInt()} sp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = fontSize,
                    onValueChange = { fontSize = it },
                    valueRange = 14f..40f
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onAdd(textInput, selectedColorHex, selectedFont, fontSize, isCurved)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSchoolMode) SchoolTeal else BabyRosePeach),
                modifier = Modifier.testTag("confirm_add_text_btn")
            ) {
                Text("إدراج في التصميم")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
private fun ClipartSelectionSheet(
    isSchoolMode: Boolean,
    onDismiss: () -> Unit,
    onSelectClipart: (ClipartItem) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(if (isSchoolMode) "الحضانات والمدارس" else "الكل") }
    val categories = listOf("الكل", "الحضانات والمدارس", "أعياد الميلاد", "حيوانات الغابة", "تيجان ونجوم", "رمضان والأعياد", "سبوع ومواليد")

    val filteredClipart = if (selectedCategory == "الكل") {
        sampleClipartLibrary
    } else {
        sampleClipartLibrary.filter { it.category == selectedCategory }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎨 مكتبة الرسومات والشعارات", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (cat == "الحضانات والمدارس") SchoolTeal else BabySkyBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier.height(260.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        filteredClipart.chunked(3).forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { item ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .clickable { onSelectClipart(item) }
                                            .testTag("clipart_item_${item.id}"),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(text = item.iconEmoji, fontSize = 28.sp)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.nameAr,
                                                fontSize = 9.sp,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق")
            }
        }
    )
}
