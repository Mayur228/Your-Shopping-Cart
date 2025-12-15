package com.demo.yourshoppingcart.ui.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity

@Composable
fun CategoryList(
    categories: List<HomeEntity.CategoryEntity>,
    onCategorySelected: (cat: String) -> Unit,
    isDark: Boolean
) {
    var selectedCat by remember { mutableStateOf("All") }

    val finalList = listOf(HomeEntity.CategoryEntity(id = "", name = "All", img = "")) + categories

    LazyRow(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(finalList) { category ->
            val isSelected = selectedCat == category.name

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.12f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
            )

            val bgColor by animateColorAsState(
                if (isSelected)
                    MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.25f else 0.18f)
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.18f else 0.5f),
                tween(250)
            )

            val textColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                tween(250)
            )

            Column(
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .padding(horizontal = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AssistChip(
                    onClick = {
                        selectedCat = category.name
                        onCategorySelected(category.name)
                    },
                    label = { Text(category.name, color = textColor) },
                    shape = RoundedCornerShape(50),
                    colors = AssistChipDefaults.assistChipColors(containerColor = bgColor)
                )

                if (isSelected) {
                    Spacer(modifier = Modifier.padding(top = 4.dp))
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(3.dp)
                            .width(28.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                    )
                } else {
                    Spacer(modifier = Modifier.padding(top = 7.dp))
                }
            }
        }
    }
}
