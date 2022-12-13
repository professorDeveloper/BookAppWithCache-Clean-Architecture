package com.azamovhudstc.bookappwithcache.ui.screens.book

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.bookappwithcache.R
import com.azamovhudstc.bookappwithcache.data.local.database.entites.BookEntities
import com.azamovhudstc.bookappwithcache.ui.adapter.BooksAdapter
import com.azamovhudstc.bookappwithcache.utils.showSnack
import com.azamovhudstc.bookappwithcache.viewmodel.HomeScreenViewModel
import com.azamovhudstc.bookappwithcache.viewmodel.LoginViewModel
import com.azamovhudstc.bookappwithcache.viewmodel.imp.HomeScreenViewModelImp
import com.azamovhudstc.bookappwithcache.viewmodel.imp.LoginViewModelImp
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*


class BookScreen : Fragment(R.layout.fragment_home),
    BooksAdapter.ContactItemCallBack.SetLongClickListener {
    private val adapter by lazy { BooksAdapter(this) }
    private var size = 0
    private val viewModel: HomeScreenViewModel by viewModels<HomeScreenViewModelImp>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.statusLiveData.observe(this) {
            toolbar.title = it
        }
        viewModel.messageLiveData.observe(this) {
            showSnack(it)
        }
        viewModel.editBookLiveData.observe(this) {

        }
        viewModel.getAllBooksLiveData.observe(this, getAllBookObserver)
        viewModel.addBookLiveData.observe(this) {
            findNavController().navigate(R.id.addBookScreen)
        }
        viewModel.progressLiveData.observe(this, progressObserver)
    }

    private val progressObserver = Observer<Boolean> {
        if (it) progressHome.show() else progressHome.hide()
    }
    private val getAllBookObserver = Observer<List<BookEntities>> {
        adapter.submitList(it)
        size = it.size
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_book.adapter = adapter

        btnRefresh.setOnClickListener {
            viewModel.reloadData()
        }
        viewModel.status()
        viewModel.getAllBooks()
        buttonAdd.setOnClickListener {
            viewModel.addBook()
        }
    }

    override fun deleteClick(contact: BookEntities) {
        viewModel.delete(contact)
    }

    override fun showClick(contact: BookEntities) {
        var bundle = Bundle()
        bundle.putSerializable("data", contact)
        findNavController().navigate(R.id.showBookScreen,bundle)

    }

    override fun editItemClick(contact: BookEntities) {
        var bundle = Bundle()
        bundle.putSerializable("data", contact)
        findNavController().navigate(R.id.editBookScreen,bundle)
    }

    override fun likedClick(contact: BookEntities) {

    }


}