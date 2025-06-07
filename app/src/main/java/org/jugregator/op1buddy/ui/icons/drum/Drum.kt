package org.jugregator.op1buddy.ui.icons.drum

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Drum.Drum: ImageVector
    get() {
        if (_Drum != null) {
            return _Drum!!
        }
        _Drum = ImageVector.Builder(
            name = "Drum.Drum",
            defaultWidth = 2.727.dp,
            defaultHeight = 3.1815.dp,
            viewportWidth = 2.727f,
            viewportHeight = 3.1815f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF4D4D4D)),
                strokeLineWidth = 0.227252f
            ) {
                moveTo(1.3635f, 1.3635f)
                moveToRelative(-1.2499f, 0f)
                arcToRelative(1.2499f, 1.2499f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.4998f, 0f)
                arcToRelative(1.2499f, 1.2499f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.4998f, 0f)
            }
            path(
                stroke = SolidColor(Color(0xFF4D4D4D)),
                strokeLineWidth = 0.227252f
            ) {
                moveToRelative(1.6192f, 1.5908f)
                curveToRelative(0f, 0.142f, -0.1136f, 0.2557f, -0.2557f, 0.2557f)
                curveToRelative(-0.142f, 0f, -0.2557f, -0.1136f, -0.2557f, -0.2557f)
                curveToRelative(0f, -0.142f, 0.1428f, -0.2272f, 0.2557f, -0.2272f)
                curveToRelative(0.1128f, 0f, 0.2557f, 0.0852f, 0.2557f, 0.2272f)
                close()
                moveTo(1.3635f, 3.1815f)
                verticalLineToRelative(-1.5908f)
            }
        }.build()

        return _Drum!!
    }

@Suppress("ObjectPropertyName")
private var _Drum: ImageVector? = null
