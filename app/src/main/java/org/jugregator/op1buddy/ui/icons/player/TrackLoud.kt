package org.jugregator.op1buddy.ui.icons.player

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Player.TrackLoud: ImageVector
    get() {
        if (_TrackLoud != null) {
            return _TrackLoud!!
        }
        _TrackLoud = ImageVector.Builder(
            name = "Player.TrackLoud",
            defaultWidth = 36.dp,
            defaultHeight = 36.dp,
            viewportWidth = 36f,
            viewportHeight = 36f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1C1C))) {
                moveTo(36f, 18f)
                curveTo(36f, 27.941f, 27.941f, 36f, 18f, 36f)
                curveTo(8.059f, 36f, 0f, 27.941f, 0f, 18f)
                curveTo(0f, 8.059f, 8.059f, 0f, 18f, 0f)
                curveTo(27.941f, 0f, 36f, 8.059f, 36f, 18f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFEFEFE))) {
                moveTo(22.433f, 10.099f)
                curveTo(22.78f, 10.265f, 23f, 10.616f, 23f, 11f)
                verticalLineTo(25f)
                curveTo(23f, 25.384f, 22.78f, 25.735f, 22.433f, 25.901f)
                curveTo(22.087f, 26.068f, 21.676f, 26.021f, 21.375f, 25.781f)
                lineTo(16.649f, 22f)
                horizontalLineTo(13f)
                curveTo(12.448f, 22f, 12f, 21.552f, 12f, 21f)
                verticalLineTo(15f)
                curveTo(12f, 14.448f, 12.448f, 14f, 13f, 14f)
                horizontalLineTo(16.649f)
                lineTo(21.375f, 10.219f)
                curveTo(21.676f, 9.979f, 22.087f, 9.932f, 22.433f, 10.099f)
                close()
                moveTo(21f, 13.081f)
                lineTo(17.625f, 15.781f)
                curveTo(17.447f, 15.923f, 17.227f, 16f, 17f, 16f)
                horizontalLineTo(14f)
                verticalLineTo(20f)
                horizontalLineTo(17f)
                curveTo(17.227f, 20f, 17.447f, 20.077f, 17.625f, 20.219f)
                lineTo(21f, 22.919f)
                verticalLineTo(13.081f)
                close()
            }
        }.build()

        return _TrackLoud!!
    }

@Suppress("ObjectPropertyName")
private var _TrackLoud: ImageVector? = null
