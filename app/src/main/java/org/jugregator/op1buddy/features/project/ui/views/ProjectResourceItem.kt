package org.jugregator.op1buddy.features.project.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.drumkit.DrumkitType
import org.jugregator.op1buddy.data.synth.SynthEngine
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.sync.TapeSelector
import org.jugregator.op1buddy.ui.icons.synth.Cluster
import org.jugregator.op1buddy.ui.icons.synth.Digital
import org.jugregator.op1buddy.ui.icons.synth.Dna
import org.jugregator.op1buddy.ui.icons.synth.Drwave
import org.jugregator.op1buddy.ui.icons.synth.Dsynth
import org.jugregator.op1buddy.ui.icons.synth.Fm
import org.jugregator.op1buddy.ui.icons.synth.Phase
import org.jugregator.op1buddy.ui.icons.synth.Pulse
import org.jugregator.op1buddy.ui.icons.synth.Sampler
import org.jugregator.op1buddy.ui.icons.synth.String
import org.jugregator.op1buddy.ui.icons.synth.Synth
import org.jugregator.op1buddy.ui.icons.synth.Voltage
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ProjectResourceItem(type: ProjectResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        when (type) {
            is ProjectResource.Drumkit -> DrumkitResourceItem(drumkit = type, onClick = onClick)
            is ProjectResource.Synth -> SynthResourceItem(synth = type)
            is ProjectResource.Tape -> TapePlayer(
                onPlayClick = onClick,
                onStopClick = {}
            )
        }
    }
}

@Composable
fun DrumkitResourceItem(modifier: Modifier = Modifier, drumkit: ProjectResource.Drumkit, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
        ) {
            SynthIcon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                engine = SynthEngine.DSynth(),
            )
        }
        Text(
            modifier = modifier
                .padding(12.dp)
                .weight(1f),
            text = drumkit.name
        )
    }
}

@Composable
fun SynthResourceItem(modifier: Modifier = Modifier, synth: ProjectResource.Synth) {
    Row(
        modifier = modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
        ) {
            SynthIcon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                engine = synth.engine,
            )
        }
        Text(
            modifier = modifier
                .padding(12.dp)
                .weight(1f),
            text = synth.name
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun SynthResourceItemPreview() {
    AppTheme {
        SynthResourceItem(synth = ProjectResource.Synth(0, "filename", "iter", SynthEngine.Iter()))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun DrumkitResourceItemPreview() {
    AppTheme {
        DrumkitResourceItem(drumkit = ProjectResource.Drumkit(0, "filename", "iter", DrumkitType.Sample)) {}
    }
}

@Composable
fun SynthIcon(modifier: Modifier = Modifier, engine: SynthEngine) {
    val icon by remember {
        mutableStateOf(
            when (engine) {
                is SynthEngine.Cluster -> Icons.Synth.Cluster
                is SynthEngine.DNA -> Icons.Synth.Dna
                is SynthEngine.DSynth -> Icons.Synth.Dsynth
                is SynthEngine.Digital -> Icons.Synth.Digital
                is SynthEngine.DrWave -> Icons.Synth.Drwave
                is SynthEngine.FM -> Icons.Synth.Fm
                is SynthEngine.Iter -> Icons.Synth.Cluster
                is SynthEngine.Phase -> Icons.Synth.Phase
                is SynthEngine.Pulse -> Icons.Synth.Pulse
                is SynthEngine.Sampler -> Icons.Synth.Sampler
                is SynthEngine.String -> Icons.Synth.String
                is SynthEngine.Voltage -> Icons.Synth.Voltage
                is SynthEngine.Undefined -> Icons.Synth.Synth
            }
        )
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun TapeIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = rememberVectorPainter(Icons.Sync.TapeSelector),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun TapeResourceItem(modifier: Modifier = Modifier, tape: ProjectResource.Tape, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
        ) {
            TapeIcon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
            )
        }
        Text(
            modifier = modifier
                .padding(12.dp)
                .weight(1f),
            text = "Tape ${tape.index + 1}"
        )
    }
}

@Preview
@Composable
fun TapeResourceItemPreview() {
    MaterialTheme {
        TapeResourceItem(tape = ProjectResource.Tape(0, "asdasd")) {}
    }
}
