package com.poscodx.contract_ai_partner.ui.component
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberHorizontalPdfReaderState
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@Composable
fun BouquetPdfViewer(
    url: String,
    modifier: Modifier = Modifier
) {
    val orientation = LocalConfiguration.current.orientation
    val stateVert = rememberVerticalPdfReaderState(ResourceType.Remote(url))
    val stateHorz = rememberHorizontalPdfReaderState(ResourceType.Remote(url))

    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        HorizontalPDFReader(state = stateHorz, modifier = modifier.fillMaxSize())
    else
        VerticalPDFReader(state = stateVert, modifier = modifier.fillMaxSize())
}
