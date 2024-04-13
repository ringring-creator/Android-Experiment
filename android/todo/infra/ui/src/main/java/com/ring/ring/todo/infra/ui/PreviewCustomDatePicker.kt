package com.ring.ring.todo.infra.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
fun PreviewCustomDatePicker() {
    CustomDatePicker(
        isShowDatePicker = true,
        datePickerState = rememberDatePickerState(),
        dismissDatePicker = {},
        setDate = {},
    )
}