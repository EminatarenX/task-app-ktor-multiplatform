package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.example.project.domain.Task
import org.example.project.utils.AppColors

@Composable
fun TaskDialog(
    task: Task? = null,
    isOpen: Boolean,
    onClose: () -> Unit,
    onSave: (String, String) -> Unit
) {
    if (isOpen) {
        var name by remember { mutableStateOf(task?.name ?: "") }
        var description by remember { mutableStateOf(task?.description ?: "") }

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
                        text = if (task == null) "Create Note" else "Note Details",
                        style = MaterialTheme.typography.h6,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Title") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppColors.TextPrimary,
                            focusedBorderColor = AppColors.Primary,
                            unfocusedBorderColor = AppColors.Border
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppColors.TextPrimary,
                            focusedBorderColor = AppColors.Primary,
                            unfocusedBorderColor = AppColors.Border
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClose) {
                            Text("Cancel", color = AppColors.TextPrimary)
                        }
                        Button(
                            onClick = {
                                onSave(name, description)
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColors.Primary
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