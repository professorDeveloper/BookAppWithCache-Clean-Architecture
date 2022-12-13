package com.azamovhudstc.bookappwithcache.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.EditBookRequest
import com.azamovhudstc.bookappwithcache.repo.impl.BookRepositoryImpl
import com.azamovhudstc.bookappwithcache.viewmodel.EditScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditScreenViewModelImpl : EditScreenViewModel, ViewModel() {
    override val successBackLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val errorLiveData: MutableLiveData<String> = MutableLiveData()
    override val progressStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val bookScreenUseCaseImp = BookRepositoryImpl()

    override fun editBook(bookRequest: BookEntities) {
        progressStatusLiveData.value = true
        var token = AppReference.getInstance().getToken()


        if (checker(bookRequest.toUpdate())) {
            progressStatusLiveData.value = false
            viewModelScope.launch(Dispatchers.IO) {
                bookScreenUseCaseImp.editBook(token!!, bookRequest.id, bookRequest.toUpdate(), bookRequest.localId)
                successBackLiveData.postValue(Unit)
            }
        } else {
            progressStatusLiveData.value = false

            errorLiveData.value = "Maydonlar Hato to`ldirilgan"
        }
    }

    private fun checker(bookRequest: EditBookRequest): Boolean {
        return bookRequest.author.length > 10 && bookRequest.description.length > 30

    }
}