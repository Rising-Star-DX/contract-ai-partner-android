package com.poscodx.contract_ai_partner.feature.contractdetail

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poscodx.contract_ai_partner.core.pdf.renderAllPagesFromUrl
import com.poscodx.contract_ai_partner.domain.model.IncorrectText
import com.poscodx.contract_ai_partner.ui.component.GlideRemoteImage
import com.poscodx.contract_ai_partner.ui.component.PdfLazyViewerWithHighlight

@Composable
fun ContractDetailScreen(
    vm: ContractDetailViewModel = hiltViewModel()
) {
    when (val state = vm.uiState) {
        is ContractDetailViewModel.UiState.Loading -> CenterLoading()
        is ContractDetailViewModel.UiState.Error -> CenterError()
        is ContractDetailViewModel.UiState.Success -> {
            val doc = state.data

            Log.d(TAG, "ContractDetailScreen: ${doc.url}")

            if (doc.type.equals("PDF", true)) {
                PdfDetailBody(
                    pdfUrl = doc.url,
                    marks  = doc.incorrectTexts
                )
            } else {
                GlideRemoteImage(url = doc.url, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun PdfDetailBody(
    pdfUrl: String,
    marks: List<IncorrectText>
) {
    val context = LocalContext.current
    val pageBitmaps by produceState<List<ImageBitmap>?>(initialValue = null, pdfUrl) {
        value = context.renderAllPagesFromUrl(pdfUrl)
    }

    if (pageBitmaps == null) {
        CenterLoading()
    } else {
        PdfLazyViewerWithHighlight(
            pageBitmaps = pageBitmaps!!,
            incorrects  = marks,
            modifier    = Modifier.fillMaxSize()
        )
    }
}

/* ────────── 공통 UI 헬퍼 ────────── */
@Composable
private fun CenterLoading() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

@Composable
private fun CenterError() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("문서를 불러오는 데 실패했어요 😢")
    }