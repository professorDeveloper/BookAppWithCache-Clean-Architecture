package com.azamovhudstc.bookappwithcache.viewmodel.imp

import android.util.Log
import androidx.lifecycle.*
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.repo.impl.BookRepositoryImpl
import com.azamovhudstc.bookappwithcache.usecase.book.imp.BookScreenUseCaseImp
import com.azamovhudstc.bookappwithcache.utils.hasConnection
import com.azamovhudstc.bookappwithcache.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeScreenViewModelImp : HomeScreenViewModel, ViewModel() {
    override val addBookLiveData: MediatorLiveData<Unit> = MediatorLiveData()
    override val messageLiveData: MediatorLiveData<String> = MediatorLiveData()
    override val statusLiveData: MutableLiveData<String> = MediatorLiveData()
    private val bookScreenUseCaseImp = BookScreenUseCaseImp(BookRepositoryImpl(),this)
    override var getAllBooksLiveData: MutableLiveData<List<BookEntities>> = MutableLiveData()
    override val editBookLiveData: MediatorLiveData<BookEntities> = MediatorLiveData()
    override val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var viewLiveData: LiveData<List<BookEntities>> = MutableLiveData()

    init {
        getAllBooks()
    }

    override fun editBook(bookEntities: BookEntities) {
    }

    override fun status() {
        viewModelScope.launch {
            if (hasConnection()) statusLiveData.value = "Online"
            else statusLiveData.value =
                "Offline"
        }
    }

    override fun delete(deleteBookRequest: BookEntities) {
        viewModelScope.launch(Dispatchers.IO) {
            bookScreenUseCaseImp.delete(deleteBookRequest)

        }
    }

    override fun addBook() {
        addBookLiveData.value = Unit
    }

    override fun reloadData() {
        try {
            if (hasConnection()) {
                viewModelScope.launch(Dispatchers.IO) {
                    bookScreenUseCaseImp.reloadData()
                }
            } else {
                messageLiveData.value = "Iltimos Internetni yoqing !"
            }
            status()
        } catch (e: SocketTimeoutException) {
            Log.d("!@#", "reloadData: ${e.message}")
        }

    }

    override fun getAllBooks() {
        progressLiveData.value = true

        viewModelScope.launch(Dispatchers.Main) {
            bookScreenUseCaseImp.getAllBooks().onEach { it ->
                editBookLiveData.addSource(it) {
                    getAllBooksLiveData.value = it

                }
            }.launchIn(viewModelScope)
            progressLiveData.value = false

        }
    }
}