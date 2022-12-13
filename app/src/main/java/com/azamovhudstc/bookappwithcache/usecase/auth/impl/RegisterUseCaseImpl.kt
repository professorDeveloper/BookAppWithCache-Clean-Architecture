package com.azamovhudstc.bookappwithcache.usecase.auth.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.bookappwithcache.repo.impl.AuthRepositoryImpl
import com.azamovhudstc.bookappwithcache.usecase.auth.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterUseCaseImpl(var authRepositoryImpl: AuthRepositoryImpl, var viewModel: ViewModel) :
    RegisterUseCase {
    override fun register(
        name: String,
        password: String,
        phone: String,
        lastName: String
    ): Boolean {
        var boolean = false
        if (check(name, password)) {
            boolean = true
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                boolean = authRepositoryImpl.register(name, password, phone, lastName).first()
            }
        }
        return boolean
    }

    override fun check(name: String, password: String): Boolean =
        (name.length > 3 && password.length > 3)


}