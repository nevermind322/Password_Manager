package com.example.passwordmanager.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.passwordmanager.VisibilityIcon
import com.example.passwordmanager.VisibilityOffIcon
import com.example.passwordmanager.passwordKeyboardOptions
import com.example.passwordmanager.siteKeyboardOptions

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit) {
    var mask by remember {
        mutableStateOf(true)
    }
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        visualTransformation = if (mask) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { mask = !mask }) {
                if (!mask)
                    VisibilityIcon
                else
                    VisibilityOffIcon
            }
        },
        singleLine = true,
        keyboardOptions = passwordKeyboardOptions,
        shape = RoundedCornerShape(5),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SiteField(site: String, onSiteChange: (String) -> Unit, enabled: Boolean = true) {
    TextField(
        value = site,
        onValueChange = onSiteChange,
        singleLine = true,
        enabled = enabled,
        keyboardOptions = siteKeyboardOptions,
        shape = RoundedCornerShape(5),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}