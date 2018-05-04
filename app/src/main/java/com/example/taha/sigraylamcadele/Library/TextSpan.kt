package com.example.taha.sigraylamcadele.Library

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.style.LineBackgroundSpan


class TextSpan : LineBackgroundSpan {

    private val radius: Float
    private val color: Int
    private var text:String? = ""


    constructor(radius: Float, color: Int,text:String) {
        this.radius = radius
        this.color = color
        this.text = text
    }

    override fun drawBackground(
            canvas: Canvas, paint: Paint,
            left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
            charSequence: CharSequence,
            start: Int, end: Int, lineNum: Int
    ) {
        var zundi = paint
        zundi.setTextSize(25f)
        zundi.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        val oldColor = paint.color
        if (color != 0) {
            paint.color = color
        }
        canvas.drawText(text,
                ((left + right) / 2).toFloat() -18,
                bottom + radius + 18,zundi)
        paint.color = oldColor
    }

    companion object {

        /**
         * Default radius used
         */
        val DEFAULT_RADIUS = 3f
    }
}
