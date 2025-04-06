package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class GetAllMoodsByMoodTypeStatusUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(status: Int, moodType: MoodType): MoodResult<List<MoodWtQuestion>> {
        return moodRepository.fetchAllMoodsBy(
            status = status,
            moodType = moodType
        )
    }
}