package org.jugregator.op1buddy.ui.icons.player

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Player.Stop: ImageVector
    get() {
        if (_Stop != null) {
            return _Stop!!
        }
        _Stop = ImageVector.Builder(
            name = "Player.PlayerStop",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 1f
            ) {
                moveTo(24f, 24f)
                moveToRelative(-23.5f, 0f)
                arcToRelative(23.5f, 23.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 47f, 0f)
                arcToRelative(23.5f, 23.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -47f, 0f)
            }
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(34f, 14f)
                horizontalLineTo(14f)
                verticalLineTo(34f)
                horizontalLineTo(34f)
                verticalLineTo(14f)
                close()
            }
        }.build()

        return _Stop!!
    }

@Suppress("ObjectPropertyName")
private var _Stop: ImageVector? = null
