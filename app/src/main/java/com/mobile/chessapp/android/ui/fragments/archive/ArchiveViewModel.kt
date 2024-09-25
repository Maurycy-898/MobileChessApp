package com.mobile.chessapp.android.ui.fragments.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ArchiveViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is archive Fragment"
    }
    val text: LiveData<String> = _text
}
