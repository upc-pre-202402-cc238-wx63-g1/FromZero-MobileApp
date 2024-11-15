package com.cursokotlin.appfromzero.adapters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.BitmapShader
import com.squareup.picasso.Transformation
import com.squareup.picasso.Picasso


class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createScaledBitmap(squaredBitmap, size, size, true)
        squaredBitmap.recycle()

        val output = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(output)
        val paint = Paint()
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        paint.shader = shader
        canvas.drawCircle((size / 2).toFloat(), (size / 2).toFloat(), (size / 2).toFloat(), paint)
        bitmap.recycle()

        return output
    }

    override fun key(): String {
        return "circle"
    }
}
