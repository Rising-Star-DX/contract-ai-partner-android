package com.poscodx.contract_ai_partner.feature.standarddetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.poscodx.contract_ai_partner.domain.model.StandardDetail
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberHorizontalPdfReaderState
import com.rizzi.bouquet.rememberVerticalPdfReaderState

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
    val orientation = LocalConfiguration.current.orientation
    val verticalState  = rememberVerticalPdfReaderState(
        resource      = ResourceType.Remote(doc.url),
        isZoomEnable  = true
    )
    val horizontalState = rememberHorizontalPdfReaderState(
        resource      = ResourceType.Remote(doc.url),
        isZoomEnable  = true
    )

    Column(Modifier.fillMaxSize()) {

        if (doc.type.equals("PDF", true)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                HorizontalPDFReader(
                    state   = horizontalState,
                    modifier = Modifier.fillMaxSize()
                )
            else
                VerticalPDFReader(
                    state   = verticalState,
                    modifier = Modifier.fillMaxSize()
                )
        } else {
            /* Glide 이미지 뷰어 */
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