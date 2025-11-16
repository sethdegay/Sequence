package dev.sethdegay.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.sethdegay.routines.core.navigation.RoutinesNavDisplay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialExpressiveTheme {
                RoutinesNavDisplay()
            }
        }
    }
}