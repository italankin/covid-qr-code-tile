# COVID QR Code Tile

A simple WearOS tile which shows COVID vaccination QR Code.

![](screenshot.png)

## Build

1. Retrieve QR Code (preferable black on white square image with a little bit of padding)
2. Place QR code image to `app/src/main/res/drawable-nodpi/qr.*` (in any image format supported by Android)
3. Assemble the app:

```shell
./gradlew :app:assembleRelease # or :app:assembleDebug, if you don't want to bother with signing  
```

5. Sign the app (if required)
6. Install the app
7. Add a tile on your watch
