package org.jugregator.op1buddy.ui.icons.player

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Player.TrackMuted: ImageVector
    get() {
        if (_TrackMuted != null) {
            return _TrackMuted!!
        }
        _TrackMuted = ImageVector.Builder(
            name = "Player.TrackMuted",
            defaultWidth = 36.dp,
            defaultHeight = 36.dp,
            viewportWidth = 36f,
            viewportHeight = 36f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 2f
            ) {
                moveTo(35f, 18f)
                curveTo(35f, 27.389f, 27.389f, 35f, 18f, 35f)
                curveTo(8.611f, 35f, 1f, 27.389f, 1f, 18f)
                curveTo(1f, 8.611f, 8.611f, 1f, 18f, 1f)
                curveTo(27.389f, 1f, 35f, 8.611f, 35f, 18f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(17f, 11f)
                lineTo(12f, 15f)
                horizontalLineTo(8f)
                verticalLineTo(21f)
                horizontalLineTo(12f)
                lineTo(17f, 25f)
                verticalLineTo(11f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(29f, 15f)
                lineTo(23f, 21f)
            }
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(23f, 15f)
                lineTo(29f, 21f)
            }
        }.build()

        return _TrackMuted!!
    }

@Suppress("ObjectPropertyName")
private var _TrackMuted: ImageVector? = null
