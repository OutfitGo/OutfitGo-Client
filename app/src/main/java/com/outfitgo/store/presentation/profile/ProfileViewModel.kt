package com.outfitgo.store.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.usecase.auth.GetSavedUserTokenUseCase
import com.outfitgo.store.domain.usecase.auth.LogoutUseCase
import com.outfitgo.store.domain.usecase.profile.GetUserByAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSavedUserTokenUseCase: GetSavedUserTokenUseCase,
    private val getUserByAccessTokenUseCase: GetUserByAccessTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Logout -> logout()
            ProfileIntent.LoadProfile -> loadProfile()
            else -> Unit
        }
    }

    private fun loadProfile() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val token = getSavedUserTokenUseCase.execute()
                val user = getUserByAccessTokenUseCase.execute(token)
                if (user != null) {
                    _state.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            isAuthenticated = true
                        )
                    }
                }
            } catch (exp: Exception) {
                _effect.emit(ProfileEffect.SendSnackBar("${exp.message}"))
                _state.update { it.copy(isLoading = false, isAuthenticated = false) }
            }
        }
    }

    private fun logout() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                logoutUseCase.execute()
                _state.update { it.copy(isLoading = false, isAuthenticated = false) }
                _effect.emit(ProfileEffect.LogoutSuccess)
            } catch (exp: MissingUserTokenException) {
                _effect.emit(ProfileEffect.SendSnackBar("${exp.message}"))
                Log.i(TAG, "logout: ${exp.message}")
                _state.update { it.copy(isLoading = false) }
            } catch (exp: Exception) {
                _effect.emit(ProfileEffect.SendSnackBar("${exp.message}"))
                Log.e(TAG, "logout: ", exp)
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


}