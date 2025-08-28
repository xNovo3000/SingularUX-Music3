package org.singularux.music.data.playbackstate.position

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class PositionSerializer : Serializer<Position> {

    override val defaultValue: Position = Position.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Position {
        return Position.parseFrom(input)
    }

    override suspend fun writeTo(t: Position, output: OutputStream) {
        t.writeTo(output)
    }

}