package org.jugregator.op1buddy.features.sync.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.sync.RestoreScreenState
import org.jugregator.op1buddy.features.sync.isEmpty
import org.jugregator.op1buddy.features.sync.ui.views.DrumSelector
import org.jugregator.op1buddy.features.sync.ui.views.SynthSelector
import org.jugregator.op1buddy.features.sync.ui.views.TapeSelector
import org.jugregator.op1buddy.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExportScreen(
    state: RestoreScreenState,
    isCopying: Boolean,
    onBackupDirSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val createBackupFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        uri?.let(onBackupDirSelected)
    }

    Column(modifier = modifier.padding(8.dp)) {
        Column(modifier = modifier) {
            Text(
                text = "Export current backup",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            if (isCopying) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (tapeIndex in 0..1) {
                    TapeSelector(
                        index = tapeIndex,
                        enabled = state.backupInfo.tapes[tapeIndex].first.enabled,
                        selected = state.backupInfo.tapes[tapeIndex].second,
                        onSelected = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (tapeIndex in 2..3) {
                    TapeSelector(
                        index = tapeIndex,
                        enabled = state.backupInfo.tapes[tapeIndex].first.enabled,
                        selected = state.backupInfo.tapes[tapeIndex].second,
                        onSelected = { })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                SynthSelector(
                    selected = state.backupInfo.synths,
                    enabled = state.backupInfo.synthsEnabled,
                    onSelected = { })

                DrumSelector(
                    selected = state.backupInfo.drumkits,
                    enabled = state.backupInfo.drumkitsEnabled,
                    onSelected = { })
            }

            Spacer(modifier = Modifier.weight(1.0f))

            val isExportEnabled by remember {
                derivedStateOf {
                    !isCopying && !state.backupInfo.isEmpty()
                }
            }

            Button(
                onClick = {
                    val dateFormated = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
                    createBackupFileLauncher.launch(
                        String.format(
                            Locale.US,
                            "%s.zip",
                            dateFormated
                        )
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = isExportEnabled
            ) {
                Text(text = "Export", style = MaterialTheme.typography.displayLarge)
            }
        }
    }
}

@Preview
@Composable
fun ExportScreenPreview() {
    AppTheme {
        ExportScreen(RestoreScreenState(), true, {})
    }
}
