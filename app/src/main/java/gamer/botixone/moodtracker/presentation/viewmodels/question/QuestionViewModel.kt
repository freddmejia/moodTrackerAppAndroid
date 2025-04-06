package gamer.botixone.moodtracker.presentation.viewmodels.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity
import gamer.botixone.moodtracker.domain.result.QuestionResult

import gamer.botixone.moodtracker.domain.use_case.question.CreateQuestionUseCase
import gamer.botixone.moodtracker.domain.use_case.question.FetchQuestionsByStatusUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val fetchQuestionsByStatusUseCase: FetchQuestionsByStatusUseCase
) : ViewModel() {
    private val _questions = MutableLiveData<QuestionResult<List<QuestionEntity>>>()
    val questions: LiveData<QuestionResult<List<QuestionEntity>>> = _questions

    fun loadQuestionsBy(status: Int) = viewModelScope.launch {
        _questions.value = fetchQuestionsByStatusUseCase.execute(status = status)
    }

    fun createQuestion(questionEntity: QuestionEntity) = viewModelScope.launch {
        createQuestionUseCase.execute(questionEntity = questionEntity)
    }
}
