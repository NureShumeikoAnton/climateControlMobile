package com.example.climatecontrolmobile.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.climatecontrolmobile.ui.viewmodel.AuthViewModel
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val authError by authViewModel.authError.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate("main")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Authentication") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        decorationBox = { innerTextField ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                if (email.isEmpty()) Text("Enter login", color = LocalContentColor.current.copy(alpha = 0.5f))
                                innerTextField()
                            }
                        }
                    )

                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        decorationBox = { innerTextField ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                if (password.isEmpty()) Text("Enter password", color = LocalContentColor.current.copy(alpha = 0.5f))
                                innerTextField()
                            }
                        }
                    )

                    Button(
                        onClick = { authViewModel.authenticate(email, password) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Login")
                    }

                    if (authError != null) {
                        Text(
                            text = authError!!,
                            color = androidx.compose.ui.graphics.Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}