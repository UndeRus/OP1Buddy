package org.jugregator.op1buddy.ui.icons.sync

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Sync.TapeSelector: ImageVector
    get() {
        if (_TapeSelector != null) {
            return _TapeSelector!!
        }
        _TapeSelector = ImageVector.Builder(
            name = "Sync.TapeSelector",
            defaultWidth = 54.dp,
            defaultHeight = 38.dp,
            viewportWidth = 54f,
            viewportHeight = 38f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13.967f, 29.254f)
                curveTo(19.444f, 29.254f, 23.883f, 24.528f, 23.883f, 18.698f)
                curveTo(23.883f, 12.869f, 19.444f, 8.143f, 13.967f, 8.143f)
                curveTo(8.49f, 8.143f, 4.05f, 12.869f, 4.05f, 18.698f)
                curveTo(4.05f, 24.528f, 8.49f, 29.254f, 13.967f, 29.254f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(40.034f, 29.254f)
                curveTo(45.51f, 29.254f, 49.95f, 24.528f, 49.95f, 18.698f)
                curveTo(49.95f, 12.869f, 45.51f, 8.143f, 40.034f, 8.143f)
                curveTo(34.557f, 8.143f, 30.117f, 12.869f, 30.117f, 18.698f)
                curveTo(30.117f, 24.528f, 34.557f, 29.254f, 40.034f, 29.254f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13.967f, 29.857f)
                horizontalLineTo(40.033f)
            }
        }.build()

        return _TapeSelector!!
    }

@Suppress("ObjectPropertyName")
private var _TapeSelector: ImageVector? = null
