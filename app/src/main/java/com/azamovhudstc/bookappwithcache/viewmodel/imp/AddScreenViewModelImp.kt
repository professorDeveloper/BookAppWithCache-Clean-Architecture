package com.azamovhudstc.bookappwithcache.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.AddBookRequest
import com.azamovhudstc.bookappwithcache.repo.impl.BookRepositoryImpl
import com.azamovhudstc.bookappwithcache.usecase.book.imp.BookScreenUseCaseImp
import com.azamovhudstc.bookappwithcache.viewmodel.AddScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddScreenViewModelImp : AddScreenViewModel, ViewModel() {
    override val successBackLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val errorLiveData: MutableLiveData<String> = MutableLiveData()
    override val progressStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val bookScreenUseCase = BookRepositoryImpl()


    override fun addBook(bookRequest: AddBookRequest) {
        progressStatusLiveData.value = true
        var token = AppReference.getInstance().getToken()
        if (checker(bookRequest)) {
            progressStatusLiveData.value = false
            viewModelScope.launch(Dispatchers.IO) {
                bookScreenUseCase.addBook(AppReference.getInstance().getToken()!!, bookRequest)
                    .let {

                    }
                successBackLiveData.postValue(Unit)
            }
        } else {
            progressStatusLiveData.value = false

            errorLiveData.value = "Maydonlar Hato to`ldirilgan"
        }
    }

    private fun checker(bookRequest: AddBookRequest): Boolean {
        return bookRequest.author.length > 10 && bookRequest.description.length > 30

    }
}