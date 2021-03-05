package dev.bitvictory.bitfence.ui.theme

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private val DarkColorPalette = darkColors(
    primary = purplePrimaryLight,
    primaryVariant = purplePrimaryDark,
    secondary = purpleAccent,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = purplePrimary,
    primaryVariant = purplePrimaryDark,
    secondary = purpleAccent,
    onSecondary = Color.White

    /* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
)

@Composable
fun GeofenceDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity,
    content: @Composable() () -> Unit,
) {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    val colors = if (darkTheme) {
        activity.window.statusBarColor = DarkColorPalette.primaryVariant.toArgb()
        DarkColorPalette
    } else {
        activity.window.statusBarColor = LightColorPalette.primaryVariant.toArgb()
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}