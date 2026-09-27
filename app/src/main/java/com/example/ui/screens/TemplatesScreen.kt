package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
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
import com.example.data.model.DesignElementType
import com.example.data.model.TemplatePreset
import com.example.data.model.availableFabricColors
import com.example.data.model.sampleTemplates
import com.example.ui.theme.BabyCardBorder
import com.example.ui.theme.BabyRosePeach
import com.example.ui.theme.SchoolTeal
import com.example.viewmodel.BabyPrintViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("الكل") }
    val categories = listOf("الكل", "الحضانات والمدارس", "أعياد الميلاد", "رمضان والأعياد", "سبوع ومواليد")

    val filtered = if (selectedCategory == "الكل") {
        sampleTemplates
    } else {
        sampleTemplates.filter { it.categoryName == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = BabyRosePeach
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "القوالب الجاهزة للمناسبات والحضانات",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "اختاري قالباً جاهزاً وعدلي اسم الطفل أو اسم الحضانة والروضة بلمسة واحدة",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    val chipColor = if (cat == "الحضانات والمدارس") SchoolTeal else BabyRosePeach

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = if (cat == "الحضانات والمدارس") "🏫 $cat" else cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // Template Cards
        items(filtered) { template ->
            TemplateCardItem(
                template = template,
                onApply = { viewModel.applyTemplate(template) }
            )
        }
    }
}

@Composable
private fun TemplateCardItem(
    template: TemplatePreset,
    onApply: () -> Unit
) {
    val fabricColor = availableFabricColors.find { it.id == template.defaultFabricColorId }
        ?: availableFabricColors[0]
    val isSchool = template.categoryName == "الحضانات والمدارس"
    val accentColor = if (isSchool) SchoolTeal else BabyRosePeach

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BabyCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = template.categoryName,
                        fontSize = 11.sp,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "الفئة المستفيدة: ${template.targetAudience}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Visual Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(fabricColor.color.copy(alpha = 0.65f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = template.previewEmoji, fontSize = 34.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    val textElems = template.elements.filter { it.type == DesignElementType.TEXT }
                    textElems.forEach { textElem ->
                        Text(
                            text = textElem.content,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(textElem.colorHex)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = template.titleAr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "القطعة المقترحة: ${template.recommendedApparelType.titleAr}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier.testTag("apply_template_btn_${template.id}")
                ) {
                    Icon(imageVector = Icons.Default.ColorLens, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تطبيق وتعديل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
