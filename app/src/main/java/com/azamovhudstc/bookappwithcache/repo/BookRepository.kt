package com.azamovhudstc.bookappwithcache.repo

import androidx.lifecycle.LiveData
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.AddBookRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.EditBookRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.response.book.BooksResponseItem
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBook(token: String): Flow<LiveData<List<BookEntities>>>
    suspend fun deleteBook(token: String, booksResponseItem: Int, id:Int)
    suspend fun addBook(token: String, bookRequest: AddBookRequest)
    suspend fun editBook(token: String, id: Int, requestData: EditBookRequest, localId: Int)

    suspend fun reLoadLocalData():Boolean
    suspend fun internetState(): Flow<Boolean>

}