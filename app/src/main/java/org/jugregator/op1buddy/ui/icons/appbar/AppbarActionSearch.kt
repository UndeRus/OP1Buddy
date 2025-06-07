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

val Icons.AppBar.ActionSearch: ImageVector
    get() {
        if (_ActionSearch != null) {
            return _ActionSearch!!
        }
        _ActionSearch = ImageVector.Builder(
            name = "AppBar.AppbarActionSearch",
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
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 3f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(9f, 16f)
                    curveTo(12.866f, 16f, 16f, 12.866f, 16f, 9f)
                    curveTo(16f, 5.134f, 12.866f, 2f, 9f, 2f)
                    curveTo(5.134f, 2f, 2f, 5.134f, 2f, 9f)
                    curveTo(2f, 12.866f, 5.134f, 16f, 9f, 16f)
                    close()
                }
                path(
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 3f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(18f, 18f)
                    lineTo(14f, 14f)
                }
            }
        }.build()

        return _ActionSearch!!
    }

@Suppress("ObjectPropertyName")
private var _ActionSearch: ImageVector? = null
