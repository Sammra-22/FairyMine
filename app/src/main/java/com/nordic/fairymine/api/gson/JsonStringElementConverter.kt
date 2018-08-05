package com.nordic.fairymine.api.gson
import com.google.gson.*
import com.nordic.fairymine.common.utils.KotLog
import java.lang.reflect.Type

/**
 * Created by Sam22 on 2/20/18.
 */
internal abstract class JsonStringElementConverter<T> : JsonDeserializer<T>, JsonSerializer<T> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T? {
        return try {
            valueOf(json.asString)
        } catch (e: IllegalArgumentException) {
            KotLog.d(TAG, "Failed to deserialize ${javaClass.simpleName} from JSON: ${json.asString}")
            null
        }
    }

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(stringValue(src))
    }

    protected abstract fun valueOf(string: String): T

    protected abstract fun stringValue(source: T): String

    companion object {
        private val TAG = JsonStringElementConverter::class.java.name
    }
}