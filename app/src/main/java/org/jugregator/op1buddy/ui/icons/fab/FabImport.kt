package org.jugregator.op1buddy.ui.icons.fab

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Fab.FabImport: ImageVector
    get() {
        if (_FabImport != null) {
            return _FabImport!!
        }
        _FabImport = ImageVector.Builder(
            name = "Fab.FabImport",
            defaultWidth = 30.dp,
            defaultHeight = 32.dp,
            viewportWidth = 30f,
            viewportHeight = 32f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFFEFEFE)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 17f)
                lineTo(15f, 30f)
                lineTo(28f, 17f)
            }
            path(
                stroke = SolidColor(Color(0xFFFEFEFE)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 2f)
                verticalLineTo(30f)
            }
        }.build()

        return _FabImport!!
    }

@Suppress("ObjectPropertyName")
private var _FabImport: ImageVector? = null
