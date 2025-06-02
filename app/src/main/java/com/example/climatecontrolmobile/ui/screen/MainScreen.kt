package com.example.climatecontrolmobile.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.climatecontrolmobile.ui.component.BottomNavigationBar
import com.example.climatecontrolmobile.ui.component.ProfileSelectionModal
import com.example.climatecontrolmobile.ui.component.TopSystemBar
import com.example.climatecontrolmobile.ui.viewmodel.MainViewModel
import com.example.climatecontrolmobile.utils.FCMHelper
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel, onLogout: () -> Unit) {
    val sensorsWithMeasurements by viewModel.sensorsWithMeasurements.collectAsState()
    val devicesWithCommands by viewModel.devicesWithCommands.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedSystem by viewModel.selectedSystem.collectAsState()
    val systems by viewModel.systems.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val profiles by viewModel.profiles.collectAsState()
    val showProfileModal by viewModel.showProfileModal.collectAsState()    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadSystems()
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.loadSensors()
        }
    }

    Scaffold(
        topBar = {
            TopSystemBar(
                selectedSystem = selectedSystem,
                systems = systems,
                onSystemSelected = { viewModel.selectSystem(it) },                
                onChangeProfile = { viewModel.showProfileModal() },
                onLogout = onLogout
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.setCurrentTab(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    when (currentTab) {
                        MainViewModel.Tab.SENSORS -> SensorsScreen(
                            sensorsWithMeasurements = sensorsWithMeasurements,
                            onSensorClick = { sensorId -> 
                                navController.navigate("sensor_detail/$sensorId")
                            }
                        )
                        MainViewModel.Tab.DEVICES -> DevicesScreen(
                            devicesWithCommands = devicesWithCommands,
                            onDeviceClick = { deviceId -> 
                                navController.navigate("device_detail/$deviceId")
                            }
                        )
                    }
                }
            }
        }

        ProfileSelectionModal(
            isVisible = showProfileModal,
            profiles = profiles,
            currentProfileId = selectedSystem?.profileId,
            onProfileSelected = { profileId -> viewModel.changeProfile(profileId) },
            onDismiss = { viewModel.hideProfileModal() }
        )
    }
}
