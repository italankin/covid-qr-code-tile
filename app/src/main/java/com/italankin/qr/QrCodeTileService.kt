package com.italankin.qr

import android.graphics.Color
import android.os.PowerManager
import androidx.wear.tiles.ColorBuilders.ColorProp
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.DimensionBuilders.DpProp
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.Layout
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.ResourceBuilders.AndroidImageResourceByResId
import androidx.wear.tiles.ResourceBuilders.ImageResource
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.ListenableFuture

private const val RESOURCES_VERSION = "1"
private const val IMAGE_QR = "qr"
private const val WAKELOCK_TAG = "qrcodetile:TileWakelock"
private const val WAKELOCK_TIMEOUT = 30 * 1000L

class QrCodeTileService : TileService() {

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, WAKELOCK_TAG)
    }

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> {
        val tile = Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setFreshnessIntervalMillis(0)
            .setTimeline(
                Timeline.Builder().addTimelineEntry(
                    TimelineEntry.Builder().setLayout(
                        Layout.Builder().setRoot(
                            LayoutElementBuilders.Image.Builder()
                                .setContentScaleMode(LayoutElementBuilders.CONTENT_SCALE_MODE_FIT)
                                .setModifiers(createModifiers())
                                .setWidth(DimensionBuilders.ExpandedDimensionProp.Builder().build())
                                .setHeight(DimensionBuilders.ExpandedDimensionProp.Builder().build())
                                .setResourceId(IMAGE_QR)
                                .build()
                        ).build()
                    ).build()
                ).build()
            ).build()
        return ImmediateFuture(tile)
    }

    override fun onResourcesRequest(
        requestParams: ResourcesRequest
    ): ListenableFuture<Resources> {
        return ImmediateFuture(
            Resources.Builder()
                .addIdToImageMapping(
                    IMAGE_QR,
                    ImageResource.Builder().setAndroidResourceByResId(
                        AndroidImageResourceByResId.Builder().setResourceId(R.drawable.qr).build()
                    ).build()
                )
                .setVersion(RESOURCES_VERSION)
                .build()
        )
    }

    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        wakeLock.acquire(WAKELOCK_TIMEOUT)
    }

    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    private fun createModifiers(): ModifiersBuilders.Modifiers {
        return ModifiersBuilders.Modifiers.Builder()
            .setPadding(
                ModifiersBuilders.Padding.Builder()
                    .setAll(DpProp.Builder().setValue(32f).build())
                    .build()
            )
            .setBackground(
                ModifiersBuilders.Background.Builder()
                    .setColor(ColorProp.Builder().setArgb(Color.WHITE).build())
                    .build()
            )
            .build()
    }
}
