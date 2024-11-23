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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jugregator.op1buddy.R
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

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.export_title),
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (isCopying) {
            LinearProgressIndicator(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f))
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(60.dp)
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

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(60.dp)
        ) {
            for (tapeIndex in 2..3) {
                TapeSelector(
                    index = tapeIndex,
                    enabled = state.backupInfo.tapes[tapeIndex].first.enabled,
                    selected = state.backupInfo.tapes[tapeIndex].second,
                    onSelected = { })
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 60.dp, alignment = Alignment.CenterHorizontally),
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

        Spacer(modifier = Modifier.height(40.dp))

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
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            ),
            enabled = isExportEnabled
        ) {
            Text(
                text = stringResource(R.string.export),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Black)
            )
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
