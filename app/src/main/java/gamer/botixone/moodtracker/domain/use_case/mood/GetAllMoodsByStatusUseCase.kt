package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class GetAllMoodsByStatusUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(status: Int): MoodResult<List<MoodWtQuestion>> {
        return moodRepository.fetchAllMoodsBy(
            status = status
        )
    }
}