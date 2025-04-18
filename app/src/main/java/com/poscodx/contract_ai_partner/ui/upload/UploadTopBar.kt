package com.poscodx.contract_ai_partner.ui.upload

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.poscodx.contract_ai_partner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadTopBar(onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("계약서 업로드") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Filled.ArrowBack, contentDescription = "back",
                    tint = colorResource(R.color.primary))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}