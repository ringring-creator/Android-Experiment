package com.ring.ring.todo.infra.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    isShowDatePicker: Boolean,
    datePickerState: DatePickerState,
    dismissDatePicker: () -> Unit,
    setDate: (Long) -> Unit,
) {
    if (isShowDatePicker) {
        DatePickerDialog(
            onDismissRequest = dismissDatePicker,
            confirmButton = {
                Text("Set", modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        datePickerState.selectedDateMillis?.let { setDate(it) }
                        dismissDatePicker()
                    })
            }
        ) {
            DatePicker(datePickerState)
        }
    }
}