package com.app.hihlo.ui.profile.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hihlo.model.common_response.CommonResponse
import com.app.hihlo.model.rating_review.RatingReviewRequest
import com.app.hihlo.network_call.repository.ApiRepository
import com.app.hihlo.utils.network_utils.Resources
import com.app.hihlo.utils.network_utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateUsViewModel @Inject constructor(): ViewModel() {
    private val rateUsLiveData = SingleLiveEvent<Resources<CommonResponse>>()

    fun getRateUsLiveData(): LiveData<Resources<CommonResponse>> {
        return rateUsLiveData
    }
    fun hitRateUsDataApi(token: String, request: RatingReviewRequest) {

        try {
            rateUsLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    rateUsLiveData.postValue(
                        Resources.success(
                            ApiRepository().ratingReviewApi(token, request
                            )
                        )
                    )
                } catch (ex: Exception) {
                    rateUsLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}