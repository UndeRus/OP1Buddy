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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.synth.SynthEngine
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ProjectResourceItem(type: ProjectResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        when (type) {
            is ProjectResource.Drumkit -> DrumkitResourceItem(drumkit = type, onClick = onClick)
            is ProjectResource.Synth -> SynthResourceItem(synth = type)
            is ProjectResource.Tape -> TapeResourceItem(tape = type) { }
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
        DrumkitResourceItem(drumkit = ProjectResource.Drumkit(0, "filename", "iter")) {}
    }
}

@Composable
fun SynthIcon(modifier: Modifier = Modifier, engine: SynthEngine) {
    val icon by remember {
        mutableIntStateOf(
            when (engine) {
                is SynthEngine.Cluster -> R.drawable.synth_cluster
                is SynthEngine.DNA -> R.drawable.synth_dna
                is SynthEngine.DSynth -> R.drawable.synth_dsynth
                is SynthEngine.Digital -> R.drawable.synth_digital
                is SynthEngine.DrWave -> R.drawable.synth_drwave
                is SynthEngine.FM -> R.drawable.synth_fm
                is SynthEngine.Iter -> R.drawable.synth_cluster
                is SynthEngine.Phase -> R.drawable.synth_phase
                is SynthEngine.Pulse -> R.drawable.synth_pulse
                is SynthEngine.Sampler -> R.drawable.synth_sampler
                is SynthEngine.String -> R.drawable.synth_string
                is SynthEngine.Voltage -> R.drawable.synth_voltage
                is SynthEngine.Undefined -> R.drawable.synth
            }
        )
    }

    Icon(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun TapeIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.tape_selector),
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
