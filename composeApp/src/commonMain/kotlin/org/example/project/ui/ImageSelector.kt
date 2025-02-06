package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.unit.dp
import org.example.project.utils.AppColors

//@Composable
//fun ImageSelector(
//    imageBase64: ByteArray?,
//    onImageSelected: (ByteArray?) -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (imageBase64 != null) {
//            // Mostrar preview de la imagen seleccionada
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .background(AppColors.CardBackground, RoundedCornerShape(8.dp))
//                    .padding(8.dp)
//            ) {
//                // Aquí iría la imagen en base64
//                // Por ahora mostraremos un placeholder
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(AppColors.Border)
//                )
//
//                // Botón para eliminar la imagen
//                IconButton(
//                    onClick = { onImageSelected(null) },
//                    modifier = Modifier.align(Alignment.TopEnd)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Close,
//                        contentDescription = "Remove image",
//                        tint = AppColors.Error
//                    )
//                }
//            }
//        } else {
//            // Botón para seleccionar imagen
//            Button(
//                onClick = {
//                    // Aquí se implementará la selección de imagen
//                    // Por ahora simularemos con un string base64
//                    onImageSelected(ByteArray(0))
//                },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = AppColors.CardBackground
//                ),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.PhotoCamera,
//                        contentDescription = "Add photo",
//                        tint = AppColors.TextPrimary
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Add Photo",
//                        color = AppColors.TextPrimary
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun ImageSelector(
//    hasImage: Boolean,
//    onToggleImage: () -> Unit
//) {
//    Button(
//        onClick = onToggleImage,
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = if (hasImage) AppColors.Primary else AppColors.CardBackground
//        ),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Image,
//                contentDescription = if (hasImage) "Remove image" else "Add image",
//                tint = if (hasImage) Color.White else AppColors.TextPrimary
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = if (hasImage) "Remove Image" else "Add Image",
//                color = if (hasImage) Color.White else AppColors.TextPrimary
//            )
//        }
//    }
@Composable
fun ImageSelector(
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageBytes != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(AppColors.CardBackground, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                // Placeholder for image preview
                Text(
                    text = "Image captured",
                    color = AppColors.TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { onImageSelected(null) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove image",
                        tint = AppColors.Error
                    )
                }
            }
        } else {
            Button(
                onClick = { PlatformCameraLauncher.takePicture(onImageSelected) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColors.CardBackground
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Take photo",
                        tint = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Take Photo",
                        color = AppColors.TextPrimary
                    )
                }
            }
        }
    }
}

expect object PlatformCameraLauncher {
    fun takePicture(onImageCaptured: (ByteArray) -> Unit)
}