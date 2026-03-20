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
class ContactUsViewModel @Inject constructor(): ViewModel() {
    private val contactUsLiveData = SingleLiveEvent<Resources<CommonResponse>>()

    fun getContactUsLiveData(): LiveData<Resources<CommonResponse>> {
        return contactUsLiveData
    }
    fun hitContactUsDataApi(token: String, request: ContactUsRequest) {

        try {
            contactUsLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    contactUsLiveData.postValue(
                        Resources.success(
                            ApiRepository().contactUsApi(token, request
                            )
                        )
                    )
                } catch (ex: Exception) {
                    contactUsLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}