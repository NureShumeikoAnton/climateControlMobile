package com.example.climatecontrolmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.climatecontrolmobile.ui.screen.MainScreen
import com.example.climatecontrolmobile.ui.screen.SensorDetailScreen
import com.example.climatecontrolmobile.ui.screen.DeviceDetailScreen
import com.example.climatecontrolmobile.ui.theme.ClimateControlMobileTheme
import com.example.climatecontrolmobile.ui.viewmodel.MainViewModel
import com.example.climatecontrolmobile.ui.viewmodel.SensorDetailViewModel
import com.example.climatecontrolmobile.ui.viewmodel.DeviceDetailViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.climatecontrolmobile.ui.screen.AuthScreen
import com.example.climatecontrolmobile.ui.viewmodel.AuthViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Notification permission granted")
        } else {
            Log.d("MainActivity", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        askNotificationPermission()
        
        val authViewModel = AuthViewModel(applicationContext)
        val mainViewModel = MainViewModel(applicationContext)
        setContent {
            ClimateControlMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
                    
                    LaunchedEffect(isAuthenticated) {
                        if (isAuthenticated) {
                            navController.navigate("main") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "auth"
                    ) {
                        composable("auth"){
                            AuthScreen(navController = navController, authViewModel = authViewModel)
                        }
                        composable("main") {
                            MainScreen(
                                navController = navController, 
                                viewModel = mainViewModel,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("auth") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("sensor_detail/{sensorId}") { backStackEntry ->
                            val sensorId = backStackEntry.arguments?.getString("sensorId")?.toIntOrNull() ?: 0
                            val sensorDetailViewModel = SensorDetailViewModel(applicationContext, sensorId)
                            SensorDetailScreen(navController = navController, viewModel = sensorDetailViewModel)
                        }
                        composable("device_detail/{deviceId}") { backStackEntry ->
                            val deviceId = backStackEntry.arguments?.getString("deviceId")?.toIntOrNull() ?: 0
                            val deviceDetailViewModel = DeviceDetailViewModel(applicationContext, deviceId)
                            DeviceDetailScreen(navController = navController, viewModel = deviceDetailViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("MainActivity", "Notification permission already granted")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}