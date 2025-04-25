package com.poscodx.contract_ai_partner.feature.standardlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.poscodx.contract_ai_partner.R
import com.poscodx.contract_ai_partner.data.mapper.toUi
import com.poscodx.contract_ai_partner.feature.contractlist.SearchBar
import com.poscodx.contract_ai_partner.feature.contractlist.SortState
import com.poscodx.contract_ai_partner.feature.contractlist.component.CombinedChipsRow
import com.poscodx.contract_ai_partner.feature.contractlist.component.DocumentListItem
import com.poscodx.contract_ai_partner.feature.contractlist.component.FilterBottomSheet
import com.poscodx.contract_ai_partner.feature.contractlist.component.SortBottomSheet

@Composable
fun StandardListScreen(navController: NavHostController) {
    // ───── 검색 & 바텀시트 상태 ──────────────────────────────────────────────
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    // ───── 필터링 상태 ──────────────────────────────────────────────────────
    var selectedAIStatuses by remember { mutableStateOf(setOf<String>()) }
    var selectedExtensions by remember { mutableStateOf(setOf<String>()) }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    val isFilterActive =
        selectedAIStatuses.isNotEmpty() || selectedExtensions.isNotEmpty() || selectedCategories.isNotEmpty()

    // ───── 정렬 상태 ──────────────────────────────────────────────────────
    var sortState by remember { mutableStateOf<SortState?>(null) }
    val isSortActive = sortState != null

    // ───── ViewModel & UIState ────────────────────────────────────────────
    val viewModel: StandardListViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        when (uiState) {
            is StandardListViewModel.UiState.Success ->
                Log.d("StandardListScreen", "API Success: ${uiState.data.size} items")

            is StandardListViewModel.UiState.Error ->
                Log.e("StandardListScreen", "API Error", uiState.throwable)

            else -> Unit
        }
    }

    // ───── 화면 루트 ───────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /* ───── 상단 검색 + 정렬/필터 버튼 ───── */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = { showSortSheet = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = "Sort Icon",
                    tint = if (isSortActive) colorResource(R.color.primary) else Color.Gray
                )
            }

            IconButton(onClick = { showFilterSheet = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = "Filter Icon",
                    tint = if (isFilterActive) colorResource(R.color.primary) else Color.Gray
                )
            }
        }

        /* ───── 적용된 소트 & 필터 칩 ───── */
        CombinedChipsRow(
            sortState = sortState,
            onRemoveSort = { sortState = null },
            aiStatuses = selectedAIStatuses,
            onRemoveAIStatus = { selectedAIStatuses -= it },
            extensions = selectedExtensions,
            onRemoveExtension = { selectedExtensions -= it },
            categories = selectedCategories,
            onRemoveCategory = { selectedCategories -= it }
        )

        /* ───── 문서 리스트 데이터 ───── */
        val documentList = when (val state = viewModel.uiState) {
            is StandardListViewModel.UiState.Success -> state.data.map { it.toUi() }
            else -> emptyList()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(documentList) { doc ->
                DocumentListItem(
                    document = doc,
                    onClick = { clickedId ->
                        navController.navigate("standardDetail/$clickedId")
                    }
                )
            }
        }
    }

    /* ───── 필터 BottomSheet ───── */
    if (showFilterSheet) {
        FilterBottomSheet(
            selectedAIStatuses = selectedAIStatuses,
            onAIStatusesChange = { selectedAIStatuses = it },
            selectedExtensions = selectedExtensions,
            onExtensionsChange = { selectedExtensions = it },
            selectedCategories = selectedCategories,
            onCategoriesChange = { selectedCategories = it },
            onDismiss = { showFilterSheet = false },
            onFilterApplied = { showFilterSheet = false }
        )
    }

    /* ───── 정렬 BottomSheet ───── */
    if (showSortSheet) {
        SortBottomSheet(
            currentSort = sortState,
            onDismiss = { showSortSheet = false },
            onSortApplied = { sortState = it; showSortSheet = false }
        )
    }
}
