package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectTitle
import org.jugregator.op1buddy.data.synth.SynthEngine
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ProjectResourceItem(type: ProjectResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val title by remember {
        mutableStateOf(
            when (type) {
                is ProjectResource.Synth -> "Synth"
                is ProjectResource.Drumkit -> "Drum"
                is ProjectResource.Tape -> "Tape"
            }
        )
    }
    Row(modifier = modifier.padding(8.dp)) {
        when (type) {
            is ProjectResource.Drumkit -> DrumkitResourceItem(drumkit = type, onClick = onClick)
            is ProjectResource.Synth -> SynthResourceItem(synth = type)
            is ProjectResource.Tape -> ProjectTitle(
                title = "$title 0",
                onClick = onClick,
                modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun DrumkitResourceItem(modifier: Modifier = Modifier, drumkit: ProjectResource.Drumkit, onClick: () -> Unit) {
    Card(modifier = modifier, onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .padding(8.dp)
                    .weight(1f),
                text = drumkit.name
            )
        }
    }
}

@Composable
fun SynthResourceItem(modifier: Modifier = Modifier, synth: ProjectResource.Synth) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .padding(8.dp)
                    .weight(1f),
                text = synth.name
            )
            SynthIcon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp), engine = synth.engine
            )
        }
    }
}

@Preview
@Composable
fun SyntResourceItemPreview() {
    AppTheme {
        SynthResourceItem(synth = ProjectResource.Synth(0, "filename", "iter", SynthEngine.Iter()))
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