package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedDesignEntity
import com.example.ui.theme.BabyRosePeach
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BabyPrintViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedDesignsScreen(
    viewModel: BabyPrintViewModel,
    modifier: Modifier = Modifier
) {
    val savedDesigns by viewModel.savedDesigns.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (savedDesigns.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(BabyRosePeach.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎨", fontSize = 36.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "لا توجد مسودات تصاميم محفوظة",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "يمكنك حفظ أي تصميم تقوم به في الاستوديو للعودة إليه لاحقاً",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                    colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("فتح استوديو التصميم")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "تصاميمي المحفوظة (${savedDesigns.size})",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                items(savedDesigns) { draft ->
                    SavedDesignCard(
                        draft = draft,
                        onResume = { viewModel.resumeSavedDesign(draft) },
                        onDelete = { viewModel.deleteSavedDesign(draft.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedDesignCard(
    draft: SavedDesignEntity,
    onResume: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale("ar")).format(Date(draft.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_design_${draft.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BabyRosePeach.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👕", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = draft.designTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${draft.productTitleAr} - مقاس: ${draft.selectedSize}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "تاريخ الحفظ: $dateStr",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = Color.Gray)
            }

            Button(
                onClick = onResume,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BabyRosePeach),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("تعديل", fontSize = 11.sp)
            }
        }
    }
}
