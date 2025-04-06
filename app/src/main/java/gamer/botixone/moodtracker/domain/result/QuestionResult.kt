package gamer.botixone.moodtracker.domain.result

sealed class QuestionResult<out T> {
    data class Success<out T>(val data: T): QuestionResult<T>()
    data class Error(val exception: Throwable) : QuestionResult<Nothing>()
}