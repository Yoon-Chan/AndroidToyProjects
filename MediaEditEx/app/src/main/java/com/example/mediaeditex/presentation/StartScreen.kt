package com.example.mediaeditex.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme

@Composable
fun StartScreenRoot(
    onClickMixingMusic: () -> Unit,
    onClickRecordingVideo: () -> Unit,
    onClickVideoCut: () -> Unit,
) {
    val context = LocalContext.current
    val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()

    val requestPermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.values.all { it }

        if(allPermissionsGranted) {
            //카메라 화면 이동
            onClickRecordingVideo()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onClickMixingMusic) {
            Text(
                text = "음향 합성하기"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if(isCheckPermissions(context, permissions)) {
                onClickRecordingVideo()
            } else {
                requestPermissions.launch(permissions)
            }
        }) {
            Text(
                text = "동영상 촬영하기"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onClickVideoCut) {
            Text(
                text = "동영상 자르기"
            )
        }
    }
}

private fun isCheckPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    MediaEditExTheme {
        StartScreenRoot(
            onClickMixingMusic = {},
            onClickRecordingVideo = {},
            onClickVideoCut = {}
        )
    }
}