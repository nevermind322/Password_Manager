package com.example.passwordmanager.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

@Composable
fun AddSiteScreen(prefs: SharedPreferences, onAdded: () -> Unit, siteParam: String? = null) {

    var site by remember { mutableStateOf(siteParam ?: "") }
    var password by remember { mutableStateOf("") }
    var buttonEnabled by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SiteField(
            site = siteParam ?: site,
            onSiteChange = { site = it },
            enabled = siteParam == null
        )
        PasswordField(password = password, onPasswordChange = { password = it })
        Button(
            onClick = {
                prefs.edit { putString(site, password) }
                buttonEnabled = false
                keyboardController?.hide()
                onAdded()
            },
            enabled = buttonEnabled
        ) {
            Text(text = "Save")
        }

    }
}