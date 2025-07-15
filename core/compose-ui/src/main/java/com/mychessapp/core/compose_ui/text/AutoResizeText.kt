package com.mychessapp.core.compose_ui.text

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp

private val DEFAULT_MAX_SIZE: TextUnit = 10000.sp

/**
 * This composable creates a text element that dynamically adjusts its font size
 * to the maximum possible value that still fits within the constraints of its parent composable.
 * The fontSize is limited by [maxFontSize] which is equal to 128sp by default.
 *
 * @param text The text to be displayed.
 * @param modifier [Modifier] to apply to this layout node.
 * @param maxFontSize Limits the max font size of this text.
 * @param maxLines An optional maximum number of lines for the text to span.
 * @param style Style configuration for the text such as color, font, line height etc.
 */
@Composable
fun AutoSizeText(
  text: String,
  modifier: Modifier = Modifier,
  maxFontSize: TextUnit = DEFAULT_MAX_SIZE,
  maxLines: Int = Int.MAX_VALUE,
  style: TextStyle = LocalTextStyle.current,
) {
  check(maxFontSize.isSpecified) {
    "maxFontSize needs to be specified"
  }
  BoxWithConstraints(modifier) {
    val density = LocalDensity.current
    val paragraphCalculator = createParagraphCalculator(
      text = text,
      style = style,
      maxLines = maxLines
    )
    val paragraph = paragraphCalculator.findFittingParagraph(
      maxFontSize = maxFontSize,
      density = density,
      maxWidth = maxWidth,
      maxHeight = maxHeight,
    )
    Canvas(Modifier.size(paragraph.getDpSize())) {
      drawIntoCanvas { canvas ->
        paragraph.paint(
          canvas = canvas,
          color = style.color,
          shadow = style.shadow,
          textDecoration = style.textDecoration,
        )
      }
    }
  }
}

private fun ParagraphCalculator.findFittingParagraph(
  maxFontSize: TextUnit,
  density: Density,
  maxWidth: Dp,
  maxHeight: Dp,
): Paragraph {
  val multiplier = binarySearch { multiplier ->
    val paragraph = calculate(maxFontSize * multiplier)
    shouldDecreaseFontSize(
      density = density,
      intrinsics = paragraph,
      maxWidth = maxWidth,
      maxHeight = maxHeight,
    )
  }
  return calculate(maxFontSize * multiplier)
}

private fun binarySearch(
  start: Float = 0f,
  end: Float = 1f,
  threshold: Float = 1e-6f,
  predicate: (Float) -> Boolean,
): Float {
  var from = start
  var to = end
  while (to - from > threshold) {
    val center = (from + to) / 2
    if (predicate(center)) {
      to = center
    } else {
      from = center
    }
  }
  return (from + to) / 2
}

@Composable
private fun BoxWithConstraintsScope.createParagraphCalculator(
  text: String,
  style: TextStyle,
  maxLines: Int,
): ParagraphCalculator {
  val density = LocalDensity.current
  val fontFamilyResolver = LocalFontFamilyResolver.current
  val boxConstraints = constraints

  return ParagraphCalculator(
    text = text,
    style = style,
    maxLines = maxLines,
    density = density,
    fontFamilyResolver = fontFamilyResolver,
    boxConstraints = boxConstraints
  )
}

private class ParagraphCalculator(
  private val text: String,
  private val style: TextStyle,
  private val maxLines: Int,
  private val density: Density,
  private val fontFamilyResolver: FontFamily.Resolver,
  private val boxConstraints: Constraints,
) {
  fun calculate(fontSize: TextUnit): Paragraph =
    Paragraph(
      text = text,
      style = style.copy(fontSize = fontSize),
      maxLines = maxLines,
      density = density,
      fontFamilyResolver = fontFamilyResolver,
      constraints = boxConstraints
    )
}

private fun shouldDecreaseFontSize(
  density: Density,
  intrinsics: Paragraph,
  maxWidth: Dp,
  maxHeight: Dp,
): Boolean = with(density) {
  intrinsics.didExceedMaxLines ||
      intrinsics.maxLineWidth.toDp() > maxWidth ||
      intrinsics.height.toDp() > maxHeight
}

private val Paragraph.maxLineWidth: Float
  get() = (0 until lineCount).maxOf(::getLineWidth)

@Composable
private fun Paragraph.getDpSize(): DpSize =
  with(LocalDensity.current) {
    DpSize(
      width = maxLineWidth.toDp(),
      height = height.toDp()
    )
  }


private data class AutoSizeTextPreviewParameter(
  val text: String = "Sample text that should auto resize to fill the box.",
  val minFontSize: TextUnit = 1.sp,
  val maxLines: Int = Int.MAX_VALUE,
  val parentWidth: Dp = 200.dp,
  val parentHeight: Dp = 100.dp,
)

private class AutoSizeTextPreviewParameterProvider :
  PreviewParameterProvider<AutoSizeTextPreviewParameter> {
  override val values: Sequence<AutoSizeTextPreviewParameter> = AutoSizeTextPreviewParameter().run {
    sequenceOf(
      this,
      copy(maxLines = 1),
      copy(parentWidth = 120.dp, minFontSize = 20.sp),
      copy(parentHeight = 24.dp),
      copy(parentWidth = 100.dp, maxLines = 3),
      copy(text = "W", parentWidth = 32.dp, parentHeight = 32.dp),
    )
  }
}

@Preview
@Composable
private fun AutoSizePreview(
  @PreviewParameter(AutoSizeTextPreviewParameterProvider::class) param: AutoSizeTextPreviewParameter,
) {
  MaterialTheme {
    Surface {
      Box(
        Modifier.size(
          width = param.parentWidth,
          height = param.parentHeight
        )
      ) {
        AutoSizeText(
          text = param.text,
          maxLines = param.maxLines,
        )
      }
    }
  }
}
