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

val Icons.Synth.Dna: ImageVector
    get() {
        if (_Dna != null) {
            return _Dna!!
        }
        _Dna = ImageVector.Builder(
            name = "Synth.SynthDna",
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
                    moveTo(2.08f, 1.04f)
                    lineTo(7.98f, 6.9f)
                    lineTo(13.92f, 1.04f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(13.92f, 14.96f)
                    lineTo(8.02f, 9.1f)
                    lineTo(2.08f, 14.96f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(14.555f, 7.656f)
                    lineTo(8.008f, 1.108f)
                    lineTo(1.46f, 7.656f)
                    lineTo(8.008f, 14.204f)
                    lineTo(14.555f, 7.656f)
                    close()
                }
            }
        }.build()

        return _Dna!!
    }

@Suppress("ObjectPropertyName")
private var _Dna: ImageVector? = null
