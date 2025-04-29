package com.poscodx.contract_ai_partner.core.pdf

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

suspend fun Context.downloadPdfToCache(url: String): File = withContext(Dispatchers.IO) {
    val fileName = "tmp_${System.currentTimeMillis()}.pdf"
    val file = File(cacheDir, fileName)
    URL(url).openStream().use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    file
}