package com.azamovhudstc.bookappwithcache.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.bookappwithcache.repo.AuthRepository
import com.azamovhudstc.bookappwithcache.repo.impl.AuthRepositoryImpl
import com.azamovhudstc.bookappwithcache.usecase.auth.LoginUseCase
import com.azamovhudstc.bookappwithcache.usecase.auth.impl.LoginUseCaseImpl
import com.azamovhudstc.bookappwithcache.utils.hasConnection
import com.azamovhudstc.bookappwithcache.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModelImp : LoginViewModel, ViewModel() {
    override val errorLiveData: MutableLiveData<String> = MutableLiveData()
    override val changeButtonStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val repo: LoginUseCase = LoginUseCaseImpl(AuthRepositoryImpl())
    override val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    override val notConnectionLiveData: MutableLiveData<Unit> = MutableLiveData()

    override val openVerifyScreenLiveData: MutableLiveData<Unit> = MutableLiveData()
    override fun login(phone: String, password: String) {
        progressLiveData.value = true
        if (!hasConnection()) {
            progressLiveData.value = false
            notConnectionLiveData.value = Unit
            return
        }


        repo.login(password, phone).onEach {
            it.onSuccess {
                progressLiveData.value = false
                openVerifyScreenLiveData.value = Unit
            }
            it.onFailure {
                progressLiveData.value = false
                errorLiveData.value = it.message
            }
        }.launchIn(viewModelScope)


//        viewModelScope.launch {
//            repo.login(password,phone).flowOn(Dispatchers.Main).collect{
//                it.onSuccess {
//                    progressLiveData.value=false
//                   openVerifyScreenLiveData.value=Unit
//                }
//                it.onFailure {
//                    progressLiveData.value=false
//                    errorLiveData.value=it.message
//                }
//            }
//        }
    }


}