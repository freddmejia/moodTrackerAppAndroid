package gamer.botixone.moodtracker.data.local_datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.MoodType
import gamer.botixone.moodtracker.domain.model.MoodWtQuestion

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createMood(moodEntity: MoodEntity): Long

    @Query("update moodentity set intensity = :intensity, " +
            "note = :note, moodType = :moodType where id = :id")
    fun updateMood(intensity: Int, note: String, moodType: MoodType, id: Int)

    @Transaction
    @Query("select * from moodentity where status = :status")
    fun fetchAllMoodsBy(status: Int): List<MoodWtQuestion>

    @Transaction
    @Query("SELECT * FROM moodentity WHERE status = :status ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun fetchAllMoodsBy(status: Int, limit: Int, offset: Int): List<MoodWtQuestion>

    @Transaction
    @Query("""
        SELECT * FROM moodentity
        WHERE status = :status AND timestamp >= :weekStartTimestamp
        ORDER BY timestamp DESC
    """)
    fun fetchMoodsOfWeek(status: Int, weekStartTimestamp: Long): List<MoodWtQuestion>

    @Transaction
    @Query("select * from moodentity where moodType = :moodType and status = :status")
    fun fetchAllMoodsBy(moodType: MoodType, status: Int): List<MoodWtQuestion>

    @Query("update moodentity set status = :status where id = :id")
    fun deleteMood(status: Int, id: Int)

    @Query("select * from moodentity where id = :id")
    fun fetchMoodBy(id: Int): MoodEntity?
}