package com.poscodx.contract_ai_partner.ui.document

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    var sortExpanded by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf("Sort") }

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
            // 1) 검색창
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery -> searchQuery = newQuery },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 2) 정렬 버튼
            SortButton(
                sortType = sortType,
                onSortTypeChange = { newSort -> sortType = newSort },
                expanded = sortExpanded,
                onExpandedChange = { sortExpanded = it }
            )
        }

        // 문서 목록
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(documentList) { doc ->
                DocumentListItem(doc)
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier
            .height(48.dp)
            .background(
                color = Color(0xFFF7F9FD),
                shape = RoundedCornerShape(50)
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
        }
    )
}

@Composable
fun SortButton(
    sortType: String,
    onSortTypeChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { onExpandedChange(true) },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(0.5.dp, Color.LightGray),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Sort",
                    tint = Color.LightGray,
                )
                Spacer(Modifier.width(4.dp))
                Text(sortType, fontSize = 14.sp, color = Color.Black)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            DropdownMenuItem(
                text = { Text("최신순") },
                onClick = {
                    onSortTypeChange("최신순")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = { Text("이름순") },
                onClick = {
                    onSortTypeChange("이름순")
                    onExpandedChange(false)
                }
            )
        }
    }
}


@Composable
fun DocumentListItem(document: DocumentItemUi) {
    // 완료=파란색(#2962FF 근사값), 실패=빨간색(#FF5252 근사값)
    val statusColor = if (document.aiStatus == "완료") Color(0xFF2962FF) else Color(0xFFFF5252)
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
                    .padding(8.dp),
                contentAlignment = Alignment.Center
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
                    text = document.department,
                    fontSize = 12.sp,
                    color = Color(0xFF888888)
                )
                Text(
                    text = document.dateTime,
                    fontSize = 12.sp,
                    color = Color(0xFF888888)
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
                    text = statusText,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
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
