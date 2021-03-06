// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.wpam.graphic

import android.gesture.OrientedBoundingBox
import android.graphics.*
import android.util.Log
import com.example.wpam.graphic.GraphicOverlay.Graphic
import com.google.firebase.ml.vision.text.FirebaseVisionText

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic internal constructor(
    overlay: GraphicOverlay?,
    private val content: String,
    private val boundingBox: Rect?
) :
    Graphic(overlay!!) {
    private val rectPaint: Paint = Paint()
    private val textPaint: Paint

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas?) {
        Log.d(TAG, "on draw text graphic")
        checkNotNull(boundingBox) { "Attempting to draw a null text." }

        // Draws the bounding box around the TextBlock.
        val rect = RectF(boundingBox)
        canvas!!.drawRect(rect.left, rect.top, textPaint.measureText(content) + rect.right, rect.bottom, rectPaint)

        // Renders the text at the bottom of the box.
        canvas.drawText(content, rect.left, rect.bottom, textPaint)
    }

    companion object {
        private const val TAG = "TextGraphic"
        private const val TEXT_COLOR = Color.WHITE
        private const val RECT_COLOR = Color.BLACK
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
    }

    init {
        rectPaint.color =
            RECT_COLOR
        rectPaint.style = Paint.Style.FILL
        rectPaint.strokeWidth =
            STROKE_WIDTH
        textPaint = Paint()
        textPaint.color =
            TEXT_COLOR
        textPaint.textSize =
            TEXT_SIZE
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }
}
