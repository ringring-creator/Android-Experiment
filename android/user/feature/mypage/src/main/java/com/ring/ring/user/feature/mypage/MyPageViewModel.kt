package com.ring.ring.user.feature.mypage

import androidx.lifecycle.ViewModel
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.model.UserNetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
    private val localDataSource: UserLocalDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow(defaultMyPageUiState())
    val uiState = _uiState.asStateFlow()

    private fun defaultMyPageUiState(): MyPageUiState {
        return MyPageUiState(
            email = MyPageUiState.Email("", isError = false, isShowSupportingText = false),
            password = MyPageUiState.Password("", isError = false),
        )
    }

    fun setEmail(email: String) {

    }

    fun setPassword(password: String) {

    }

    fun edit() {
    }

    fun withdrawal() {

    }
}