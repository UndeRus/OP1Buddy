package org.jugregator.op1buddy.features.sync

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import org.jugregator.op1buddy.features.sync.ui.screens.SyncScreen
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OP1SyncActivity : ComponentActivity() {

    private val viewModel: OP1SyncViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SyncScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
