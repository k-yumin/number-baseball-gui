package yt.yacht.ui

import java.awt.*
import javax.swing.JPanel

class Light : JPanel() {

    init { isOpaque = false }

    private val renderer = mapOf(
        RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
        RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
        RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY,
        RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE,
        RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON,
        RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
        RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
        RenderingHints.KEY_STROKE_CONTROL to RenderingHints.VALUE_STROKE_PURE,
    )

    val defaultLightColor: Color = Color.DARK_GRAY
    var lightColor: Color = Color.DARK_GRAY

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHints(renderer)

        val borderWidth = 4

        val d = width.coerceAtMost(height) - 2 * borderWidth
        val x = (width - d) / 2
        val y = (height - d) / 2

        g2d.color = Color.DARK_GRAY
        g2d.stroke = BasicStroke(borderWidth.toFloat())
        g2d.drawOval(x, y, d, d)

        g2d.color = lightColor
        g2d.fillOval(
            x + borderWidth / 2, y + borderWidth / 2,
            d - borderWidth, d - borderWidth
        )
    }
}