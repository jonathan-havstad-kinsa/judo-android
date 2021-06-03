package app.judo.sdk.ui.extensions

import android.content.Context
import app.judo.sdk.api.models.Alignment
import app.judo.sdk.api.models.Layer
import app.judo.sdk.api.models.Padding
import app.judo.sdk.ui.layout.composition.getX
import app.judo.sdk.ui.layout.composition.getY
import app.judo.sdk.ui.layout.composition.setX
import app.judo.sdk.ui.layout.composition.setY

internal fun Layer.alignIn(width: Float, height: Float, alignment: Alignment) {
    when(alignment) {
        Alignment.TOP -> {
            setX(getX() + ((width - sizeAndCoordinates.width) / 2f))
        }
        Alignment.TOP_LEADING -> { }
        Alignment.TOP_TRAILING -> {
            setX(getX() + (width - sizeAndCoordinates.width))
        }
        Alignment.BOTTOM -> {
            setX(getX() + ((width - sizeAndCoordinates.width) / 2f))
            setY(getY() + height - sizeAndCoordinates.height)
        }
        Alignment.BOTTOM_LEADING -> {
            setY(getY() + height - sizeAndCoordinates.height)
        }
        Alignment.BOTTOM_TRAILING -> {
            setX(getX() + width - sizeAndCoordinates.width)
            setY(getY() + height - sizeAndCoordinates.height)
        }
        Alignment.LEADING -> {
            setY(getY() + ((height - sizeAndCoordinates.height) / 2f))
        }
        Alignment.TRAILING -> {
            setX(getX() + width - sizeAndCoordinates.width)
            setY(getY() + ((height - sizeAndCoordinates.height) / 2f))
        }
        Alignment.CENTER -> {
            setX(getX() + ((width - sizeAndCoordinates.width) / 2f))
            setY(getY() + ((height - sizeAndCoordinates.height) / 2f))
        }
    }
}

internal fun Layer.adjustPositionForPadding(context: Context, padding: Padding?) {
    val horizontalPaddingWhenCentred = (((padding?.leading?.dp?.toPx(context) ?: 0f) - ((padding?.trailing?.dp?.toPx(context)) ?: 0f)) / 2f)
    val verticalPaddingWhenCentred = (((padding?.top?.dp?.toPx(context) ?: 0f) - ((padding?.bottom?.dp?.toPx(context)) ?: 0f)) / 2f)

    when (frame?.alignment) {
        Alignment.TOP -> {
            setY(getY() + (padding?.top?.dp?.toPx(context) ?: 0f))
            setX(getX() + horizontalPaddingWhenCentred)
        }
        Alignment.TOP_LEADING -> {
            setY(getY() + (padding?.top?.dp?.toPx(context) ?: 0f))
            setX(getX() + (padding?.leading?.dp?.toPx(context) ?: 0f))
        }
        Alignment.TOP_TRAILING -> {
            setY(getY() + (padding?.top?.dp?.toPx(context) ?: 0f))
            setX(getX() - (padding?.trailing?.dp?.toPx(context) ?: 0f))
        }
        Alignment.BOTTOM -> {
            setY(getY() - (padding?.bottom?.dp?.toPx(context) ?: 0f))
            setX(getX() + horizontalPaddingWhenCentred)
        }
        Alignment.BOTTOM_LEADING -> {
            setY(getY() - (padding?.bottom?.dp?.toPx(context) ?: 0f))
            setX(getX() + (padding?.leading?.dp?.toPx(context) ?: 0f))
        }
        Alignment.BOTTOM_TRAILING -> {
            setY(getY() - (padding?.bottom?.dp?.toPx(context) ?: 0f))
            setX(getX() - (padding?.trailing?.dp?.toPx(context) ?: 0f))
        }
        Alignment.LEADING -> {
            setX(getX() + (padding?.leading?.dp?.toPx(context) ?: 0f))
            setY(getY() + verticalPaddingWhenCentred)
        }
        Alignment.TRAILING -> {
            setY(getY() + verticalPaddingWhenCentred)
            setX(getX() - (padding?.trailing?.dp?.toPx(context) ?: 0f))
        }
        Alignment.CENTER -> {
            setX(getX() + horizontalPaddingWhenCentred)
            setY(getY() + verticalPaddingWhenCentred)
        }
        null -> {
            setY(getY() + (padding?.top?.dp?.toPx(context) ?: 0f))
            setX(getX() + (padding?.leading?.dp?.toPx(context) ?: 0f))
        }
    }
}

fun medianOf(first: Float, second: Float, third: Float): Float {
    val max = maxOf(first, second, third)
    val min = minOf(first, second, third)
    val median = listOf(first, second, third).find { it != max && it != min }
    return median ?: first
}