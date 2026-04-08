package com.baiqizzaaziza0105.moneycheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.baiqizzaaziza0105.moneycheck.navigation.SetupNavGraph
import com.baiqizzaaziza0105.moneycheck.ui.theme.MoneyCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyCheckTheme {
                SetupNavGraph()
            }
        }
    }
}

