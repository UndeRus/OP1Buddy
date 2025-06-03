package org.jugregator.op1buddy.ui.icons.appbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.AppBar.More: ImageVector
    get() {
        if (_More != null) {
            return _More!!
        }
        _More = ImageVector.Builder(
            name = "AppBar.AppbarMore",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(20f)
                    verticalLineToRelative(20f)
                    horizontalLineToRelative(-20f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF001E2F))) {
                    moveTo(9.5f, 19f)
                    curveTo(8.813f, 19f, 8.224f, 18.768f, 7.735f, 18.303f)
                    curveTo(7.245f, 17.837f, 7f, 17.278f, 7f, 16.625f)
                    curveTo(7f, 15.972f, 7.245f, 15.413f, 7.735f, 14.947f)
                    curveTo(8.224f, 14.482f, 8.813f, 14.25f, 9.5f, 14.25f)
                    curveTo(10.188f, 14.25f, 10.776f, 14.482f, 11.266f, 14.947f)
                    curveTo(11.755f, 15.413f, 12f, 15.972f, 12f, 16.625f)
                    curveTo(12f, 17.278f, 11.755f, 17.837f, 11.266f, 18.303f)
                    curveTo(10.776f, 18.768f, 10.188f, 19f, 9.5f, 19f)
                    close()
                    moveTo(9.5f, 11.875f)
                    curveTo(8.813f, 11.875f, 8.224f, 11.642f, 7.735f, 11.177f)
                    curveTo(7.245f, 10.712f, 7f, 10.153f, 7f, 9.5f)
                    curveTo(7f, 8.847f, 7.245f, 8.288f, 7.735f, 7.822f)
                    curveTo(8.224f, 7.357f, 8.813f, 7.125f, 9.5f, 7.125f)
                    curveTo(10.188f, 7.125f, 10.776f, 7.357f, 11.266f, 7.822f)
                    curveTo(11.755f, 8.288f, 12f, 8.847f, 12f, 9.5f)
                    curveTo(12f, 10.153f, 11.755f, 10.712f, 11.266f, 11.177f)
                    curveTo(10.776f, 11.642f, 10.188f, 11.875f, 9.5f, 11.875f)
                    close()
                    moveTo(9.5f, 4.75f)
                    curveTo(8.813f, 4.75f, 8.224f, 4.517f, 7.735f, 4.052f)
                    curveTo(7.245f, 3.587f, 7f, 3.028f, 7f, 2.375f)
                    curveTo(7f, 1.722f, 7.245f, 1.163f, 7.735f, 0.698f)
                    curveTo(8.224f, 0.233f, 8.813f, -0f, 9.5f, -0f)
                    curveTo(10.188f, -0f, 10.776f, 0.233f, 11.266f, 0.698f)
                    curveTo(11.755f, 1.163f, 12f, 1.722f, 12f, 2.375f)
                    curveTo(12f, 3.028f, 11.755f, 3.587f, 11.266f, 4.052f)
                    curveTo(10.776f, 4.517f, 10.188f, 4.75f, 9.5f, 4.75f)
                    close()
                }
            }
        }.build()

        return _More!!
    }

@Suppress("ObjectPropertyName")
private var _More: ImageVector? = null
