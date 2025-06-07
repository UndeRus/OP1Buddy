package org.jugregator.op1buddy.ui.icons.bottombar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.BottomBar.Recover: ImageVector
    get() {
        if (_Recover != null) {
            return _Recover!!
        }
        _Recover = ImageVector.Builder(
            name = "BottomBar.BbRecover",
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
                moveTo(40f, 24f)
                verticalLineTo(14f)
                lineTo(30f, 4f)
                horizontalLineTo(12f)
                curveTo(10.939f, 4f, 9.922f, 4.421f, 9.172f, 5.172f)
                curveTo(8.421f, 5.922f, 8f, 6.939f, 8f, 8f)
                verticalLineTo(40f)
                curveTo(8f, 41.061f, 8.421f, 42.078f, 9.172f, 42.828f)
                curveTo(9.922f, 43.579f, 10.939f, 44f, 12f, 44f)
                horizontalLineTo(21f)
            }
            path(
                stroke = SolidColor(Color(0xFF868686)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(26f, 28f)
                verticalLineTo(34f)
                moveTo(26f, 34f)
                horizontalLineTo(32.4f)
                moveTo(26f, 34f)
                lineTo(28.456f, 31.593f)
                curveTo(29.375f, 30.765f, 30.49f, 30.155f, 31.711f, 29.813f)
                curveTo(32.932f, 29.472f, 34.222f, 29.408f, 35.474f, 29.628f)
                curveTo(36.726f, 29.848f, 37.904f, 30.346f, 38.91f, 31.079f)
                curveTo(39.915f, 31.812f, 40.72f, 32.759f, 41.256f, 33.842f)
                moveTo(42f, 46f)
                verticalLineTo(40f)
                moveTo(42f, 40f)
                horizontalLineTo(35.6f)
                moveTo(42f, 40f)
                lineTo(39.544f, 42.408f)
                curveTo(38.625f, 43.235f, 37.51f, 43.845f, 36.289f, 44.187f)
                curveTo(35.068f, 44.528f, 33.778f, 44.592f, 32.526f, 44.372f)
                curveTo(31.274f, 44.152f, 30.096f, 43.654f, 29.09f, 42.921f)
                curveTo(28.085f, 42.188f, 27.28f, 41.241f, 26.744f, 40.158f)
            }
        }.build()

        return _Recover!!
    }

@Suppress("ObjectPropertyName")
private var _Recover: ImageVector? = null
