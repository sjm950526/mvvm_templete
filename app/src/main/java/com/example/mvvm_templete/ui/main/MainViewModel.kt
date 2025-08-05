package com.example.mvvm_templete.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm_templete.common.base.BaseViewModel
import com.example.mvvm_templete.remote.Repository
import com.example.mvvm_templete.remote.data.ResponseTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mRepository: Repository
) : BaseViewModel() {

    private val _mDataList = MutableLiveData<List<ResponseTest>>()
    val mDataList: LiveData<List<ResponseTest>> = _mDataList
    private val _mData = MutableLiveData<ResponseTest>()
    val mData: LiveData<ResponseTest> = _mData

    fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            runDataLoading {
                mRepository.getList().body().let {
                    _mDataList.postValue(it)
                }
            }
        }
    }
}