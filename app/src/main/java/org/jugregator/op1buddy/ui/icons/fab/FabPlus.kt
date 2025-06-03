package org.jugregator.op1buddy.ui.icons.fab

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Fab.FabPlus: ImageVector
    get() {
        if (_FabPlus != null) {
            return _FabPlus!!
        }
        _FabPlus = ImageVector.Builder(
            name = "Fab.FabPlus",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 32f,
            viewportHeight = 32f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFFEFEFE)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 2f)
                verticalLineTo(30f)
            }
            path(
                stroke = SolidColor(Color(0xFFFEFEFE)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 16f)
                horizontalLineTo(30f)
            }
        }.build()

        return _FabPlus!!
    }

@Suppress("ObjectPropertyName")
private var _FabPlus: ImageVector? = null
