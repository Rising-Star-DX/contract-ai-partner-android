package com.poscodx.contract_ai_partner.ui.document

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.poscodx.contract_ai_partner.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CombinedChipsRow(
    sortState: SortState?,
    onRemoveSort: () -> Unit,
    aiStatuses: Set<String>,
    onRemoveAIStatus: (String) -> Unit,
    extensions: Set<String>,
    onRemoveExtension: (String) -> Unit,
    categories: Set<String>,
    onRemoveCategory: (String) -> Unit
) {
    // 칩들이 자동 줄바꿈되는 FlowRow
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)

    ) {

        // 1) 소트 칩 (항상 맨 앞에 한 개만)
        sortState?.let { state ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sortText = when (state.key) {
                    SortKey.NAME ->
                        if (state.direction == SortDirection.ASC) "이름(오름차)" else "이름(내림차)"

                    SortKey.UPLOAD_DATE ->
                        if (state.direction == SortDirection.ASC) "날짜(오름차)" else "날짜(내림차)"
                }

                InputChip(
                    onClick = { /* 필요 시 상세 보기 등 추가 */ },
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

                // 소트 칩 뒤 세로 디바이더
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color.LightGray)
                )
            }
        }

        // 2) 필터 칩들
        aiStatuses.forEach { status ->
            DeletableFilterChip(text = status) { onRemoveAIStatus(status) }
        }
        extensions.forEach { ext ->
            DeletableFilterChip(text = ext) { onRemoveExtension(ext) }
        }
        categories.forEach { cat ->
            DeletableFilterChip(text = cat) { onRemoveCategory(cat) }
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
