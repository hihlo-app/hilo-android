package com.app.hihlo.ui.calling.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class CallState(
    val isOngoing: Boolean,
    val userName: String = "",
    val profileUrl: String = "",
    val callStartTime: Long = 0L,
    val incomingType: String = ""

)

class CallStateViewModel : ViewModel() {
    private val _callState = MutableLiveData<CallState>()
    val callState: LiveData<CallState> = _callState

    fun updateCallState(state: CallState) {
        _callState.postValue(state)
    }
}
