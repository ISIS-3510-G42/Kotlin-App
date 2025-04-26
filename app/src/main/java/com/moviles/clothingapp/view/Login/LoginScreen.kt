package com.moviles.clothingapp.view.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.moviles.clothingapp.R
import com.moviles.clothingapp.viewmodel.LoginViewModel
import com.moviles.clothingapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, onNavigateToHome: () -> Unit,
                navController: NavHostController, weatherViewModel: WeatherViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    /* Observe if any errors occur in login */
    val signInErrorMessage by loginViewModel.signInErrorMessage.collectAsState()

    /* Launch process for dynamic PromoBanner - based on weather & measures loading time*/
    val trace: Trace = remember { FirebasePerformance.getInstance().newTrace("LoginScreen_Loading") }
    LaunchedEffect(Unit){
        trace.start()
        weatherViewModel.fetchWeatherData()
        trace.stop()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(300.dp)
        )


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Solo si hay error se muestra esto:
        signInErrorMessage?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Quitar el error despues de 500ms
            LaunchedEffect(errorMsg) {
                delay(5000)
                loginViewModel.clearSignUpError()
            }
        }

        TextButton(
            onClick = { navController.navigate("resetPassword") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Olvidaste tu contraseña?")
        }

        TextButton(
            onClick = { navController.navigate("createAccount") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Crear nueva cuenta")
        }

        Button(
            onClick = { loginViewModel.signIn(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)) // Dark green
        ) {
            Text("Iniciar Sesión", color = Color.White)
        }

        TextButton(
            onClick = { navController.navigate("createAccount") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Crear nueva cuenta")
        }
    }


    // Navigation
    val navigateToHome by loginViewModel.navigateToHome.collectAsState()
    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            onNavigateToHome()
            loginViewModel.onHomeNavigated()
        }
    }

}