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
import androidx.core.content.edit

@Composable
fun AddSiteScreen(prefs: SharedPreferences) {

    var site by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var buttonEnabled by remember { mutableStateOf(true) }


    Column {
        TextField(value = site, onValueChange = { site = it })
        TextField(value = password, onValueChange = { password = it })
        Button(
            onClick = {
                prefs.edit { putString(site, password) }
                buttonEnabled = false
            },
            enabled = buttonEnabled
        ) {
            Text(text = "Save")
        }

    }
}