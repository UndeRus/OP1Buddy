package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Synth: ImageVector
    get() {
        if (_Synth != null) {
            return _Synth!!
        }
        _Synth = ImageVector.Builder(
            name = "Synth.Synth",
            defaultWidth = 3.9918458.dp,
            defaultHeight = 2.11.dp,
            viewportWidth = 3.9769f,
            viewportHeight = 2.1021f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF4D4D4D)),
                strokeLineWidth = 0.227252f
            ) {
                moveToRelative(3.8633f, 0.4261f)
                curveToRelative(0f, -0.1704f, -0.142f, -0.3125f, -0.3125f, -0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, 0.142f, -0.3125f, 0.3125f)
                verticalLineToRelative(1.2499f)
                curveToRelative(0f, 0.1704f, -0.142f, 0.3125f, -0.3125f, 0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, -0.142f, -0.3125f, -0.3125f)
                verticalLineToRelative(-1.2499f)
                curveToRelative(0f, -0.1704f, -0.142f, -0.3125f, -0.3125f, -0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, 0.142f, -0.3125f, 0.3125f)
                verticalLineToRelative(1.2499f)
                curveToRelative(0f, 0.1704f, -0.142f, 0.3125f, -0.3125f, 0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, -0.142f, -0.3125f, -0.3125f)
                verticalLineToRelative(-1.2499f)
                curveToRelative(0f, -0.1704f, -0.142f, -0.3125f, -0.3125f, -0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, 0.142f, -0.3125f, 0.3125f)
                verticalLineToRelative(1.2499f)
                curveToRelative(0f, 0.1704f, -0.142f, 0.3125f, -0.3125f, 0.3125f)
                curveToRelative(-0.1704f, 0f, -0.3125f, -0.142f, -0.3125f, -0.3125f)
            }
        }.build()

        return _Synth!!
    }

@Suppress("ObjectPropertyName")
private var _Synth: ImageVector? = null
