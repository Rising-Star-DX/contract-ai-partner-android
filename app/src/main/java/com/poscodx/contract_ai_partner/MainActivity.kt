package com.poscodx.contract_ai_partner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.poscodx.contract_ai_partner.ui.navigation.MainScreen
import com.poscodx.contract_ai_partner.ui.theme.ContractaipartnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContractaipartnerTheme {
                MainScreen()
            }
        }
    }
}
