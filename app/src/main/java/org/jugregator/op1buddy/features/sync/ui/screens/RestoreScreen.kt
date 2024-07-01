package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.sync.TAPES_COUNT
import org.jugregator.op1buddy.features.sync.ui.views.TapeRow

@Composable
fun RestoreScreenTodo(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "In construction...",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun RestoreScreen() {
    Column {

        Text(
            text = "Choose what to restore",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
        for (tapeIndex in 1..TAPES_COUNT) {
            TapeRow(tapeIndex, false, {})
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "Synths")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "Drumkits")
        }
        Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer

        val isLoading by remember {
            mutableStateOf(true)
        }

        if (isLoading) {
            Text(
                text = "Don't close your app it can broke your filesystem",
                style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.error)
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading) {
            Text(text = "Restore")
        }
    }
}
