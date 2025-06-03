package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Backup: ImageVector
    get() {
        if (_Backup != null) {
            return _Backup!!
        }
        _Backup = ImageVector.Builder(
            name = "BottomBar.BbBackup",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFFEFEFE)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(40f, 32f)
                lineTo(40f, 10f)
                curveTo(40f, 8.939f, 39.579f, 7.922f, 38.828f, 7.172f)
                curveTo(38.078f, 6.421f, 37.061f, 6f, 36f, 6f)
                horizontalLineTo(32f)
                moveTo(8f, 32f)
                lineTo(8f, 10f)
                curveTo(8f, 8.939f, 8.421f, 7.922f, 9.172f, 7.172f)
                curveTo(9.922f, 6.421f, 10.939f, 6f, 12f, 6f)
                horizontalLineTo(16f)
                moveTo(30f, 18f)
                lineTo(24f, 24f)
                moveTo(24f, 24f)
                lineTo(18f, 18f)
                moveTo(24f, 24f)
                verticalLineTo(6f)
                moveTo(42f, 42f)
                lineTo(6f, 42f)
                curveTo(4.895f, 42f, 4f, 41.105f, 4f, 40f)
                verticalLineTo(34f)
                curveTo(4f, 32.895f, 4.895f, 32f, 6f, 32f)
                lineTo(42f, 32f)
                curveTo(43.105f, 32f, 44f, 32.895f, 44f, 34f)
                verticalLineTo(40f)
                curveTo(44f, 41.105f, 43.105f, 42f, 42f, 42f)
                close()
            }
        }.build()

        return _Backup!!
    }

@Suppress("ObjectPropertyName")
private var _Backup: ImageVector? = null
