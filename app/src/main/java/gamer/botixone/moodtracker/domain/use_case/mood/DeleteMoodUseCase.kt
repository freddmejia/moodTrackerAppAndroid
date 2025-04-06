package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class DeleteMoodUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(status: Int, id: Int): MoodResult<Boolean> {
        return moodRepository.deleteMood(
            status = status,
            id = id
        )
    }
}