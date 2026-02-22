package com.robert.characters.util

import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.robert.common.dispatcher.LocalDispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber


@Composable
fun rememberDominantColor(
    imageUrl: String?,
    defaultColor: Color = Color.Transparent
): Color {
    var dominantColor by remember(imageUrl) { mutableStateOf(defaultColor) }
    val context = LocalContext.current
    val dispatchers = LocalDispatcherProvider.current

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            dominantColor = defaultColor
            return@LaunchedEffect
        }

        try {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = withContext(dispatchers.io) {
                imageLoader.execute(request)
            }

            if (result is SuccessResult) {
                val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    val palette = withContext(dispatchers.default) {
                        Palette.from(bitmap).generate()
                    }

                    val extractedColor = palette.vibrantSwatch?.rgb
                        ?: palette.dominantSwatch?.rgb
                        ?: palette.lightVibrantSwatch?.rgb
                        ?: palette.darkVibrantSwatch?.rgb

                    if (extractedColor != null) {
                        dominantColor = Color(extractedColor)
                        Timber.d("Extracted color from $imageUrl: $extractedColor")
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to extract color from $imageUrl")
            dominantColor = defaultColor
        }
    }

    return dominantColor
}




