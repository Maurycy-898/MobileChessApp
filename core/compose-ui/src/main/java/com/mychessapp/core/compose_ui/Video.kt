package com.mychessapp.core.compose_ui

import android.content.Context
import android.net.Uri
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
fun Video(
  videoUri: Uri,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val exoPlayer = remember { context.buildExoPlayer(videoUri) }
  AndroidView(
    modifier = modifier,
    factory = { it.buildPlayerView(exoPlayer) }
  )
  DisposableEffect(Unit) {
    onDispose(exoPlayer::release)
  }
}

@OptIn(UnstableApi::class)
private fun Context.buildExoPlayer(
  videoUri: Uri,
): ExoPlayer = ExoPlayer.Builder(this)
  .build()
  .apply {
    setMediaItem(MediaItem.fromUri(videoUri))
    repeatMode = REPEAT_MODE_ALL
    playWhenReady = true
    prepare()
  }


@OptIn(UnstableApi::class)
private fun Context.buildPlayerView(
  exoPlayer: ExoPlayer,
): PlayerView = PlayerView(this).apply {
  player = exoPlayer
  layoutParams = FrameLayout.LayoutParams(
    FrameLayout.LayoutParams.MATCH_PARENT,
    FrameLayout.LayoutParams.MATCH_PARENT,
  )
  resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
  useController = false
}

@Composable
fun videoResourceUri(
  @RawRes resourceId: Int,
): Uri = with(LocalContext.current) {
  "android.resource://$packageName/$resourceId".toUri()
}

@Preview
@Composable
private fun VideoPreview() {
  Video(
    modifier = Modifier.fillMaxSize(),
    videoUri = videoResourceUri(R.raw.chess4k)
  )
}
