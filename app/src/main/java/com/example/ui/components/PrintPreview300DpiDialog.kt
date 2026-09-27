package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ArabicFontStyle
import com.example.data.model.DesignElement
import com.example.data.model.DesignElementType
import com.example.data.model.Product
import com.example.ui.theme.BabyMintGreen
import com.example.ui.theme.BabyRosePeach

@Composable
fun PrintPreview300DpiDialog(
    product: Product,
    fabricColorName: String,
    elements: List<DesignElement>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
                .testTag("print_preview_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "معاينة الطباعة",
                            tint = BabyRosePeach
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ملف الطباعة النهائي (300 DPI)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated Print Sheet (Transparent PNG / Vector DTF film)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF0F4F8))
                        .border(1.dp, Color(0xFFCFD8DC), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (elements.isEmpty()) {
                            Text(
                                text = "لم تقم بإضافة عناصر للطباعة بعد",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        } else {
                            elements.forEach { elem ->
                                when (elem.type) {
                                    DesignElementType.TEXT -> {
                                        Text(
                                            text = elem.content,
                                            fontSize = (elem.fontSizeSp * 0.9f).sp,
                                            fontWeight = if (elem.isBold) FontWeight.Bold else FontWeight.Normal,
                                            color = Color(elem.colorHex),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    DesignElementType.CLIPART, DesignElementType.IMAGE_STICKER -> {
                                        Text(
                                            text = elem.content,
                                            fontSize = 28.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Watermark / Print Specs overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "300 DPI | CMYK Vector Proof",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Technical Print Specification Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "📋 البيانات الفنية لماكينة الطباعة الرقمية:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SpecRow("المنتج المستهدف:", "${product.titleAr} (${product.apparelType.titleAr})")
                        SpecRow("لون القماش:", fabricColorName)
                        SpecRow("دقة الإخراج:", "300 DPI Ultra HD (Vector Transparent)")
                        SpecRow("تقنية الطباعة:", "DTG Direct-to-Garment / DTF Film")
                        SpecRow("نوع الحبر:", "أحبار مائية نباتية معتمدة آمنة للرضع (OEKO-TEX)")
                        SpecRow("منطقة الطباعة:", "28 × 35 سم (منطقة الصدر والوسط)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("المواصفات معتمدة وممتازة للطباعة")
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}
