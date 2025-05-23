package net.adhikary.mrtbuddy.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.balance
import mrtbuddy.composeapp.generated.resources.cardId
import mrtbuddy.composeapp.generated.resources.lastScan
import mrtbuddy.composeapp.generated.resources.payments
import mrtbuddy.composeapp.generated.resources.unnamedCard
import mrtbuddy.composeapp.generated.resources.visibility
import mrtbuddy.composeapp.generated.resources.visibility_off
import net.adhikary.mrtbuddy.data.CardEntity
import net.adhikary.mrtbuddy.ui.theme.DarkMRTPass
import net.adhikary.mrtbuddy.ui.theme.DarkRapidPass
import net.adhikary.mrtbuddy.ui.theme.LightMRTPass
import net.adhikary.mrtbuddy.ui.theme.LightRapidPass
import net.adhikary.mrtbuddy.utils.TimeUtils
import net.adhikary.mrtbuddy.utils.isRapidPassIdm
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CardItem(
    card: CardEntity,
    balance: Int?,
    onCardSelected: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(240.dp)
            .clickable { onCardSelected() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column {
            // Colored stripe at the top
            val isDarkTheme = isSystemInDarkTheme()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (isRapidPassIdm(card.idm)) {
                            if (isDarkTheme) DarkRapidPass else LightRapidPass
                        } else {
                            if (isDarkTheme) DarkMRTPass else LightMRTPass
                        }
                    ),
                contentAlignment = androidx.compose.ui.Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = card.name ?: stringResource(Res.string.unnamedCard),
                            style = MaterialTheme.typography.titleLarge,
                            color = (if (isDarkTheme) Color.Black else Color.White).copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Rename card",
                            tint = (if (isDarkTheme) Color.Black else Color.White).copy(alpha = 0.9f),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                                .clickable { onRenameClick() }
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete card",
                            tint = (if (isDarkTheme) Color.Black else Color.White).copy(alpha = 0.9f),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp)
                                .clickable { onDeleteClick() }
                        )
                    }
                }
            }

            // Card details
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            var isIdVisible by remember { mutableStateOf(false) }
                            // Balance Row
                            if (balance != null) {
                                Row(
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.payments),
                                        contentDescription = "Balance",
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .size(20.dp),
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Column {
                                        Text(
                                            modifier = Modifier.padding(bottom = 2.dp),
                                            text = stringResource(Res.string.balance),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = "৳ $balance",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }

                            // Card ID Row
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (isIdVisible) Res.drawable.visibility else Res.drawable.visibility_off
                                    ),
                                    contentDescription = if (isIdVisible) "Hide card ID" else "Show card ID",
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(20.dp)
                                        .clickable { isIdVisible = !isIdVisible },
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Column {
                                    Text(
                                        modifier = Modifier.padding(bottom = 2.dp),
                                        text = stringResource(Res.string.cardId),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = if (isIdVisible) card.idm else card.idm.replace(
                                            Regex(
                                                "."
                                            ), "*"
                                        ),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            val lastScanColor = if (card.lastScanTime != null) {
                                val currentTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                                val hoursDifference = (currentTime - card.lastScanTime) / (1000 * 60 * 60)
                                if (hoursDifference >= 72) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            }
                            Text(
                                text = if (card.lastScanTime != null) {
                                    "${stringResource(Res.string.lastScan)}: ${TimeUtils.getTimeAgoString(card.lastScanTime)}"
                                } else {
                                    "${stringResource(Res.string.lastScan)}: Never"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = lastScanColor
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View transactions",
                            tint = if (isRapidPassIdm(card.idm)) {
                                if (isSystemInDarkTheme()) DarkRapidPass else LightRapidPass
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
