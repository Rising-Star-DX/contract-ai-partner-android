package com.poscodx.contract_ai_partner.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideRemoteImage(
    url: String,
    modifier: Modifier = Modifier
) {
    GlideImage(
        model = url,
        contentDescription = "Remote Image",
        modifier = modifier.fillMaxWidth()
    )
}