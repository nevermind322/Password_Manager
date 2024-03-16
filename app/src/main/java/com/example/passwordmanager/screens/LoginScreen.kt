package com.example.passwordmanager.screens

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.passwordmanager.Biometrics
import com.example.passwordmanager.CryptoManager
import com.example.passwordmanager.getHashString


@Composable
fun LoginScreen(onLogin: () -> Unit) {

    var password by remember { mutableStateOf("") }

    val hash = CryptoManager.getPasswordHash(LocalContext.current.applicationContext)

    val context = LocalContext.current

    if (hash == null) {
        SignupScreen(onSignup = onLogin)
    } else
        Box(contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                PasswordField(password = password, onPasswordChange = { password = it })
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(onClick = {
                        if (hash == getHashString(password))
                            onLogin()
                    }) { Text(text = "Login") }

                    BiometricAuthButton(
                        onError = { _, _ ->
                            Toast.makeText(
                                context,
                                "Error, retry or use password",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onSuccess = { onLogin() },
                        onFail = {
                            Toast.makeText(
                                context,
                                "Error, retry or use password",
                                Toast.LENGTH_LONG
                            ).show()
                        })
                }
            }
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
    if (!biometrics.canAuthenticate())
        return
    val biometricPrompt = biometrics.getPrompt(onError, onSuccess, onFail)

    val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password").build()

    Button(onClick = { biometricPrompt.authenticate(promptInfo) }, modifier = modifier) {
        Icon(imageVector = Icons.Default.Face, contentDescription = "")
    }
}

