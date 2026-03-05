package edu.cnm.deepdive.codebreaker.app.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import edu.cnm.deepdive.codebreaker.app.R
import jakarta.inject.Inject

class SymbolMap @Inject constructor(
    @param:ActivityContext private val context: Context
) {

    private val mapping: MutableMap<Int, SymbolAttributes> = mutableMapOf()

    init {
        val names = context.resources.getStringArray(R.array.color_names)
        val valuesTyped = context.resources.obtainTypedArray(R.array.color_values)
        val values = mutableListOf<Int>()
        for (i in 0 until valuesTyped.length()) {
            val color = valuesTyped.getColor(i, Color.TRANSPARENT)
            values.add(color)

        }
        val colorKeys = context.resources.getStringArray(R.array.color_keys)
        val drawableIds = context.resources.getIntArray(R.array.color_drawables)
        val drawables = mutableListOf<Drawable>()
        for (i in drawableIds.indices) {
            val drawable = ContextCompat.getDrawable(context, drawableIds[i])!!
            drawables.add(drawable)
        }
        colorKeys.indices.forEach { i ->
            val key = colorKeys[i]
            val name = names[i]
            val value = values[i]
            val drawable = drawables[i]
            mapping[key.codePointAt(0)] = SymbolAttributes(value, name, drawable)
        }
    }

    private data class SymbolAttributes(
        val value: Int,
        val name: String,
        val drawable: Drawable
    )

// Utility methods can be added here


}
