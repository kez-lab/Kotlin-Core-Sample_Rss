package io.github.kez_lab.kotlinxrpc.sample.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinrpcsample.composeapp.generated.resources.IBMPlexSansKR_Medium
import kotlinrpcsample.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

// 뉴스 앱 색상 팔레트
object NewsColors {
    val Blue = Color(0xFF2663FF)
    val DarkBlue = Color(0xFF0D47A1)
    val LightBlue = Color(0xFFE3F2FD)
    val DarkGray = Color(0xFF333333)
    val LightGray = Color(0xFFEEEEEE)
    val Background = Color(0xFFF8F9FA)
    val BackgroundDark = Color(0xFF121212)
    val TextPrimary = Color(0xFF121212)
    val TextSecondary = Color(0xFF6B6B6B)
    val CardLight = Color.White
    val CardDark = Color(0xFF202020)
}

// 테마에서 사용할 로컬 값들
val LocalNewsColors = staticCompositionLocalOf { NewsColors }

// 라이트 테마 색상
private val LightColorPalette = lightColors(
    primary = NewsColors.Blue,
    primaryVariant = NewsColors.DarkBlue,
    secondary = NewsColors.LightBlue,
    background = NewsColors.Background,
    surface = NewsColors.CardLight,
    onPrimary = Color.White,
    onSecondary = NewsColors.DarkGray,
    onBackground = NewsColors.TextPrimary,
    onSurface = NewsColors.TextPrimary
)

// 다크 테마 색상
private val DarkColorPalette = darkColors(
    primary = NewsColors.Blue,
    primaryVariant = NewsColors.LightBlue,
    secondary = NewsColors.DarkBlue,
    background = NewsColors.BackgroundDark,
    surface = NewsColors.CardDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    
    // 폰트 로딩
    val mediumFont = Font(Res.font.IBMPlexSansKR_Medium)
    val fontFamily = FontFamily(mediumFont)
    
    val typography = Typography(
        h4 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
        h5 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        h6 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        subtitle1 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        subtitle2 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        body1 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        body2 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        caption = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )

    CompositionLocalProvider(
        LocalNewsColors provides NewsColors
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            content = content
        )
    }
}