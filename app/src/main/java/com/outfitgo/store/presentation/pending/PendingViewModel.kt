package com.outfitgo.store.presentation.pending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.auth.RegisterNewUserUseCase
import com.outfitgo.store.domain.usecase.auth.SendVerificationEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PendingViewModel"

@HiltViewModel
class PendingViewModel @Inject constructor(
    private val registerNewUserUseCase: RegisterNewUserUseCase,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PendingState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PendingEffect>()
    val effect = _effect.asSharedFlow()

    // called once to get data from navigation into the viewmodel
    fun setDate(email: String, password: String, firstName: String, lastName: String) {
        _state.update { it.copy(email = email, password = password, firstName = firstName, lastName = lastName) }
    }

    fun processIntent(intent: PendingIntent) {
        when (intent) {
            is PendingIntent.ReSendVerificationEmail -> sendVerificationEmail()

            is PendingIntent.RegisterUser -> register()
        }
    }

    private fun sendVerificationEmail() {
        viewModelScope.launch {
            try {
                sendVerificationEmailUseCase.execute(_state.value.email, _state.value.password)
                _effect.emit(PendingEffect.SendSnackbar("Sent Email for ${_state.value.email}"))
            } catch (exp: Exception) {
                _effect.emit(PendingEffect.SendSnackbar(exp.message ?: "ERROR While sending email"))
                Log.e(TAG, "sendVerificationEmail: ", exp)
            }
        }
    }

    private fun register() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val user = registerNewUserUseCase.execute(
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    email = _state.value.email,
                    password = _state.value.password
                )
                if (user != null) { // success
                    _effect.emit(PendingEffect.GoToHome)
                    Log.i(TAG, "register: successfully added user ${user.firstname}")
                    Log.i(TAG, "register: id: ${user.id}")
                } else { // failure
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(PendingEffect.SendSnackbar("Failure"))
                }
            } catch (exp: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(PendingEffect.SendSnackbar(exp.message ?: "Exception"))
                Log.e(TAG, "register: ", exp)
            }
        }
    }

}