package com.example.passwordmanager.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.CryptoManager
import com.example.passwordmanager.PASSWORD_SPECIAL_SYMBOLS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val PASSWORD_HASH_KEY = "password_hash"

class SignupViewModel : ViewModel() {

    val state = MutableStateFlow<SignupUIState>(SignupUIState.Start)

    private fun validate(password: String) = (password.length in (8..32))
            && password.contains(Regex("[0-9]"))
            && password.contains(Regex("[A-Z]"))
            && password.contains(Regex("[a-z]"))
            && password.contains(Regex("[$PASSWORD_SPECIAL_SYMBOLS]"))
            && password.matches(Regex("^([a-z]|[A-Z]|[0-9]|[$PASSWORD_SPECIAL_SYMBOLS])+$"))


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
    data object PasswordNotValid : SignupUIState()
    data object Success : SignupUIState()
}