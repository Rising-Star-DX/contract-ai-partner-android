package com.poscodx.contract_ai_partner.ui.document

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        }

        // ------------------------
        // 적용된 필터 Chip Row
        // ------------------------
        FilterChipsRow(
            aiStatuses = selectedAIStatuses,
            extensions = selectedExtensions,
            categories = selectedCategories,

            onRemoveAIStatus = { status -> selectedAIStatuses = selectedAIStatuses - status },
            onRemoveExtension = { ext -> selectedExtensions = selectedExtensions - ext },
            onRemoveCategory = { cat -> selectedCategories = selectedCategories - cat })

        SortChipRow(
            sortState = sortState,
            onRemoveSort = { sortState = null }
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

// ------------------------
// Chip Row: 적용된 필터 표시
// ------------------------
@Composable
fun FilterChipsRow(
    aiStatuses: Set<String>,
    extensions: Set<String>,
    categories: Set<String>,
    onRemoveAIStatus: (String) -> Unit,
    onRemoveExtension: (String) -> Unit,
    onRemoveCategory: (String) -> Unit
) {
    // 한 줄로 다 들어가면 좋겠지만, 만약 많다면 수평 스크롤을 넣거나 FlowRow를 사용할 수도 있음
    // 예시는 수평 스크롤 형태
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // AI 상태
        aiStatuses.forEach { status ->
            DeletableFilterChip(
                text = status, onDelete = { onRemoveAIStatus(status) })
        }
        // 확장자
        extensions.forEach { ext ->
            DeletableFilterChip(
                text = ext, onDelete = { onRemoveExtension(ext) })
        }
        // 카테고리
        categories.forEach { cat ->
            DeletableFilterChip(
                text = cat, onDelete = { onRemoveCategory(cat) })
        }
    }
}

// ------------------------
// 개별 Chip
// ------------------------
@Composable
fun DeletableFilterChip(
    text: String, onDelete: () -> Unit
) {
    InputChip(
        modifier = Modifier.padding(end = 8.dp),
        // 칩 전체를 클릭했을 때의 동작
        onClick = {
            onDelete()
        },
        // 칩에 표시될 라벨
        label = { Text(text) }, selected = true,
        // trailingIcon을 달아 칩 끝에 닫기 아이콘을 표시
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Chip",
                modifier = Modifier.size(InputChipDefaults.AvatarSize)
            )
        }, colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = colorResource(R.color.primary),
            selectedLabelColor = colorResource(R.color.white),
            selectedTrailingIconColor = colorResource(R.color.white)
        )
    )
}

// ------------------------
// sort Chip 상태 표시 열
// ------------------------
@Composable
fun SortChipRow(
    sortState: SortState?,
    onRemoveSort: () -> Unit
) {
    // sortState가 null이면 칩 표시 안 함
    if (sortState == null) return

    val sortText = when (sortState.key) {
        SortKey.NAME -> {
            if (sortState.direction == SortDirection.ASC) "이름(오름차)" else "이름(내림차)"
        }

        SortKey.UPLOAD_DATE -> {
            if (sortState.direction == SortDirection.ASC) "날짜(오름차)" else "날짜(내림차)"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        InputChip(
            onClick = { /* 칩 전체 클릭 시 다른 동작? */ },
            label = { Text(sortText) },
            selected = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Sort",
                    modifier = Modifier
                        .size(InputChipDefaults.AvatarSize)
                        .clickable { onRemoveSort() }
                )
            },
            colors = InputChipDefaults.inputChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White,
                selectedTrailingIconColor = Color.White
            )
        )
    }
}

@Composable
fun DocumentListItem(document: DocumentItemUi) {
    // 완료=파란색(#2962FF 근사값), 실패=빨간색(#FF5252 근사값)
    val statusColor =
        if (document.aiStatus == "완료") colorResource(R.color.primary) else Color(0xFFCD6155)
    val statusText = if (document.aiStatus == "완료") "AI 분석 완료" else "AI 분석 실패"

    // 아이템 배경색(이미지 보면 왼쪽만 약간 파란 배경)
    // 여기서는 Row 전체를 Card로 감싸고, 왼쪽 부분만 별도 Box 배경
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)), // 약간의 라이트 블루 느낌
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFE8F2FF)) // 왼쪽 파란 배경
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(document.fileTypeIcon),
                    contentDescription = "Document Thumbnail",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
            ) {
                Text(
                    text = document.fileName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF333333)
                )
                Text(
                    text = document.department, fontSize = 12.sp, color = Color(0xFF888888)
                )
                Text(
                    text = document.dateTime, fontSize = 12.sp, color = Color(0xFF888888)
                )
            }

            // 상태 뱃지
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50)) // 모서리를 조금 둥글게 하고 싶다면 corner
                    .background(statusColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText, color = Color.White, fontSize = 12.sp
                )
            }
        }
    }
}

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

enum class SortKey { NAME, UPLOAD_DATE }
enum class SortDirection { ASC, DESC }

data class SortState(
    val key: SortKey,
    val direction: SortDirection
)

// ==========================
// 정렬 적용을 위한 바텀시트 예시
// ==========================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: SortState?, // 현재 정렬 상태 (null 가능)
    onDismiss: () -> Unit,
    onSortApplied: (SortState) -> Unit
) {
    // 라디오 버튼에서 사용할 상태
    var selectedSortKey by remember {
        mutableStateOf(currentSort?.key ?: SortKey.NAME)
    }
    var selectedSortDir by remember {
        mutableStateOf(currentSort?.direction ?: SortDirection.ASC)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()
    ) {
        Text(text = "정렬 옵션을 선택하세요", modifier = Modifier.padding(16.dp))

        // 정렬 기준 (이름 or 업로드일)
        Text(
            text = "정렬 기준",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            RadioButtonWithLabel(
                text = "이름",
                selected = (selectedSortKey == SortKey.NAME),
                onClick = { selectedSortKey = SortKey.NAME }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButtonWithLabel(
                text = "업로드일",
                selected = (selectedSortKey == SortKey.UPLOAD_DATE),
                onClick = { selectedSortKey = SortKey.UPLOAD_DATE }
            )
        }

        // 정렬 방향 (오름차 or 내림차)
        Text(
            text = "정렬 방향",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            RadioButtonWithLabel(
                text = "오름차",
                selected = (selectedSortDir == SortDirection.ASC),
                onClick = { selectedSortDir = SortDirection.ASC }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButtonWithLabel(
                text = "내림차",
                selected = (selectedSortDir == SortDirection.DESC),
                onClick = { selectedSortDir = SortDirection.DESC }
            )
        }

        // 적용하기 버튼
        OutlinedButton(
            onClick = {
                onSortApplied(SortState(selectedSortKey, selectedSortDir))
            },
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

@Composable
fun RadioButtonWithLabel(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.material3.RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

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
