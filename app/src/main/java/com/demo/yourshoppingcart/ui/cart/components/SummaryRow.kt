package com.demo.yourshoppingcart.ui.cart.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SummaryRow(
    label: String,
    value: String,
    bold: Boolean = false,
    icon: ImageVector? = null,
    showDivider: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (bold) 6.dp else 4.dp), // premium spacing
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // LEFT: ICON + LABEL
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                    )
                }

                Text(
                    text = label,
                    style = if (bold)
                        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    else
                        MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // RIGHT: VALUE
            Text(
                text = value,
                style = if (bold)
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.bodyLarge,
                color = if (bold)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }

        // SUBTLE DIVIDER (Optional)
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                thickness = 1.dp
            )
        }
    }
}
