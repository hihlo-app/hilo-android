package com.app.hihlo.ui.profile.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hihlo.model.faq.response.FaqResponse
import com.app.hihlo.model.following_list.response.FollowingListResponse
import com.app.hihlo.network_call.repository.ApiRepository
import com.app.hihlo.utils.network_utils.Resources
import com.app.hihlo.utils.network_utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(): ViewModel() {
    private val faqLiveData = SingleLiveEvent<Resources<FaqResponse>>()

    fun getFaqLiveData(): LiveData<Resources<FaqResponse>> {
        return faqLiveData
    }
    fun hitFaqDataApi() {

        try {
            faqLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    faqLiveData.postValue(
                        Resources.success(
                            ApiRepository().getFaqListApi(
                            )
                        )
                    )
                } catch (ex: Exception) {
                    faqLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}