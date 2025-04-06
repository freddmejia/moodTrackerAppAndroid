package gamer.botixone.moodtracker.domain.result

sealed class CreateMoodUIState {
    object Idle : CreateMoodUIState()
    //object Loading : CreateMoodUIState()
    object Success : CreateMoodUIState()
    object Error: CreateMoodUIState()
}
