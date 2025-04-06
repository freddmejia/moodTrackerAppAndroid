package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class GetAllMoodsByStatusLimitOffsetUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(status: Int, limit: Int, offset: Int): MoodResult<List<MoodWtQuestion>> {
        val result = moodRepository.fetchAllMoodsBy(status, limit, offset)

        return when (result) {
            is MoodResult.Success -> {
                if (result.data.isEmpty()) MoodResult.ErrorEmpty else result
            }
            else -> result
        }
    }
}