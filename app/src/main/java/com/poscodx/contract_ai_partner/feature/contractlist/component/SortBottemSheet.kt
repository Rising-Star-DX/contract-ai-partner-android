package com.poscodx.contract_ai_partner.feature.contractlist.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poscodx.contract_ai_partner.feature.contractlist.SortDirection
import com.poscodx.contract_ai_partner.feature.contractlist.SortKey
import com.poscodx.contract_ai_partner.feature.contractlist.SortState

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