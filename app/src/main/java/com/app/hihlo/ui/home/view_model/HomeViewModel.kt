package com.app.hihlo.ui.home.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hihlo.model.common_response.CommonResponse
import com.app.hihlo.model.add_story.request.AddStoryRequest
import com.app.hihlo.model.gender_list.GenderListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.app.hihlo.utils.network_utils.Resources
import com.app.hihlo.utils.network_utils.SingleLiveEvent
import com.app.hihlo.model.home.response.HomeResponse
import com.app.hihlo.network_call.repository.ApiRepository

@HiltViewModel
class HomeViewModel @Inject constructor():ViewModel() {

    private val homeLiveDate = SingleLiveEvent<Resources<HomeResponse>>()
    private val genderListLiveData = SingleLiveEvent<Resources<GenderListResponse>>()
    fun getGenderLiveData(): LiveData<Resources<GenderListResponse>> {
        return genderListLiveData
    }

    fun getHomeLiveData(): LiveData<Resources<HomeResponse>> {
        return homeLiveDate
    }

    fun hitGenderListApi() {
        try {
            genderListLiveData.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    genderListLiveData.postValue(
                        Resources.success(
                            ApiRepository().getGenderListApi()
                        )
                    )
                } catch (ex: Exception) {
                    genderListLiveData.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun hitHomeDataApi(token: String, page: String,
                      limit: String, genderId: String) {

        try {
            homeLiveDate.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    homeLiveDate.postValue(
                        Resources.success(
                            ApiRepository().getHomeDataApi(token, page, limit, genderId
                            )
                        )
                    )
                } catch (ex: Exception) {
                    homeLiveDate.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    private val addStoryLiveDate = SingleLiveEvent<Resources<CommonResponse>>()

    fun addStoryLiveData(): LiveData<Resources<CommonResponse>> {
        return addStoryLiveDate
    }
    fun hitAddStoryDataApi(token: String, request:AddStoryRequest) {

        try {
            addStoryLiveDate.postValue(Resources.loading(null))
            viewModelScope.launch {
                try {
                    addStoryLiveDate.postValue(
                        Resources.success(
                            ApiRepository().addStoryApi(token, request
                            )
                        )
                    )
                } catch (ex: Exception) {
                    addStoryLiveDate.postValue(Resources.error(ex.localizedMessage, null))

                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }



}