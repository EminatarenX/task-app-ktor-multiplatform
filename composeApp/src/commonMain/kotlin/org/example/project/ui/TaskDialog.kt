package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.example.project.domain.Task
import org.example.project.utils.AppColors


@Composable
fun TaskDialog(
    task: Task? = null,
    isOpen: Boolean,
    onClose: () -> Unit,
    onSave: (name: String, description: String, imageBytes: ByteArray?) -> Unit
) {
    if (isOpen) {
        var name by remember { mutableStateOf(task?.name ?: "") }
        var description by remember { mutableStateOf(task?.description ?: "") }
        var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
        var showError by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = onClose) {
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = AppColors.Background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = if (task == null) "Create Note" else "Edit Note",
                        style = MaterialTheme.typography.h6,
                        color = AppColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            showError = false
                        },
                        label = { Text("Title") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppColors.TextPrimary,
                            focusedBorderColor = AppColors.Primary,
                            unfocusedBorderColor = AppColors.Border,
                            backgroundColor = AppColors.CardBackground
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            showError = false
                        },
                        label = { Text("Description") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppColors.TextPrimary,
                            focusedBorderColor = AppColors.Primary,
                            unfocusedBorderColor = AppColors.Border,
                            backgroundColor = AppColors.CardBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (task == null) {
                        ImageSelector(
                            imageBytes = imageBytes,
                            onImageSelected = { bytes ->
                                imageBytes = bytes
                                showError = false
                            }
                        )

                        if (showError) {
                            Text(
                                text = if (imageBytes == null) "Please take a photo before saving" else "Please fill all fields",
                                color = AppColors.Error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onClose,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = AppColors.TextPrimary
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (name.isBlank() || imageBytes == null) {
                                    showError = true
                                } else {
                                    onSave(name, description, imageBytes)
                                    onClose()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColors.Primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}