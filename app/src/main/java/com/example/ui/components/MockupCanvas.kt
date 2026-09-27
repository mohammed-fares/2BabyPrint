package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.BabyRosePeach
import com.example.ui.theme.FabricWhite
import kotlin.math.roundToInt

@Composable
fun MockupCanvas(
    product: Product,
    fabricColor: Color,
    isFrontSide: Boolean,
    elements: List<DesignElement>,
    selectedElementId: String?,
    onSelectElement: (String?) -> Unit,
    onMoveElement: (String, Float, Float) -> Unit,
    onDeleteElement: () -> Unit,
    onRotateElement: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .pointerInput(Unit) {
                // Clicking on empty area deselects
                detectDragGestures(
                    onDragStart = { onSelectElement(null) },
                    onDrag = { _, _ -> }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // 1. Garment Mockup Image with dynamic fabric tint
        val colorFilter = if (fabricColor == FabricWhite) null else ColorFilter.tint(fabricColor, BlendMode.Multiply)

        Image(
            painter = painterResource(id = product.blankDrawableRes),
            contentDescription = "قالب المنتج الفارغ",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentScale = ContentScale.Fit,
            colorFilter = colorFilter
        )

        // 2. Printable Safety Area (Dotted rectangle)
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(210.dp)
                .offset(y = 12.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            // Label showing printable safety zone
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-10).dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isFrontSide) "منطقة الطباعة الأمامية (300 DPI)" else "منطقة الطباعة الخلفية",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 3. Render Custom Design Elements
        elements.forEach { element ->
            val isSelected = element.id == selectedElementId

            Box(
                modifier = Modifier
                    .offset {
                        // Map relative offset (-100..100) to pixels
                        IntOffset(
                            x = (element.offsetX * 2.2f).roundToInt(),
                            y = (element.offsetY * 2.2f + 16f).roundToInt()
                        )
                    }
                    .scale(element.scale)
                    .rotate(element.rotation)
                    .pointerInput(element.id) {
                        detectDragGestures(
                            onDragStart = { onSelectElement(element.id) },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onMoveElement(
                                    element.id,
                                    dragAmount.x / 2.2f,
                                    dragAmount.y / 2.2f
                                )
                            }
                        )
                    }
                    .then(
                        if (isSelected) {
                            Modifier
                                .border(
                                    width = 1.5.dp,
                                    color = BabyRosePeach,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(6.dp)
                        } else {
                            Modifier.padding(6.dp)
                        }
                    )
            ) {
                when (element.type) {
                    DesignElementType.TEXT -> {
                        val textFontFamily = when (element.fontStyle) {
                            ArabicFontStyle.PLAYFUL -> FontFamily.SansSerif
                            ArabicFontStyle.ELEGANT -> FontFamily.Serif
                            ArabicFontStyle.MODERN -> FontFamily.Default
                            ArabicFontStyle.BOLD_KIDS -> FontFamily.Cursive
                        }
                        Text(
                            text = element.content,
                            fontSize = element.fontSizeSp.sp,
                            fontWeight = if (element.isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (element.fontStyle == ArabicFontStyle.ELEGANT) FontStyle.Italic else FontStyle.Normal,
                            fontFamily = textFontFamily,
                            color = Color(element.colorHex),
                            textAlign = TextAlign.Center
                        )
                    }
                    DesignElementType.CLIPART -> {
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 1.5f).sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    DesignElementType.IMAGE_STICKER -> {
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 1.5f).sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // If selected, show floating handles (Delete & Rotate)
                if (isSelected) {
                    // Delete Button (top right)
                    IconButton(
                        onClick = onDeleteElement,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-10).dp)
                            .background(BabyRosePeach, CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "حذف العنصر",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Rotate Handle (bottom right)
                    IconButton(
                        onClick = { onRotateElement(15f) },
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 10.dp, y = 10.dp)
                            .background(MaterialTheme.colorScheme.secondary, CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateRight,
                            contentDescription = "تدوير العنصر",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Side badge (الأمام / الخلف)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(BabyRosePeach, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isFrontSide) "الواجهة الأمامية" else "الواجهة الخلفية",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Real-time Print Quality Pill (300 DPI)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2E7D32).copy(alpha = 0.9f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🟢 300 DPI DTG جاهز",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
