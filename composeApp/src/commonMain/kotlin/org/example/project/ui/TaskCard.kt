package org.example.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.project.domain.Task
import org.example.project.utils.AppColors
import org.example.project.utils.ImageDecoder
import org.example.project.utils.formatDate
import org.example.project.utils.formatDateWithYear
import org.example.project.utils.formatRelativeDate
import kotlin.io.encoding.Base64

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(20.dp),
        backgroundColor = AppColors.CardBackground,
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Columna izquierda
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = task.name,
                        color = AppColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    if (task.image != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Has image",
                            tint = AppColors.Primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                TaskTimeInfo(task.createdAt)
            }

            // Columna derecha
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp, top = 20.dp, bottom = 20.dp)
            ) {
                TaskActions(
                    completed = task.completed,
                    onTaskClick = { onTaskClick(task) }
                )
                if (!task.completed) {
                    PendingLabel()
                }
            }
        }
    }
}

@Composable
private fun TaskTimeInfo(createdAt: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Time icon",
            tint = AppColors.IconTint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = createdAt.formatRelativeDate(),
            color = AppColors.TextSecondary,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TaskActions(
    completed: Boolean,
    onTaskClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (completed) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = AppColors.Success,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        ActionButton(onClick = onTaskClick)
    }
}

@Composable
private fun ActionButton(onClick: () -> Unit) {

    IconButton(onClick = onClick) {
        Surface(
            shape = RoundedCornerShape(100.dp),
            color = AppColors.TextPrimary
        ) {
            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Action",
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun PendingLabel() {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = AppColors.CardBackground,
        border = BorderStroke(0.4.dp, AppColors.Border)
    ) {
        Text(
            text = "Pending",
            color = AppColors.TextPrimary,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp)
        )
    }
}
@Composable
fun TaskDetailDialog(
    task: Task,
    isOpen: Boolean,
    onClose: () -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onClose
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = AppColors.Background,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header con título y fecha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = task.name,
                            style = MaterialTheme.typography.h6,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = task.createdAt.formatDate(),
                            style = MaterialTheme.typography.caption,
                            color = AppColors.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado de la tarea
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Status: ",
                            style = MaterialTheme.typography.body2,
                            color = AppColors.TextSecondary
                        )
                        Text(
                            text = if (task.completed) "Completed" else "Pending",
                            color = if (task.completed) AppColors.Success else AppColors.TextPrimary,
                            style = MaterialTheme.typography.body1
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = "Description:",
                        style = MaterialTheme.typography.body2,
                        color = AppColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.body1,
                        color = AppColors.TextPrimary
                    )

                    // Imagen si existe
                    task.image?.let { imageData ->
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Attached Image:",
                            style = MaterialTheme.typography.body2,
                            color = AppColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val base64String = imageData.image.split(",").lastOrNull()

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            backgroundColor = AppColors.CardBackground
                        ) {
                            if (base64String != null) {
                                val imageBitmap = ImageDecoder.decodeBase64Image(base64String)
                                if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = "Task image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    ErrorImagePlaceholder()
                                }
                            } else {
                                ErrorImagePlaceholder()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de cerrar
                    Button(
                        onClick = onClose,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.Primary
                        )
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorImagePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
                tint = AppColors.Error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Error loading image",
                color = AppColors.Error
            )
        }
    }
}