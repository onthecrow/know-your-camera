package com.onthecrow.knowyourcamera.camera

import android.util.Size

class CameraParams(
    val cameraType: CameraType,
    val sensorSize: Size
)

class CameraCharacteristicsInternal(
    val focalLengthMm: Float? = 0f,
    val sensorWidthMm: Float? = 0f,
    val focalLengthCharacteristicsExists: Boolean = true,
    val sensorWidthCharacteristicsExists: Boolean = true,
    val characteristicsExists: Boolean = true,
)

enum class CameraType {
    BACK, FRONT, EXTERNAL, UNKNOWN
}