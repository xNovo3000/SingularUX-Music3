package org.singularux.music.data.playbackstate.position

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

object PositionSerializer : Serializer<Position> {

    private const val TAG = "PositionSerializer"

    override val defaultValue: Position
        get() = Position(
            index = 0,
            positionMs = 0
        )

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): Position {
        return try {
            Json.decodeFromStream(
                deserializer = Position.serializer(),
                stream = input
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing WeatherPreferences", e)
            defaultValue
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: Position, output: OutputStream) {
        try {
            Json.encodeToStream(
                serializer = Position.serializer(),
                value = t,
                stream = output
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error serializing WeatherPreferences", e)
        }
    }

}