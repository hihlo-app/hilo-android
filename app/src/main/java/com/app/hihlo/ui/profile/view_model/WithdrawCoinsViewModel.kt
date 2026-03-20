package com.app.hihlo.ui.profile.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hihlo.model.common_response.CommonResponse
import com.app.hihlo.model.contact_us.request.ContactUsRequest
import com.app.hihlo.network_call.repository.ApiRepository
import com.app.hihlo.utils.network_utils.Resources
import com.app.hihlo.utils.network_utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawCoinsViewModel @Inject constructor(): ViewModel() {
    private val withdrawCoinsLiveData = SingleLiveEvent<Resources<CommonResponse>>()

    fun getWithdrawCoinsLiveData(): LiveData<Resources<CommonResponse>> {
        return withdrawCoinsLiveData
    }
    fun hitWithdrawCoinsDataApi(token: String, coins: String) {

        try {
            withdrawCoinsLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    withdrawCoinsLiveData.postValue(
                        Resources.success(
                            ApiRepository().withdrawCoinsApi(token, coins
                            )
                        )
                    )
                } catch (ex: Exception) {
                    withdrawCoinsLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}