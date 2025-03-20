package org.jugregator.op1buddy.features.drumkit.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ThreePianoKeys(
    modifier: Modifier = Modifier,
    cTitle: String,
    onCClick: () -> Unit,
    cSharpTitle: String,
    onCSharpClick: () -> Unit,
    dTitle: String,
    onDClick: () -> Unit,
    dSharpTitle: String,
    onDSharpClick: () -> Unit,
    eTitle: String,
    onEClick: () -> Unit,
) {
    Row(modifier = modifier.padding(end = 18.dp)) {
        Column(
            modifier = modifier
                .width(242.dp)
                .heightIn(196.dp)
        ) {
            WhitePianoKey(
                cTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 9.dp, end = 29.dp),
            ) {
                onCClick()
            }
            WhitePianoKey(
                dTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp, end = 29.dp),
            ) {
                onDClick()
            }
            WhitePianoKey(
                eTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp, end = 29.dp),
            ) {
                onEClick()
            }
        }

        Column(modifier = Modifier.heightIn(196.dp), verticalArrangement = Arrangement.Center) {
            BlackPianoKey(cSharpTitle) {
                onCSharpClick()
            }
            BlackPianoKey(dSharpTitle, modifier = Modifier.padding(top = 9.dp)) {
                onDSharpClick()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThreePianoKeysPreview() {
    AppTheme {
        ThreePianoKeys(
            cTitle = "Kick",
            onCClick = {},
            dTitle = "Snare",
            onDClick = {},
            eTitle = "Rimshot",
            onEClick = {},
            cSharpTitle = "Kick\nAlternative",
            onCSharpClick = {},
            dSharpTitle = "Snare\nAlternative",
            onDSharpClick = {})
    }
}


@Composable
fun FourPianoKeys(
    modifier: Modifier = Modifier,
    fTitle: String,
    onFClick: () -> Unit,
    gTitle: String,
    onGClick: () -> Unit,
    aTitle: String,
    onAClick: () -> Unit,
    bTitle: String,
    onBClick: () -> Unit,
    fSharpTitle: String,
    onFSharpClick: () -> Unit,
    gSharpTitle: String,
    onGSharpClick: () -> Unit,
    aSharpTitle: String,
    onASharpClick: () -> Unit,
) {
    Row(modifier = modifier.padding(end = 18.dp)) {
        Column(
            modifier = modifier
                .width(242.dp)
                .heightIn(261.dp)
        ) {
            WhitePianoKey(
                fTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 9.dp, end = 29.dp),
            ) {
                onFClick()
            }
            WhitePianoKey(
                gTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp, end = 29.dp),
            ) {
                onGClick()
            }
            WhitePianoKey(
                aTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp, end = 29.dp),
            ) {
                onAClick()
            }
            WhitePianoKey(
                bTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp, end = 29.dp),
            ) {
                onBClick()
            }
        }

        Column(modifier = Modifier.heightIn(261.dp), verticalArrangement = Arrangement.Center) {
            BlackPianoKey(fSharpTitle) {
                onFSharpClick()
            }
            BlackPianoKey(gSharpTitle, modifier = Modifier.padding(top = 9.dp)) {
                onGSharpClick()
            }
            BlackPianoKey(aSharpTitle, modifier = Modifier.padding(top = 9.dp)) {
                onASharpClick()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FourPianoKeysPreview() {
    AppTheme {
        FourPianoKeys(
            fTitle = "Kick",
            onFClick = {},
            gTitle = "Snare",
            onGClick = {},
            aTitle = "Rimshot",
            onAClick = {},
            bTitle = "Rimshot",
            onBClick = {},
            fSharpTitle = "Kick\nAlternative",
            onFSharpClick = {},
            gSharpTitle = "Snare\nAlternative",
            onGSharpClick = {},
            aSharpTitle = "Snare\nAlternative",
            onASharpClick = {},
        )
    }
}


@Composable
fun WhitePianoKey(title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomEnd = 8.dp))
            .background(Color(0xff1c1c1c))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            title,
            modifier = Modifier.padding(19.dp),
            style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PianoKeyPreview() {
    AppTheme {
        WhitePianoKey(title = "Kick") {}
    }
}

@Composable
fun BlackPianoKey(title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xff1c1c1c))
                .clickable { onClick() }
        )
        Text(title, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.titleSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun BlackPianoKeyPreview() {
    AppTheme {
        BlackPianoKey("Kick\nAlternative") {}
    }
}
