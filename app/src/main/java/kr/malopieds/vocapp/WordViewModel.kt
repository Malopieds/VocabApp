package kr.malopieds.vocapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.malopieds.vocapp.models.Word
import javax.inject.Inject

@HiltViewModel
public class WordViewModel @Inject constructor(
    private val repo: WordRepository
):ViewModel() {
    val words = repo.getWord()

    var openDialog by mutableStateOf(false)

    fun addWord(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repo.addWord(word)
    }

    fun deleteWord(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteWord(word)
    }

    fun openDialog() {
        openDialog = true
    }

    fun closeDialog() {
        openDialog = false
    }
}