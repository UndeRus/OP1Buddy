package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Sync: ImageVector
    get() {
        if (_Sync != null) {
            return _Sync!!
        }
        _Sync = ImageVector.Builder(
            name = "BottomBar.BbSync",
            defaultWidth = 36.dp,
            defaultHeight = 30.545454.dp,
            viewportWidth = 33f,
            viewportHeight = 28f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(31.341f, 3.331f)
                verticalLineTo(11.333f)
                horizontalLineTo(23.339f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 24.67f)
                verticalLineTo(16.668f)
                horizontalLineTo(10.002f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.348f, 9.999f)
                curveTo(6.024f, 8.087f, 7.174f, 6.379f, 8.689f, 5.032f)
                curveTo(10.205f, 3.685f, 12.037f, 2.743f, 14.014f, 2.296f)
                curveTo(15.992f, 1.848f, 18.051f, 1.909f, 19.998f, 2.473f)
                curveTo(21.946f, 3.036f, 23.719f, 4.084f, 25.153f, 5.518f)
                lineTo(31.341f, 11.333f)
                moveTo(2f, 16.667f)
                lineTo(8.188f, 22.482f)
                curveTo(9.622f, 23.916f, 11.395f, 24.964f, 13.343f, 25.527f)
                curveTo(15.29f, 26.091f, 17.349f, 26.152f, 19.327f, 25.704f)
                curveTo(21.305f, 25.257f, 23.136f, 24.316f, 24.652f, 22.969f)
                curveTo(26.167f, 21.622f, 27.317f, 19.913f, 27.993f, 18.001f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19.998f, 2.473f)
                curveTo(21.946f, 3.036f, 23.719f, 4.084f, 25.153f, 5.518f)
                lineTo(31.341f, 11.333f)
                moveTo(2f, 16.667f)
                lineTo(8.188f, 22.482f)
                curveTo(9.622f, 23.916f, 11.395f, 24.964f, 13.343f, 25.527f)
            }
        }.build()

        return _Sync!!
    }

@Suppress("ObjectPropertyName")
private var _Sync: ImageVector? = null
