package com.rzrasel.instagramvideodownload.presentation.screen

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.rzrasel.instagramvideodownload.presentation.viewmodel.InstagramViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InstagramDownloaderScreen(
    viewModel: InstagramViewModel = hiltViewModel()
) {
    val url by viewModel.url
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val context = LocalContext.current

    // Handle permissions for different Android versions
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        listOf(Manifest.permission.INTERNET)
    } else {
        listOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val permissionsState = rememberMultiplePermissionsState(permissions)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!permissionsState.allPermissionsGranted) {
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Request Permissions")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = url,
            onValueChange = viewModel::onUrlChange,
            label = { Text("Instagram video URL") },
            placeholder = { Text("https://www.instagram.com/reel/...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (permissionsState.allPermissionsGranted) {
                    viewModel.downloadVideoWithProxy(context, url)
                } else {
                    Toast.makeText(
                        context,
                        "Please grant all permissions first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            enabled = !isLoading && url.isNotBlank() && permissionsState.allPermissionsGranted,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp))
            else Text("Download Video")
        }
    }
}