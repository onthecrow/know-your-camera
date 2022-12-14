package com.onthecrow.knowyourcamera.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log
import android.util.Size
import java.util.Arrays

object CameraChecker {
    fun checkCameras(context: Context): List<CameraParams> {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val frontCameraIds = cameraManager.cameraIdList.map { cameraId ->
            with(cameraManager.getCameraCharacteristics(cameraId)) {
                CameraParams(
                    when (get(CameraCharacteristics.LENS_FACING)) {
                        CameraCharacteristics.LENS_FACING_BACK -> CameraType.BACK
                        CameraCharacteristics.LENS_FACING_FRONT -> CameraType.FRONT
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> CameraType.EXTERNAL
                        else -> CameraType.UNKNOWN
                    },
                    get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE) ?: Size(0, 0)
                )
            }
        }

        return frontCameraIds
    }

    fun getCameraCharacteristics(
        context: Context
    ): CameraCharacteristicsInternal {
        val cameraManager = context.getSystemService("camera") as CameraManager
        try {
            val cameraList = Arrays.asList(*cameraManager.cameraIdList)
            val var4: Iterator<*> = cameraList.iterator()
            while (var4.hasNext()) {
                val availableCameraId = var4.next() as String
                val availableCameraCharacteristics =
                    cameraManager.getCameraCharacteristics(availableCameraId)
                val availableLensFacing =
                    availableCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                if (availableLensFacing != null && availableLensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    val focalLength = availableCameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
                    val sensorWidth = availableCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
                    return CameraCharacteristicsInternal(
                        availableCameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.getOrNull(0),
                        availableCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)?.width,
                        focalLength != null,
                        sensorWidth != null
                    )
                }
            }
        } catch (var8: CameraAccessException) {
            Log.e("CameraXPreviewHelper", "Accessing camera ID info got error: $var8")
        }
        return CameraCharacteristicsInternal(characteristicsExists = false)
    }
}