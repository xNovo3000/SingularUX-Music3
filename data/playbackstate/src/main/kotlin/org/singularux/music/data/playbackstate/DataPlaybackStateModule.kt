package org.singularux.music.data.playbackstate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.position.PositionSerializer
import org.singularux.music.data.playbackstate.timeline.TimelineDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataPlaybackStateModule {

    @Provides
    @Singleton
    fun providesTimelineDatabase(@ApplicationContext context: Context): TimelineDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TimelineDatabase::class.java,
            name = "timeline.sql"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPositionDataStore(@ApplicationContext context: Context): DataStore<Position> {
        return DataStoreFactory.create(
            serializer = PositionSerializer(),
            produceFile = { context.dataStoreFile(fileName = "position.proto") }
        )
    }

}