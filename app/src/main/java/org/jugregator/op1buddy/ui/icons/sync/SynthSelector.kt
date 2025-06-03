package org.jugregator.op1buddy.ui.icons.sync

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Sync.SynthSelector: ImageVector
    get() {
        if (_SynthSelector != null) {
            return _SynthSelector!!
        }
        _SynthSelector = ImageVector.Builder(
            name = "Sync.SynthSelector",
            defaultWidth = 41.dp,
            defaultHeight = 20.dp,
            viewportWidth = 41f,
            viewportHeight = 20f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(39f, 4.667f)
                curveTo(39f, 3.333f, 37.697f, 2f, 35.873f, 2f)
                curveTo(34.31f, 2f, 32.746f, 3.111f, 32.746f, 4.667f)
                verticalLineTo(15.111f)
                curveTo(32.746f, 16.444f, 31.444f, 17.778f, 29.62f, 17.778f)
                curveTo(28.056f, 17.778f, 26.493f, 16.667f, 26.493f, 15.111f)
                verticalLineTo(4.667f)
                curveTo(26.493f, 3.333f, 25.19f, 2f, 23.366f, 2f)
                curveTo(22.063f, 2.222f, 20.5f, 3.333f, 20.5f, 4.667f)
                verticalLineTo(15.111f)
                curveTo(20.5f, 16.667f, 19.197f, 18f, 17.373f, 18f)
                curveTo(15.549f, 18f, 14.507f, 16.667f, 14.507f, 15.333f)
                verticalLineTo(4.667f)
                curveTo(14.507f, 3.333f, 13.204f, 2f, 11.38f, 2f)
                curveTo(9.556f, 2f, 8.254f, 3.333f, 8.254f, 4.667f)
                verticalLineTo(15.111f)
                curveTo(8.254f, 16.667f, 6.951f, 18f, 5.127f, 18f)
                curveTo(3.303f, 18f, 2f, 16.667f, 2f, 15.333f)
            }
        }.build()

        return _SynthSelector!!
    }

@Suppress("ObjectPropertyName")
private var _SynthSelector: ImageVector? = null
