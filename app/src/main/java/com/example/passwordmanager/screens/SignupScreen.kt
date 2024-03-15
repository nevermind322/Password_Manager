package com.example.passwordmanager.screens

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.CryptoManager
import com.example.passwordmanager.vm.SignupUIState
import com.example.passwordmanager.vm.SignupViewModel

@Composable
fun SignupScreen(
    onSignup: () -> Unit,
    vm: SignupViewModel = viewModel()
) {

    var password by remember {
        mutableStateOf("")
    }

    var mask by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current.applicationContext
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is SignupUIState.PasswordNotValid) {
            Toast.makeText(context, "Password is not valid", Toast.LENGTH_SHORT).show()
            vm.notValidPasswordMessageShown()
        } else if (state is SignupUIState.Success) {
            Toast.makeText(context, "Successfully created password", Toast.LENGTH_LONG).show()
            onSignup()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = if (mask) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable { mask = !mask }
                )
            }
        )
        Button(onClick = { vm.createMasterPassword(password, context) }) {
            Text("Create password")
        }
        Text(text = "State - $state")
    }
}