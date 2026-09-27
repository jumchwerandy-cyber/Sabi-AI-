package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ConversationEntity
import com.example.data.local.FeedbackEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminScreen(
    conversations: List<ConversationEntity>,
    feedbackList: List<FeedbackEntity>,
    onMarkReviewed: (String) -> Unit
) {
    val totalConversations = conversations.size
    val totalFeedback = feedbackList.size
    val negativeFeedback = feedbackList.filter { !it.category.equals("Helpful", ignoreCase = true) }
    val satisfactionRate = if (totalFeedback > 0) {
        val helpfulCount = feedbackList.count { it.category.equals("Helpful", ignoreCase = true) }
        ((helpfulCount.toFloat() / totalFeedback) * 100).toInt()
    } else 94

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .padding(16.dp)
            .testTag("admin_screen_column"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SabiGreenGlow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin & Training Desk",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SabiTextPrimary
                        )
                    }
                    Text(
                        text = "Real-time metrics, feedback loop, and fine-tuning review",
                        style = MaterialTheme.typography.bodySmall,
                        color = SabiTextSecondary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SabiGreenContainer
                ) {
                    Text(
                        text = "Authorized Admin",
                        style = MaterialTheme.typography.labelSmall,
                        color = SabiGreenGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminMetricCard(
                        title = "TOTAL USERS",
                        value = "1,420",
                        subtitle = "Registered accounts",
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "DAILY ACTIVE (DAU)",
                        value = "864",
                        subtitle = "+18% from last week",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminMetricCard(
                        title = "CONVERSATIONS",
                        value = "$totalConversations",
                        subtitle = "Active sessions",
                        icon = Icons.AutoMirrored.Filled.Chat,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "SATISFACTION",
                        value = "$satisfactionRate%",
                        subtitle = "$totalFeedback total reviews",
                        icon = Icons.Default.ThumbUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Language & Model Usage Stats
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Language & Code-Switching Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    UsageBar(label = "Nigerian Pidgin", percentage = 54, color = SabiGreenPrimary)
                    UsageBar(label = "Standard English", percentage = 28, color = SabiBlue)
                    UsageBar(label = "English / Pidgin Code-Switching", percentage = 14, color = SabiGold)
                    UsageBar(label = "Igbo/Yoruba/Hausa Queries", percentage = 4, color = SabiPurple)
                }
            }
        }

        // Model Performance Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Model Benchmarks & Infrastructure",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BenchmarkStat("Primary LLM", "Gemini 3.5 Flash")
                        BenchmarkStat("Avg Latency", "620 ms")
                        BenchmarkStat("Context Engine", "RAG Vector v1.2")
                    }
                }
            }
        }

        // Reported Responses Desk
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DATASET FEEDBACK & USER REPORTS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SabiGreenGlow
                )
                Text(
                    text = "${negativeFeedback.size} reports",
                    style = MaterialTheme.typography.labelSmall,
                    color = SabiTextMuted
                )
            }
        }

        if (negativeFeedback.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SabiNavySurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No user reports pending review. SABI context accuracy is optimal.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SabiTextSecondary
                        )
                    }
                }
            }
        } else {
            items(negativeFeedback, key = { it.id }) { report ->
                FeedbackReviewCard(
                    report = report,
                    onApprove = { onMarkReviewed(report.id) }
                )
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SabiTextMuted
                )
                Icon(icon, contentDescription = null, tint = SabiGreenGlow, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SabiTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = SabiTextSecondary
            )
        }
    }
}

@Composable
private fun UsageBar(label: String, percentage: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = SabiTextPrimary)
            Text(text = "$percentage%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = color,
            trackColor = SabiNavyElevated
        )
    }
}

@Composable
private fun BenchmarkStat(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = SabiTextMuted)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SabiGreenGlow)
    }
}

@Composable
private fun FeedbackReviewCard(
    report: FeedbackEntity,
    onApprove: () -> Unit
) {
    val date = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(report.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (report.reviewed) SabiNavyBorder else SabiError.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (report.reviewed) SabiNavyElevated else Color(0xFF451A03)
                ) {
                    Text(
                        text = report.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (report.reviewed) SabiTextSecondary else SabiGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(text = date, style = MaterialTheme.typography.labelSmall, color = SabiTextMuted)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (report.comment.isNotBlank()) {
                Text(
                    text = "User explanation: \"${report.comment}\"",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SabiTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(
                text = "Assistant Response: ${report.aiResponse.take(120)}...",
                style = MaterialTheme.typography.bodySmall,
                color = SabiTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!report.reviewed) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = SabiGreenContainer),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("approve_report_${report.id.take(6)}")
                    ) {
                        Text("Add to Fine-Tuning Dataset", color = SabiGreenGlow, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("✓ Added to Dataset", color = SabiGreenGlow, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
