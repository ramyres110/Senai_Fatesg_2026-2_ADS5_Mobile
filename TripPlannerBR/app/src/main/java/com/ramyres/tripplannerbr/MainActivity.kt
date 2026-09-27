package com.ramyres.tripplannerbr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ramyres.tripplannerbr.ui.screen.Navegacao
import com.ramyres.tripplannerbr.ui.theme.TripPlannerBRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripPlannerBRTheme {
                Navegacao()
            }
        }
    }
}