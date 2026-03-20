package com.app.hihlo.ui.profile.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hihlo.model.common_response.CommonResponse
import com.app.hihlo.network_call.repository.ApiRepository
import com.app.hihlo.utils.network_utils.Resources
import com.app.hihlo.utils.network_utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(): ViewModel() {
    private val logoutUserLiveData = SingleLiveEvent<Resources<CommonResponse>>()

    fun getLogoutUserLiveData(): LiveData<Resources<CommonResponse>> {
        return logoutUserLiveData
    }
    fun hitLogoutUserDataApi(token: String) {

        try {
            logoutUserLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    logoutUserLiveData.postValue(
                        Resources.success(
                            ApiRepository().logoutUserApi(token
                            )
                        )
                    )
                } catch (ex: Exception) {
                    logoutUserLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}