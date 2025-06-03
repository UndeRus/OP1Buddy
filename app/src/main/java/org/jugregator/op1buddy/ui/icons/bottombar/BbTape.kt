package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Tape: ImageVector
    get() {
        if (_Tape != null) {
            return _Tape!!
        }
        _Tape = ImageVector.Builder(
            name = "BottomBar.BbTape",
            defaultWidth = 36.dp,
            defaultHeight = 17.560976.dp,
            viewportWidth = 41f,
            viewportHeight = 20f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.039f, 17.965f)
                curveTo(14.479f, 17.965f, 18.078f, 14.366f, 18.078f, 9.926f)
                curveTo(18.078f, 5.486f, 14.479f, 1.887f, 10.039f, 1.887f)
                curveTo(5.599f, 1.887f, 2f, 5.486f, 2f, 9.926f)
                curveTo(2f, 14.366f, 5.599f, 17.965f, 10.039f, 17.965f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(31.17f, 17.965f)
                curveTo(35.61f, 17.965f, 39.209f, 14.366f, 39.209f, 9.926f)
                curveTo(39.209f, 5.486f, 35.61f, 1.887f, 31.17f, 1.887f)
                curveTo(26.731f, 1.887f, 23.131f, 5.486f, 23.131f, 9.926f)
                curveTo(23.131f, 14.366f, 26.731f, 17.965f, 31.17f, 17.965f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.039f, 18.425f)
                horizontalLineTo(31.17f)
            }
        }.build()

        return _Tape!!
    }

@Suppress("ObjectPropertyName")
private var _Tape: ImageVector? = null
