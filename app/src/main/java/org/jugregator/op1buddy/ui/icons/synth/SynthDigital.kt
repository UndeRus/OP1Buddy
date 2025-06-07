package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Digital: ImageVector
    get() {
        if (_Digital != null) {
            return _Digital!!
        }
        _Digital = ImageVector.Builder(
            name = "Synth.SynthDigital",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(16f)
                    verticalLineToRelative(16f)
                    horizontalLineToRelative(-16f)
                    close()
                }
            ) {
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(1.82f, 3.87f)
                    lineTo(12.4f, 12.57f)
                    lineTo(13.36f, 6.6f)
                    lineTo(3.69f, 12.33f)
                    lineTo(2.47f, 11.63f)
                    lineTo(8.96f, 3.23f)
                    lineTo(1.82f, 3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(9.46f, 4.77f)
                    curveTo(10.531f, 4.77f, 11.4f, 3.901f, 11.4f, 2.83f)
                    curveTo(11.4f, 1.759f, 10.531f, 0.89f, 9.46f, 0.89f)
                    curveTo(8.389f, 0.89f, 7.52f, 1.759f, 7.52f, 2.83f)
                    curveTo(7.52f, 3.901f, 8.389f, 4.77f, 9.46f, 4.77f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(1.94f, 5.59f)
                    curveTo(3.011f, 5.59f, 3.88f, 4.721f, 3.88f, 3.65f)
                    curveTo(3.88f, 2.579f, 3.011f, 1.71f, 1.94f, 1.71f)
                    curveTo(0.869f, 1.71f, 0f, 2.579f, 0f, 3.65f)
                    curveTo(0f, 4.721f, 0.869f, 5.59f, 1.94f, 5.59f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(2.74f, 14.17f)
                    curveTo(3.811f, 14.17f, 4.68f, 13.301f, 4.68f, 12.23f)
                    curveTo(4.68f, 11.159f, 3.811f, 10.29f, 2.74f, 10.29f)
                    curveTo(1.669f, 10.29f, 0.8f, 11.159f, 0.8f, 12.23f)
                    curveTo(0.8f, 13.301f, 1.669f, 14.17f, 2.74f, 14.17f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(12.59f, 15.11f)
                    curveTo(13.661f, 15.11f, 14.53f, 14.241f, 14.53f, 13.17f)
                    curveTo(14.53f, 12.099f, 13.661f, 11.23f, 12.59f, 11.23f)
                    curveTo(11.519f, 11.23f, 10.65f, 12.099f, 10.65f, 13.17f)
                    curveTo(10.65f, 14.241f, 11.519f, 15.11f, 12.59f, 15.11f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(14.06f, 7.94f)
                    curveTo(15.131f, 7.94f, 16f, 7.071f, 16f, 6f)
                    curveTo(16f, 4.929f, 15.131f, 4.06f, 14.06f, 4.06f)
                    curveTo(12.989f, 4.06f, 12.12f, 4.929f, 12.12f, 6f)
                    curveTo(12.12f, 7.071f, 12.989f, 7.94f, 14.06f, 7.94f)
                    close()
                }
            }
        }.build()

        return _Digital!!
    }

@Suppress("ObjectPropertyName")
private var _Digital: ImageVector? = null
