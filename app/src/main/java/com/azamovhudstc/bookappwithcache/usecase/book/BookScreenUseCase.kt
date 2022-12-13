package com.azamovhudstc.bookappwithcache.usecase.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.AddBookRequest
import com.azamovhudstc.bookappwithcache.utils.Cases
import kotlinx.coroutines.flow.Flow

interface BookScreenUseCase {

    fun editBook(bookEntities: BookEntities)
    fun delete(deleteBookRequest: BookEntities)
    fun addBook(bookRequest: AddBookRequest)
    fun reloadData(): Boolean
    fun getAllBooks(): Flow<LiveData<List<BookEntities>>>
}