package edu.cnm.deepdive.codebreaker.app.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import edu.cnm.deepdive.codebreaker.app.R
import jakarta.inject.Inject

class SymbolMap @Inject constructor(
    @param:ActivityContext private val context: Context
) {

    private val mapping: Map<Int, SymbolAttributes>

    init {
        val resource = context.resources
        val names = resource.getStringArray(R.array.color_names)
        val keys = resource.getStringArray(R.array.color_keys)
        val values = resource.obtainTypedArray(R.array.color_values).use { arrayOfInts(it) }
        val drawables = resource.getIntArray(R.array.color_drawables).map {
            ContextCompat.getDrawable(context, it)!!
        }

        mapping = keys.indices.associate { i ->
            keys[i].codePointAt(0) to SymbolAttributes(values[i], names[i], drawables[i])
        }
    }

    fun getKeys(): List<Int> = mapping.keys.toList()

    fun getColor(key: Int): Int = mapping[key]!!.value

    fun getName(key: Int): String = mapping[key]!!.name

    fun getDrawable(key: Int): Drawable = mapping[key]!!.drawable

    private fun arrayOfInts(array: TypedArray): IntArray =
        IntArray(array.length()) { array.getColor(it, Color.TRANSPARENT) }

    private data class SymbolAttributes(
        val value: Int,
        val name: String,
        val drawable: Drawable
    )

// Utility methods can be added here


}
