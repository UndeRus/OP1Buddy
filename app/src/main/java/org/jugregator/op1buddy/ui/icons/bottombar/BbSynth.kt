package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Synth: ImageVector
    get() {
        if (_Synth != null) {
            return _Synth!!
        }
        _Synth = ImageVector.Builder(
            name = "BottomBar.BbSynth",
            defaultWidth = 36.dp,
            defaultHeight = 21.dp,
            viewportWidth = 36f,
            viewportHeight = 21f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(34f, 4.833f)
                curveTo(34f, 3.417f, 32.873f, 2f, 31.296f, 2f)
                curveTo(29.944f, 2f, 28.591f, 3.181f, 28.591f, 4.833f)
                verticalLineTo(15.931f)
                curveTo(28.591f, 17.347f, 27.465f, 18.764f, 25.887f, 18.764f)
                curveTo(24.535f, 18.764f, 23.183f, 17.583f, 23.183f, 15.931f)
                verticalLineTo(4.833f)
                curveTo(23.183f, 3.417f, 22.056f, 2f, 20.479f, 2f)
                curveTo(19.352f, 2.236f, 18f, 3.417f, 18f, 4.833f)
                verticalLineTo(15.931f)
                curveTo(18f, 17.583f, 16.873f, 19f, 15.296f, 19f)
                curveTo(13.718f, 19f, 12.817f, 17.583f, 12.817f, 16.167f)
                verticalLineTo(4.833f)
                curveTo(12.817f, 3.417f, 11.69f, 2f, 10.113f, 2f)
                curveTo(8.535f, 2f, 7.408f, 3.417f, 7.408f, 4.833f)
                verticalLineTo(15.931f)
                curveTo(7.408f, 17.583f, 6.282f, 19f, 4.704f, 19f)
                curveTo(3.127f, 19f, 2f, 17.583f, 2f, 16.167f)
            }
        }.build()

        return _Synth!!
    }

@Suppress("ObjectPropertyName")
private var _Synth: ImageVector? = null
