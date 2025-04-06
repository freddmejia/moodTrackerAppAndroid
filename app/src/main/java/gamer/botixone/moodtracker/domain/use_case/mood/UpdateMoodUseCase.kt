package gamer.botixone.moodtracker.domain.use_case.mood

import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.result.MoodResult

class UpdateMoodUseCase(
    private val moodRepository: MoodRepository
) {
    suspend fun execute(intensity: Int, note: String, moodType: MoodType, id: Int): MoodResult<MoodEntity> {
        return moodRepository.updateMood(
            intensity = intensity,
            note = note,
            moodType = moodType,
            id = id
        )
    }
}