package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Drumkit: ImageVector
    get() {
        if (_Drumkit != null) {
            return _Drumkit!!
        }
        _Drumkit = ImageVector.Builder(
            name = "BottomBar.BbDrumkit",
            defaultWidth = 36.041378.dp,
            defaultHeight = 40.2.dp,
            viewportWidth = 26f,
            viewportHeight = 29f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12.795f, 23.884f)
                curveTo(18.757f, 23.884f, 23.591f, 19.05f, 23.591f, 13.088f)
                curveTo(23.591f, 7.126f, 18.757f, 2.293f, 12.795f, 2.293f)
                curveTo(6.833f, 2.293f, 2f, 7.126f, 2f, 13.088f)
                curveTo(2f, 19.05f, 6.833f, 23.884f, 12.795f, 23.884f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12.795f, 12.5f)
                curveTo(11.417f, 12.5f, 10.498f, 13.547f, 10.498f, 14.925f)
                curveTo(10.498f, 16.303f, 11.647f, 17.222f, 12.795f, 17.222f)
                curveTo(14.174f, 17.222f, 15.092f, 16.073f, 15.092f, 14.925f)
                curveTo(15.092f, 13.776f, 14f, 12.5f, 12.795f, 12.5f)
                close()
                moveTo(12.795f, 12.5f)
                verticalLineTo(28.706f)
            }
        }.build()

        return _Drumkit!!
    }

@Suppress("ObjectPropertyName")
private var _Drumkit: ImageVector? = null
