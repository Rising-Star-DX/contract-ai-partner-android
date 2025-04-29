package com.poscodx.contract_ai_partner.ui.upload

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UploadBottomBar(onClickAdd: () -> Unit) {
    NavigationBar(modifier = Modifier.padding(horizontal = 12.dp), containerColor = Color.White) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = onClickAdd,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("계약서 업로드")
        }
    }
}