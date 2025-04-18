package com.poscodx.contract_ai_partner.ui.document

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    selectedAIStatuses: Set<String>, onAIStatusesChange: (Set<String>) -> Unit,

    selectedExtensions: Set<String>, onExtensionsChange: (Set<String>) -> Unit,

    selectedCategories: Set<String>, onCategoriesChange: (Set<String>) -> Unit,

    onDismiss: () -> Unit, onFilterApplied: () -> Unit
) {
    // AI 상태, 파일 확장자, 카테고리 리스트
    val aiStatusList = listOf("AI 분석 완료", "AI 분석 중", "AI 분석 실패")
    val extensionList = listOf("PDF", "DOC", "XLS", "JPG", "PNG", "TXT")
    val categoryList = listOf("R&D", "가맹사업법", "공사", "구매", "법령", "인사")

    // 토글 함수
    fun toggleItem(
        item: String, currentSet: Set<String>, onUpdate: (Set<String>) -> Unit
    ) {
        if (currentSet.contains(item)) {
            onUpdate(currentSet - item)
        } else {
            onUpdate(currentSet + item)
        }
    }

    // 바텀 시트
    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()
    ) {
        Text(
            text = "필터 설정",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )

        // AI 상태
        MultiSelectRow(
            label = "AI 상태",
            options = aiStatusList,
            selectedItems = selectedAIStatuses,
            onToggleItem = { item -> toggleItem(item, selectedAIStatuses, onAIStatusesChange) })

        // 파일 확장자
        MultiSelectRow(
            label = "파일 확장자",
            options = extensionList,
            selectedItems = selectedExtensions,
            onToggleItem = { item -> toggleItem(item, selectedExtensions, onExtensionsChange) })

        // 카테고리
        MultiSelectRow(
            label = "카테고리",
            options = categoryList,
            selectedItems = selectedCategories,
            onToggleItem = { item -> toggleItem(item, selectedCategories, onCategoriesChange) })

        // 적용 버튼
        OutlinedButton(
            onClick = onFilterApplied,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(text = "적용하기", color = MaterialTheme.colorScheme.primary)
        }
    }
}

// -------------------------
// 공용 MultiSelectRow
// -------------------------
@Composable
fun MultiSelectRow(
    label: String, options: List<String>, selectedItems: Set<String>, onToggleItem: (String) -> Unit
) {
    // 소제목
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    // 가로 스크롤이 가능한 Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        options.forEach { item ->
            val isSelected = item in selectedItems

            // OutlinedButton 색상 변경 (선택 시 Primary, 아니면 회색/투명)
            OutlinedButton(
                onClick = { onToggleItem(item) }, border = BorderStroke(
                    1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                ), colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isSelected) Color.White else Color.Black
                ), modifier = Modifier
                    .padding(end = 8.dp)
                    .height(40.dp)
            ) {
                Text(text = item, fontSize = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.size(8.dp)) // 구분을 위한 여백
}