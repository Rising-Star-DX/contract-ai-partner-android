package com.poscodx.contract_ai_partner.ui.upload

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.poscodx.contract_ai_partner.R

@Composable
fun UploadBottomBar(onClickAdd: () -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = onClickAdd,
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add")
            },
            label = { Text("추가") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(R.color.primary),
                selectedTextColor = colorResource(R.color.primary),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
    }
}