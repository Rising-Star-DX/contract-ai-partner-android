package com.poscodx.contract_ai_partner.core.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.renderAllPages(
    pdfUri: Uri
): List<ImageBitmap> = withContext(Dispatchers.IO) {

    val fd = contentResolver.openFileDescriptor(pdfUri, "r") ?: return@withContext emptyList()
    val renderer = PdfRenderer(fd)
    val list = buildList {
        repeat(renderer.pageCount) { idx ->
            renderer.openPage(idx).use { page ->
                val bmp = createBitmap(page.width, page.height)
                page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                add(bmp.asImageBitmap())
            }
        }
    }
    renderer.close()
    list
}

suspend fun Context.renderAllPagesFromUrl(
    pdfUrl: String
): List<ImageBitmap> {
    val localFile = downloadPdfToCache(pdfUrl)          // ⭐ 1단계
    val uri = Uri.fromFile(localFile)
    val bitmaps = renderAllPages(uri)                   // 기존 함수 재사용
    // 필요하다면 localFile.delete()
    return bitmaps
}

suspend fun PdfRenderer.Page.renderToBitmap(scale: Float): Bitmap {
    val targetW = (width  * scale).toInt()
    val targetH = (height * scale).toInt()
    val bmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
    render(bmp, null, null, RENDER_MODE_FOR_DISPLAY)
    return bmp
}