package com.nordic.fairymine.api.gson

import com.google.gson.*
import com.nordic.fairymine.api.model.User
import com.nordic.fairymine.common.form.Form
import com.nordic.fairymine.common.form.InfoRow
import com.nordic.fairymine.common.form.InfoRowType
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * Created by Sam22 on 2/20/18.
 */
internal class UserConverter : JsonSerializer<User>, JsonDeserializer<User> {

    override fun serialize(src: User, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        val json = JSONObject().put(MAIL_KEY, src.mail)
        arrayOf(src.subscriptionForm, src.registrationForm).forEach { form ->
            val filtered = form.infoList.filterNot {
                it.value.get().isNullOrEmpty()
            }
            filtered.forEach { info ->
                json.put(info.key, info.value.get())
            }
        }
        return Gson().fromJson(json.toString(), JsonElement::class.java)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        val infoList = arrayListOf<InfoRow>()
        if (json != null && json.isJsonObject) {
            try {
                val jsonObj = JSONObject(Gson().toJson(json))
                jsonObj.keys().forEach { key ->
                    val fieldMap = jsonObj.getJSONObject(key)
                    val fieldLabel = fieldMap.getString(LABEL_NAME)
                    val fieldMode = fieldMap.getString(LABEL_MODE)
                    val fieldValues = fieldMap.getJSONObject(LABEL_VALUES)
                    val entriesMap = fieldValues.keys().asSequence().associateBy({ fieldValues.getString(it) }, { it })

                    val fieldInfoType = if (fieldMode == LABEL_MULTI_CHOICE) {
                        InfoRowType.MultipleSelect(entriesMap)
                    } else {
                        InfoRowType.SingleSelect(entriesMap)
                    }
                    infoList.add(InfoRow(key, fieldLabel, fieldInfoType))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return User(registrationForm = Form(infoList))
    }

    companion object {
        private const val MAIL_KEY = "mail"
        private const val LABEL_MULTI_CHOICE = "MULTIPLE"
        private const val LABEL_NAME = "label"
        private const val LABEL_MODE = "mode"
        private const val LABEL_VALUES = "values"
    }
}