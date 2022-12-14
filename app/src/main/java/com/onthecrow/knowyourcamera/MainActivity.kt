package com.onthecrow.knowyourcamera

import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.util.Size
import android.util.SizeF
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onthecrow.knowyourcamera.camera.CameraCharacteristicsInternal
import com.onthecrow.knowyourcamera.camera.CameraChecker
import com.onthecrow.knowyourcamera.camera.CameraParams
import com.onthecrow.knowyourcamera.camera.CameraType
import com.onthecrow.knowyourcamera.ui.theme.KnowYourCameraTheme

class MainActivity : ComponentActivity() {
    private val camerasState: MutableState<List<CameraParams>> = mutableStateOf(listOf())
    private val cameraCharacteristicsState: MutableState<CameraCharacteristicsInternal> =
        mutableStateOf(CameraCharacteristicsInternal())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KnowYourCameraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(camerasState, cameraCharacteristicsState)
                }
            }
        }
        camerasState.value = CameraChecker.checkCameras(this)
        cameraCharacteristicsState.value = CameraChecker.getCameraCharacteristics(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    state: State<List<CameraParams>> = mutableStateOf(listOf()),
    characteristicsState: State<CameraCharacteristicsInternal> = mutableStateOf(
        CameraCharacteristicsInternal()
    ),
    modifier: Modifier = Modifier
) {
    val characteristicsStateValue by remember { characteristicsState }
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        content = {
            item {
                Text(text = "Cameras on device:")
            }
            state.value.forEachIndexed { index, cameraParams ->
                item {
                    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
                        Text(text = "Camera index: $index")
                        Text(text = "Camera direction: ${cameraParams.cameraType.name}")
                        Text(text = "Camera sensor size: ${cameraParams.sensorSize}")
                    }
                }
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Characteristics of the frontal camera:"
                )
                if (!characteristicsStateValue.characteristicsExists) {
                    Text(text = "Can't obtain the camera characteristics!")
                }
                if (!characteristicsStateValue.focalLengthCharacteristicsExists) {
                    Text(text = "Obtained characteristics doesn't contain the focal length")
                }
                if (!characteristicsStateValue.sensorWidthCharacteristicsExists) {
                    Text(text = "Obtained characteristics doesn't contain the sensor width")
                }
                Text(text = "Focal length: ${characteristicsStateValue.focalLengthMm}, Sensor width: ${characteristicsStateValue.sensorWidthMm}")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KnowYourCameraTheme {
        val paramsState =
            mutableStateOf(
                listOf(
                    CameraParams(
                        cameraType = CameraType.FRONT,
                        sensorSize = Size(1027, 768)
                    ),
                    CameraParams(
                        cameraType = CameraType.BACK,
                        sensorSize = Size(4096, 3072)
                    ),
                )
            )

        Greeting(paramsState)
    }
}