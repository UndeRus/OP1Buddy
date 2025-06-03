package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Export: ImageVector
    get() {
        if (_Export != null) {
            return _Export!!
        }
        _Export = ImageVector.Builder(
            name = "BottomBar.BbExport",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(28f, 4f)
                verticalLineTo(12f)
                curveTo(28f, 13.061f, 28.421f, 14.078f, 29.172f, 14.828f)
                curveTo(29.922f, 15.579f, 30.939f, 16f, 32f, 16f)
                horizontalLineTo(40f)
                moveTo(24f, 24f)
                verticalLineTo(36f)
                moveTo(24f, 24f)
                lineTo(30f, 30f)
                moveTo(24f, 24f)
                lineTo(18f, 30f)
                moveTo(30f, 4f)
                horizontalLineTo(12f)
                curveTo(10.939f, 4f, 9.922f, 4.421f, 9.172f, 5.172f)
                curveTo(8.421f, 5.922f, 8f, 6.939f, 8f, 8f)
                verticalLineTo(40f)
                curveTo(8f, 41.061f, 8.421f, 42.078f, 9.172f, 42.828f)
                curveTo(9.922f, 43.579f, 10.939f, 44f, 12f, 44f)
                horizontalLineTo(36f)
                curveTo(37.061f, 44f, 38.078f, 43.579f, 38.828f, 42.828f)
                curveTo(39.579f, 42.078f, 40f, 41.061f, 40f, 40f)
                verticalLineTo(14f)
                lineTo(30f, 4f)
                close()
            }
        }.build()

        return _Export!!
    }

@Suppress("ObjectPropertyName")
private var _Export: ImageVector? = null
