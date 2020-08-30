package com.alamkanak.weekview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.shopify.testify.ScreenshotRule

class RendererTestActivity : AppCompatActivity() {

    val imageView: ImageView by lazy { ImageView(this) }

    val bitmap: Bitmap
        get() = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(imageView)
    }

    fun render(bitmap: Bitmap) {
        runOnUiThread {
            imageView.setImageBitmap(bitmap)
        }
    }
}

internal fun createViewState(
    activity: RendererTestActivity,
    configuration: ViewState.() -> Unit
): ViewState {
    val viewState = ViewState(
        viewWidth = activity.imageView.width,
        viewHeight = activity.imageView.height
    )
    viewState.configuration()
    return viewState
}

internal fun ScreenshotRule<RendererTestActivity>.validate(renderer: Renderer) {
    val bitmap = activity.bitmap
    renderer.render(Canvas(bitmap))
    activity.render(bitmap)
    assertSame()
}
