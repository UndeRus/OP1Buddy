package org.jugregator.op1buddy.ui.icons.appbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.AppBar.Back: ImageVector
    get() {
        if (_Back != null) {
            return _Back!!
        }
        _Back = ImageVector.Builder(
            name = "AppBar.AppbarBack",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(20f)
                    verticalLineToRelative(20f)
                    horizontalLineToRelative(-20f)
                    close()
                }
            ) {
                path(
                    stroke = SolidColor(Color(0xFF1C1C1C)),
                    strokeLineWidth = 3f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(17.778f, 10f)
                    horizontalLineTo(2.222f)
                }
                path(
                    stroke = SolidColor(Color(0xFF1C1C1C)),
                    strokeLineWidth = 3f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(10f, 17.778f)
                    lineTo(2.222f, 10f)
                    lineTo(10f, 2.222f)
                }
            }
        }.build()

        return _Back!!
    }

@Suppress("ObjectPropertyName")
private var _Back: ImageVector? = null
