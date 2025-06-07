package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Phase: ImageVector
    get() {
        if (_Phase != null) {
            return _Phase!!
        }
        _Phase = ImageVector.Builder(
            name = "Synth.SynthPhase",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(1.49f, 7.87f)
                curveTo(1.49f, 7.87f, 1.59f, 4.55f, 2.81f, 4.55f)
                curveTo(5.21f, 4.55f, 3.39f, 11.44f, 5.39f, 11.44f)
                curveTo(7.39f, 11.44f, 6f, 4.55f, 8f, 4.55f)
                curveTo(9.15f, 4.55f, 9.33f, 7.87f, 9.33f, 7.87f)
                verticalLineTo(8f)
                curveTo(9.46f, 9.73f, 9.58f, 11.46f, 10.59f, 11.46f)
                curveTo(12.59f, 11.46f, 10.97f, 4.57f, 13.18f, 4.57f)
                curveTo(14.33f, 4.57f, 14.51f, 7.89f, 14.51f, 7.89f)
            }
            path(fill = SolidColor(Color(0xFF36597D))) {
                moveTo(1.49f, 10.08f)
                curveTo(2.313f, 10.08f, 2.98f, 9.413f, 2.98f, 8.59f)
                curveTo(2.98f, 7.767f, 2.313f, 7.1f, 1.49f, 7.1f)
                curveTo(0.667f, 7.1f, 0f, 7.767f, 0f, 8.59f)
                curveTo(0f, 9.413f, 0.667f, 10.08f, 1.49f, 10.08f)
                close()
            }
            path(fill = SolidColor(Color(0xFF36597D))) {
                moveTo(14.51f, 10.08f)
                curveTo(15.333f, 10.08f, 16f, 9.413f, 16f, 8.59f)
                curveTo(16f, 7.767f, 15.333f, 7.1f, 14.51f, 7.1f)
                curveTo(13.687f, 7.1f, 13.02f, 7.767f, 13.02f, 8.59f)
                curveTo(13.02f, 9.413f, 13.687f, 10.08f, 14.51f, 10.08f)
                close()
            }
        }.build()

        return _Phase!!
    }

@Suppress("ObjectPropertyName")
private var _Phase: ImageVector? = null
