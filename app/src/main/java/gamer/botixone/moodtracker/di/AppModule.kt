package gamer.botixone.moodtracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gamer.botixone.moodtracker.data.local_datasource.dao.MoodDao
import gamer.botixone.moodtracker.data.local_datasource.dao.QuestionDao
import gamer.botixone.moodtracker.data.local_datasource.db.DataModelDatabase
import gamer.botixone.moodtracker.data.local_datasource.implementation.mood.MoodRepositoryImpl
import gamer.botixone.moodtracker.data.local_datasource.implementation.question.QuestionRepositoryImpl
import gamer.botixone.moodtracker.domain.repository.mood.MoodRepository
import gamer.botixone.moodtracker.domain.repository.question.QuestionRepository
import gamer.botixone.moodtracker.domain.use_case.mood.CreateMoodUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.DeleteMoodUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllModsOfWeekUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByMoodTypeStatusUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByStatusLimitOffsetUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.GetAllMoodsByStatusUseCase
import gamer.botixone.moodtracker.domain.use_case.mood.UpdateMoodUseCase
import gamer.botixone.moodtracker.domain.use_case.question.CreateQuestionUseCase
import gamer.botixone.moodtracker.domain.use_case.question.FetchQuestionsByStatusUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideMoodRepository(moodDao: MoodDao): MoodRepository {
        return MoodRepositoryImpl(moodDao = moodDao)
    }

    @Singleton
    @Provides
    fun provideCreateMoodUseCase(moodRepository: MoodRepository): CreateMoodUseCase {
        return CreateMoodUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateMoodUseCase(moodRepository: MoodRepository): UpdateMoodUseCase {
        return UpdateMoodUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteMoodUseCase(moodRepository: MoodRepository): DeleteMoodUseCase {
        return DeleteMoodUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllMoodsByMoodTypeStatusUseCase(moodRepository: MoodRepository): GetAllMoodsByMoodTypeStatusUseCase {
        return GetAllMoodsByMoodTypeStatusUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllMoodsByStatusUseCase(moodRepository: MoodRepository): GetAllMoodsByStatusUseCase {
        return GetAllMoodsByStatusUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllMoodsByStatusLimitOffsetUseCase(moodRepository: MoodRepository): GetAllMoodsByStatusLimitOffsetUseCase {
        return GetAllMoodsByStatusLimitOffsetUseCase(moodRepository = moodRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllModsOfWeekUseCase(moodRepository: MoodRepository): GetAllModsOfWeekUseCase {
        return GetAllModsOfWeekUseCase(moodRepository = moodRepository)
    }


    //questions
    @Singleton
    @Provides
    fun provideQuestionRepository(questionDao: QuestionDao): QuestionRepository {
        return QuestionRepositoryImpl(questionDao = questionDao)
    }
    @Singleton
    @Provides
    fun provideCreateQuestionUseCase(questionRepository: QuestionRepository): CreateQuestionUseCase {
        return CreateQuestionUseCase(questionRepository = questionRepository)
    }

    @Singleton
    @Provides
    fun provideFetchQuestionsByStatusUseCase(questionRepository: QuestionRepository): FetchQuestionsByStatusUseCase {
        return FetchQuestionsByStatusUseCase(questionRepository = questionRepository)
    }



    //Db, dao's
    @Singleton
    @Provides
    fun provideDataModelDatabase(@ApplicationContext appContext: Context) = DataModelDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideQuestionDao(db: DataModelDatabase) = db.questionDao()

    @Singleton
    @Provides
    fun provideMoodDao(db: DataModelDatabase) = db.moodDao()


}