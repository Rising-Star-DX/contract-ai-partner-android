package com.poscodx.contract_ai_partner.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.poscodx.contract_ai_partner.domain.model.IncorrectText

@Composable
fun PdfLazyViewerWithHighlight(
    pageBitmaps: List<ImageBitmap>,
    incorrects: List<IncorrectText>,
    modifier: Modifier = Modifier
) {
    var selectedMark by remember { mutableStateOf<IncorrectText?>(null) }

    Box(
        modifier = modifier
            .background(Color.White)
    ) {
        LazyColumn {
            itemsIndexed(pageBitmaps) { pageIndex, bitmap ->
                PageWithAnnotations(
                    bitmap = bitmap,
                    marks = incorrects.filter { it.currentPage == pageIndex + 1 },
                    onMarkClick = { selectedMark = it }
                )
            }
        }

        // ② 선택된 마크가 있으면 다이얼로그 띄우기
        selectedMark?.let { mark ->
            CorrectionDialog(
                mark = mark,
                onDismiss = { selectedMark = null }
            )
        }
    }
}

@Composable
private fun PageWithAnnotations(
    bitmap: ImageBitmap,
    marks: List<IncorrectText>,
    onMarkClick: (IncorrectText) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(bitmap.width.toFloat() / bitmap.height)
            // ➂ 터치 제스처를 전부 이 Box 에서 캐치
            .pointerInput(marks) {
                detectTapGestures { tapOffset ->
                    // Canvas 내 픽셀 크기
                    val w = size.width
                    val h = size.height

                    // 터치된 mark 찾기
                    val tapped = marks.firstOrNull { incorrect ->
                        (incorrect.positions + incorrect.positionParts).any { pos ->
                            // % → 픽셀 좌표
                            val x = (pos.left / 100f * w).toFloat()
                            val y = (pos.top / 100f * h).toFloat()
                            val pw = (pos.width / 100f * w).toFloat()
                            val ph = (pos.height / 100f * h).toFloat()
                            tapOffset.x in x..(x + pw) &&
                                    tapOffset.y in y..(y + ph)
                        }
                    }
                    tapped?.let(onMarkClick)
                }
            }
    ) {
        // ❶ PDF 페이지
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )

        // ❷ 하이라이트 & 언더라인
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height
            val stroke = 2.dp.toPx()

            marks.forEach { incorrect ->
                incorrect.positions.forEach { pos ->
                    val x = (pos.left / 100f * canvasW).toFloat()
                    val y = (pos.top / 100f * canvasH).toFloat()
                    val pw = (pos.width / 100f * canvasW).toFloat()
                    val ph = (pos.height / 100f * canvasH).toFloat()
                    drawLine(
                        color = Color.Red,
                        strokeWidth = stroke,
                        start = Offset(x, y + ph),
                        end = Offset(x + pw, y + ph)
                    )
                }
                incorrect.positionParts.forEach { part ->
                    val x = (part.left / 100f * canvasW).toFloat()
                    val y = (part.top / 100f * canvasH).toFloat()
                    val pw = (part.width / 100f * canvasW).toFloat()
                    val ph = (part.height / 100f * canvasH).toFloat()
                    drawRect(
                        color = Color.Yellow.copy(alpha = 0.3f),
                        topLeft = Offset(x, y),
                        size = Size(pw, ph)
                    )
                }
            }
        }
    }
}

@Composable
private fun CorrectionDialog(
    mark: IncorrectText,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Text(
                text = "AI 교정 결과",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(max = 300.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExpandableSection(label = "📍원문", content = mark.incorrectText)
                ExpandableSection(label = "🔍 검토 의견", content = mark.proofText)
                ExpandableSection(label = "✅ 제안 문구", content = mark.correctedText)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        }
    )
}

@Composable
private fun ExpandableSection(label: String, content: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(8.dp))
        if (expanded) {
            Text(
                text = content,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
            TextButton(onClick = { expanded = false }) {
                Text("접기")
            }
        } else {
            Text(
                text = content,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
            TextButton(onClick = { expanded = true }) {
                Text("더 보기")
            }
        }
    }
}