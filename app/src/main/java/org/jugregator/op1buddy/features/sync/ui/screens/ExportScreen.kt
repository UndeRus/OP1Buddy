package org.jugregator.op1buddy.features.sync.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ExportScreen(isCopying: Boolean, onBackupDirSelected: (Uri) -> Unit, modifier: Modifier = Modifier) {
    val createBackupFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")) {
        uri -> uri?.let(onBackupDirSelected)
    }

    Column(modifier = modifier) {
        Text(text = "Export current backup", style = MaterialTheme.typography.displayLarge)
        if (isCopying) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Button(onClick = { createBackupFileLauncher.launch("/") }, enabled = !isCopying) {
            Text(text = "Export backup")
        }
    }
}

@Preview
@Composable
fun ExportScreenPreview() {
    AppTheme {
        ExportScreen(true, {})
    }
}