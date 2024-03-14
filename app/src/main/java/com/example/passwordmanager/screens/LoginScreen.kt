package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.passwordmanager.CryptoManager


@Composable
fun LoginScreen(hash: String, onLogin: () -> Unit) {

    var password by remember { mutableStateOf("") }

    Column {
        TextField(value = password, onValueChange = { password = it })
        Button(onClick = {
            if (hash == getHashString(password))
                onLogin()
        }) {
            Text(text = "Login")
        }
    }
}


fun getHashString(message: String): String =
    CryptoManager.sha256(message.encodeToByteArray()).decodeToString()