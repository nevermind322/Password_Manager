package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.passwordmanager.Biometrics
import com.example.passwordmanager.CryptoManager


@Composable
fun LoginScreen(onLogin: () -> Unit) {

    var password by remember { mutableStateOf("") }

    val hash = CryptoManager.getPasswordHash(LocalContext.current.applicationContext)

    if (hash == null) {
        SignupScreen(onSignup = onLogin)
    } else
        Column {
            TextField(value = password, onValueChange = { password = it })
            Button(onClick = {
                if (hash == getHashString(password))
                    onLogin()
            }) { Text(text = "Login") }
            BiometricAuthButton(
                onError = { _, _ -> },
                onSuccess = { onLogin() },
                onFail = { /*TODO*/ })
        }
}


@Composable
fun BiometricAuthButton(
    onError: (Int, CharSequence) -> Unit,
    onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    onFail: () -> Unit,
    modifier: Modifier = Modifier
) {

    val activity = LocalContext.current as FragmentActivity
    val biometrics = Biometrics(activity)
    val biometricPrompt = biometrics.getPrompt(onError, onSuccess, onFail)

    val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password").build()

    Button(onClick = { biometricPrompt.authenticate(promptInfo) }, modifier = modifier) {
        Icon(imageVector = Icons.Default.Face, contentDescription = "")
    }
}

fun getHashString(message: String): String =
    CryptoManager.sha256(message.encodeToByteArray()).decodeToString()