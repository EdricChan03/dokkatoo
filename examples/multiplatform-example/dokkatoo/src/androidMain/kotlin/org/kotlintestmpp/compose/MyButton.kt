package org.kotlintestmpp.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

/** The button type to use. */
enum class ButtonType {
  Filled, Elevated, FilledTonal, Outlined, Text
}

/**
 * Button which shows the given [text]. [onClick] will be invoked
 * when the button is clicked.
 * @param text The text to be shown
 * @param type The button type to use
 * @param onClick Lambda which is invoked when the button is clicked
 */
@Composable
fun MyButton(
  modifier: Modifier = Modifier,
  type: ButtonType = ButtonType.Text,
  text: String,
  onClick: () -> Unit,
) {
  MyButton(
    modifier = modifier,
    type = type,
    content = { Text(text) },
    onClick = onClick
  )
}

/**
 * Button which shows the given [text]. [onClick] will be invoked
 * when the button is clicked.
 * @param text The text to be shown
 * @param type The button type to use
 * @param onClick Lambda which is invoked when the button is clicked
 */
@Composable
fun MyButton(
  modifier: Modifier = Modifier,
  type: ButtonType = ButtonType.Text,
  text: AnnotatedString,
  onClick: () -> Unit,
) {
  MyButton(
    modifier = modifier,
    type = type,
    content = { Text(text) },
    onClick = onClick
  )
}

/**
 * Button which shows the given [text]. [onClick] will be invoked
 * when the button is clicked.
 * @param type The button type to use
 * @param text The text to be shown
 * @param imageVector The icon to be displayed next to the text
 * @param iconContentDescription Content description for the [imageVector]
 * @param onClick Lambda which is invoked when the button is clicked
 */
@Composable
fun MyButton(
  modifier: Modifier = Modifier,
  type: ButtonType = ButtonType.Text,
  text: AnnotatedString,
  imageVector: ImageVector,
  iconContentDescription: String,
  onClick: () -> Unit
) {
  MyButton(
    modifier = modifier,
    type = type,
    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
    content = {
      Icon(
        modifier = Modifier.size(ButtonDefaults.IconSize),
        imageVector = imageVector,
        contentDescription = iconContentDescription
      )
      Spacer(Modifier.size(ButtonDefaults.IconSpacing))
      Text(text)
    },
    onClick = onClick
  )
}

/**
 * Button which shows the given [content]. [onClick] will be invoked
 * when the button is clicked.
 * @param type The button type to use
 * @param content The content to be shown
 * @param onClick Lambda which is invoked when the button is clicked
 */
@Composable
fun MyButton(
  modifier: Modifier = Modifier,
  type: ButtonType = ButtonType.Text,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable RowScope.() -> Unit,
  onClick: () -> Unit,
) {
  when (type) {
    ButtonType.Filled      -> Button(
      modifier = modifier,
      contentPadding = contentPadding,
      onClick = onClick,
      content = content
    )

    ButtonType.Elevated    -> ElevatedButton(
      modifier = modifier,
      contentPadding = contentPadding,
      onClick = onClick,
      content = content
    )

    ButtonType.FilledTonal -> FilledTonalButton(
      modifier = modifier,
      contentPadding = contentPadding,
      onClick = onClick,
      content = content
    )

    ButtonType.Outlined    -> OutlinedButton(
      modifier = modifier,
      contentPadding = contentPadding,
      onClick = onClick,
      content = content
    )

    ButtonType.Text        -> TextButton(
      modifier = modifier,
      contentPadding = contentPadding,
      onClick = onClick,
      content = content
    )
  }
}
