package com.example.passwordmanager.vm

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.CryptoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val PASSWORD_HASH_KEY = "password_hash"

class SignupViewModel : ViewModel() {

    val state = MutableStateFlow<SignupUIState>(SignupUIState.Start)

    private fun validate(password: String) = (password.length in (8..32))
            && password.contains(Regex("[0-9]"))
            && password.contains(Regex("[A-Z]"))
            && password.contains(Regex("[a-z]"))
            && password.contains(Regex("[_&?%*]"))


    fun createMasterPassword(password: String, context: Context) {
        viewModelScope.launch {
            state.value = SignupUIState.Start
            if (validate(password)) {
                CryptoManager.createPasswordHash(context, password)
                state.value = SignupUIState.Success
            } else
                state.value = SignupUIState.PasswordNotValid
        }
    }

    fun notValidPasswordMessageShown() {
        viewModelScope.launch {
            state.value = SignupUIState.Start
        }
    }
}


sealed class SignupUIState {
    data object Start : SignupUIState()
    data object Loading : SignupUIState()
    data object PasswordNotValid : SignupUIState()
    data object Success : SignupUIState()
}