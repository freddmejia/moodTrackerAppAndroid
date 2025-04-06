package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class GetAllModsOfWeekUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(status: Int, weekStartTimestamp: Long): MoodResult<List<MoodWtQuestion>> {
        return moodRepository.fetchMoodsOfWeek(
            status = status,
            weekStartTimestamp = weekStartTimestamp
        )
    }
}