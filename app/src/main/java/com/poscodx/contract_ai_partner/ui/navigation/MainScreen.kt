package com.poscodx.contract_ai_partner.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.poscodx.contract_ai_partner.R

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination =
        navBackStackEntry.value?.destination?.route ?: BottomNavItem.DocumentList.route

    // 상단 AppBar, 하단 BottomNavigation
    Scaffold(
        topBar = {
            TopBar(
                title = "전체",
                onMenuClick = { /* TODO: Drawer 열기 or 기타 이벤트 */ },
                onCategoryClick = { /* TODO: Dropdown 열기 or 기타 이벤트 */ }
            )
        },
        bottomBar = {
            BottomBar(navController = navController, currentRoute = currentDestination)
        }
    ) { innerPadding ->
        // NavHost가 메인 콘텐츠를 교체
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * 상단바(TopAppBar).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    // 이미지 첨부 예시에 따르면, 왼쪽 햄버거 버튼, 중앙 "전체", 우측에 ▼ 아이콘 정도가 있음
    TopAppBar(
        title = {
            // 가운데에 카테고리 텍스트
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    // 임의로 ic_menu라 가정
                    imageVector = Icons.Default.Menu,
                    contentDescription = "menu",
                    tint = colorResource(id = R.color.primary)
                )
            }
        },
        actions = {
            IconButton(onClick = onCategoryClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Category Dropdown",
                    tint = colorResource(id = R.color.primary)
                )
            }
        },
        // 대략적인 높이, 색상
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
    )
}

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = colorResource(R.color.primary)
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.primary),
                    selectedTextColor = colorResource(R.color.primary),
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

enum class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val iconRes: Int
) {
    DocumentList("document_list", "문서 조회", R.drawable.ic_menu),
    Upload("upload", "계약서 업로드", R.drawable.ic_upload),
    MyPage("mypage", "마이페이지", R.drawable.ic_user)
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}