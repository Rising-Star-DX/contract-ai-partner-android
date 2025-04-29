package com.poscodx.contract_ai_partner.feature.standarddetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poscodx.contract_ai_partner.domain.model.StandardDetail
import com.poscodx.contract_ai_partner.ui.component.BouquetPdfViewer
import com.poscodx.contract_ai_partner.ui.component.GlideRemoteImage

@Composable
fun StandardDetailScreen(
    viewModel: StandardDetailViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is StandardDetailViewModel.UiState.Loading -> CenterLoading()
        is StandardDetailViewModel.UiState.Error -> CenterError()
        is StandardDetailViewModel.UiState.Success -> DetailContent(state.data)
    }
}

/* ────────────────── 본문 ────────────────── */
@Composable
private fun DetailContent(doc: StandardDetail) {
    Column(Modifier.fillMaxSize()) {

        if (doc.type.equals("PDF", true)) {
            BouquetPdfViewer(url = doc.url)
        } else {
            GlideRemoteImage(url = doc.url)
        }
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

@Composable
private fun UnsupportedViewer() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("미지원 형식입니다 🙏")
    }