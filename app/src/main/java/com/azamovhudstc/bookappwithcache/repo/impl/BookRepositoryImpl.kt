package com.azamovhudstc.bookappwithcache.repo.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.azamovhudstc.bookappwithcache.data.local.database.appDatabase.AppDatabase
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.DeleteBookRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.AddBookRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.request.book.EditBookRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.response.book.BooksResponseItem
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.cilent.ApiClient
import com.azamovhudstc.bookappwithcache.repo.BookRepository
import com.azamovhudstc.bookappwithcache.utils.hasConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

class BookRepositoryImpl : BookRepository {
    var database = AppDatabase.getInstance()
    private val bookApi = ApiClient.getBookApi()
    private val mediatorLiveData = MediatorLiveData<Unit>()

    init {
        mediatorLiveData.observeForever {
        }
    }

    override  fun getAllBook(token: String): Flow<LiveData<List<BookEntities>>> = flow {
        if (hasConnection()) {
            reLoadLocalData()
        }
        emit(database.bookDao().getBooks())
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteBook(token: String, booksResponseItem: Int, id: Int) {
            if (hasConnection()) {
                bookApi.deleteBook("Bearer " + token, DeleteBookRequest(id))
                loadData(token)

            } else {
                var book = database.bookDao().getBook(booksResponseItem)
                Log.d("TTT", "deleteBook: $book")
                val data = BookEntities(
                    book.id,
                    book.author,
                    book.description,
                    false,
                    book.pageCount,
                    book.title,
                    book.localId, -1
                )

                database.bookDao().update(data)

            }
        }


    override suspend  fun addBook(token: String, bookRequest: AddBookRequest)  {
            if (hasConnection()) {
                bookApi.addBook("Bearer $token", bookRequest)
                loadData(token)
            } else {

                database.bookDao().insert(bookRequest.toAddRoom())

            }

        }

    override suspend  fun editBook(
        token: String,
        id: Int,
        bookRequest: EditBookRequest,
        localId: Int
    ) {
        if (hasConnection()) {
            bookApi.editBook("Bearer $token", bookRequest)
            loadData(token)
        } else {
            var data = database.bookDao().getBook(localId)
            var entity = BookEntities(
                data.id,
                bookRequest.author,
                bookRequest.description,
                false,
                bookRequest.pageCount,
                bookRequest.title,
                data.localId,
                2
            )
            database.bookDao().update(entity)
        }


    }


    override suspend  fun reLoadLocalData():Boolean {
        if (hasConnection()) {
            loadData(token = AppReference.getInstance().getToken()!!)
            return true
        }
        return false

    }


    override  suspend fun internetState(): Flow<Boolean> = flow {
        emit(hasConnection())
    }.flowOn(Dispatchers.IO)

    private suspend  fun loadData(token: String)  {
        //Tekshirish
        var list = database.bookDao().getAllBooks() as ArrayList<BookEntities>
        list.filter {
            it.state == 1
        }.forEach { add ->
            bookApi.addBook("Bearer $token", add.toAdd())
        }
        list.filter {
            it.state == 2
        }.forEach { update ->
            bookApi.editBook("Bearer $token", update.toUpdate())
        }
        list.filter {
            it.state == -1
        }.forEach { delete ->
            bookApi.deleteBook("Bearer $token", delete.toDelete().toDeleteRequest())
        }

        val getAllBook = bookApi.getAllBooks("Bearer $token")
        if (getAllBook.isSuccessful) {
            val body = getAllBook.body()
            database.bookDao().updateWholeDB(body?.map { it.toBookEntity() }!!)

        }
    }

}