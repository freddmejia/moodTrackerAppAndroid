package gamer.botixone.moodtracker.domain.result

sealed class MoodResult<out T> {
    data class Success<out T>(val data: T): MoodResult<T>()
    data class Error(val exception: Throwable) : MoodResult<Nothing>()
    object ErrorEmpty : MoodResult<Nothing>()
}