package com.poscodx.contract_ai_partner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poscodx.contract_ai_partner.feature.contractdetail.ContractDetailScreen
import com.poscodx.contract_ai_partner.feature.contractlist.DocumentListScreen
import com.poscodx.contract_ai_partner.feature.standarddetail.StandardDetailScreen
import com.poscodx.contract_ai_partner.feature.standardlist.StandardListScreen
import com.poscodx.contract_ai_partner.ui.upload.UploadContractScreen


@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Contract.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Standard.route) {
            StandardListScreen(navController)
        }
        composable(
            "standardDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            StandardDetailScreen()
        }


        composable(BottomNavItem.Contract.route) {
            DocumentListScreen(navController)
        }
        composable(
            "agreementDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { ContractDetailScreen() }

        composable(BottomNavItem.MyPage.route) {
//            MyPageScreen()
        }
        composable("upload") { UploadContractScreen() }
    }
}