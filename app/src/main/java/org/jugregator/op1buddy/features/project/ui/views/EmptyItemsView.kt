package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyItemsView(modifier: Modifier = Modifier, picture: Painter, onSyncClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier,
                text = "It's empty here".uppercase(),
                style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.error)
            )
            Image(
                picture,
                contentDescription = null,
                modifier = Modifier.padding(top = 26.dp)
            )
            Text(
                "You can add items after",
                modifier = Modifier.padding(top = 40.dp),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xff868686)),
            )
            Button(
                modifier = Modifier.padding(top = 12.dp),
                onClick = onSyncClick, shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    "Sync with OP-1",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Black)
                )
            }
        }
    }
}