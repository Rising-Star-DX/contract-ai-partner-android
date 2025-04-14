package com.poscodx.contract_ai_partner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poscodx.contract_ai_partner.ui.document.DocumentListScreen
//import com.poscodx.contract_ai_partner.ui.mypage.MyPageScreen
//import com.poscodx.contract_ai_partner.ui.upload.UploadScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.DocumentList.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.DocumentList.route) {
            DocumentListScreen()
        }
        composable(BottomNavItem.Upload.route) {
//            UploadScreen()
        }
        composable(BottomNavItem.MyPage.route) {
//            MyPageScreen()
        }
    }
}