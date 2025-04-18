package com.poscodx.contract_ai_partner.ui.document

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poscodx.contract_ai_partner.R

@Composable
fun DocumentListScreen() {
    // 문서 리스트 예시
    val documentList = listOf(
        DocumentItemUi(R.drawable.ic_pdf, "근로계약서.pdf", "인사", "2025.4.7 오후 5:51:33", "완료"),
        DocumentItemUi(R.drawable.ic_doc, "근로계약서.pdf", "인사", "2025.4.7 오후 5:51:33", "실패"),
        DocumentItemUi(R.drawable.ic_jpg, "근로계약서.pdf", "인사", "2025.4.7 오후 5:51:33", "완료"),
    )

    var searchQuery by remember { mutableStateOf("") }

    // 바텀시트 표시 상태
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    // --- 필터 상태(다중 선택) ---
    var selectedAIStatuses by remember { mutableStateOf(setOf<String>()) }
    var selectedExtensions by remember { mutableStateOf(setOf<String>()) }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    // 필터 활성화 여부
    val isFilterActive =
        selectedAIStatuses.isNotEmpty() || selectedExtensions.isNotEmpty() || selectedCategories.isNotEmpty()

    // 소트 활성화 여부
    var sortState by remember { mutableStateOf<SortState?>(null) }
    val isSortActive = (sortState != null)

    // 전체 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        // 상단 검색 / 정렬 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 검색창
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery -> searchQuery = newQuery },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 정렬 아이콘 버튼
            val sortIconTint = if (isSortActive) {
                colorResource(R.color.primary)
            } else {
                Color.Gray
            }
            IconButton(onClick = { showSortSheet = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = "Sort Icon",
                    tint = sortIconTint
                )
            }

            // 필터 아이콘 버튼
            val filterIconTint: Color = if (isFilterActive) {
                colorResource(R.color.primary)
            } else {
                Color.Gray
            }
            IconButton(onClick = { showFilterSheet = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = "Filter Icon",
                    tint = filterIconTint
                )
            }
        }

        // ============ 적용된 소트 & 필터 ============
        CombinedChipsRow(
            sortState = sortState,
            onRemoveSort = { sortState = null },
            aiStatuses = selectedAIStatuses,
            onRemoveAIStatus = { status -> selectedAIStatuses -= status },
            extensions = selectedExtensions,
            onRemoveExtension = { ext -> selectedExtensions -= ext },
            categories = selectedCategories,
            onRemoveCategory = { cat -> selectedCategories -= cat }
        )

        // 문서 목록
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(documentList) { doc ->
                DocumentListItem(doc)
            }
        }
    }

    // ============ BottomSheet (필터) ============
    if (showFilterSheet) {
        FilterBottomSheet(
            // 부모에서 관리 중인 필터 상태를 넘김
            selectedAIStatuses = selectedAIStatuses,
            onAIStatusesChange = { selectedAIStatuses = it },
            selectedExtensions = selectedExtensions,
            onExtensionsChange = { selectedExtensions = it },
            selectedCategories = selectedCategories,
            onCategoriesChange = { selectedCategories = it },

            onDismiss = { showFilterSheet = false },
            onFilterApplied = { showFilterSheet = false })
    }

    // ============ BottomSheet (정렬) ============
    if (showSortSheet) {
        SortBottomSheet(
            currentSort = sortState,
            onDismiss = { showSortSheet = false },
            onSortApplied = { newSort ->
                sortState = newSort
                showSortSheet = false
            })
    }
}

@Composable
fun SearchBar(
    searchQuery: String, onSearchQueryChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    BasicTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier
            .height(48.dp)
            .background(
                color = Color(0xFFF7F9FD), shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp),

        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                // 실제 텍스트 입력 부분
                innerTextField()
            }
        })
}

enum class SortKey { NAME, UPLOAD_DATE }
enum class SortDirection { ASC, DESC }

data class SortState(
    val key: SortKey,
    val direction: SortDirection
)

// 간단한 UI 데이터 모델
data class DocumentItemUi(
    @DrawableRes val fileTypeIcon: Int,
    val fileName: String,
    val department: String,
    val dateTime: String,
    val aiStatus: String
)

// Preview
@Preview(showBackground = true)
@Composable
fun DocumentListScreenPreview() {
    DocumentListScreen()
}
