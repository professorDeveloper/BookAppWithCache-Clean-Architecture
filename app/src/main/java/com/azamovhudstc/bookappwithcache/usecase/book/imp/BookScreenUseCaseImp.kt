package com.azamovhudstc.bookappwithcache.usecase.book.imp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.AddBookRequest
import com.azamovhudstc.bookappwithcache.repo.impl.AuthRepositoryImpl
import com.azamovhudstc.bookappwithcache.repo.impl.BookRepositoryImpl
import com.azamovhudstc.bookappwithcache.usecase.book.BookScreenUseCase
import com.azamovhudstc.bookappwithcache.utils.Cases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookScreenUseCaseImp(var bookRepositoryImpl: BookRepositoryImpl, var viewModel: ViewModel) :
    BookScreenUseCase {
    override fun editBook(bookEntities: BookEntities) {
        var unit = Unit
        viewModel.viewModelScope.launch {
            unit = bookRepositoryImpl.editBook(
                AppReference.getInstance().getToken()!!,
                bookEntities.id,
                bookEntities.toUpdate(),
                bookEntities.localId
            )
        }
        return unit

    }


    override fun delete(deleteBookRequest: BookEntities) {
        var unit = Unit
        viewModel.viewModelScope.launch {

            unit = bookRepositoryImpl.deleteBook(
                AppReference.getInstance().getToken()!!,
                deleteBookRequest.localId,
                deleteBookRequest.id
            )
        }
        return unit
    }

    override fun addBook(bookRequest: AddBookRequest) {
        var unit = Unit

        viewModel.viewModelScope.launch {
            unit = bookRepositoryImpl.addBook(AppReference.getInstance().getToken()!!, bookRequest)
        }
        return unit
    }

    override fun reloadData(): Boolean {
        var boolean = false
        viewModel.viewModelScope.launch {
            boolean = bookRepositoryImpl.reLoadLocalData()
        }
        return boolean

    }

    override fun getAllBooks(): Flow<LiveData<List<BookEntities>>> {
        return bookRepositoryImpl.getAllBook(AppReference.getInstance().getToken()!!)
    }


}