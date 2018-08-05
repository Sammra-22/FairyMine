package com.nordic.fairymine.api.gson

import android.text.InputType
import com.google.gson.Gson
import com.nordic.fairymine.api.model.User
import com.nordic.fairymine.common.form.Form
import com.nordic.fairymine.common.form.InfoRow
import com.nordic.fairymine.common.form.InfoRowType
import junit.framework.Assert
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by Sam22 on 4/3/18.
 */
class GsonFactoryTest {

    private val gson: Gson = GsonFactory.createGson()

    @Test
    fun testUserSerializer() {
        val userJson = gson.toJson(userUpstream, User::class.java)
        Assert.assertEquals(readJsonFromFile("user_upstream.json"), userJson)
    }

    @Test
    fun testUserDeserializer() {
        val userEntity = gson.fromJson(readJsonFromFile("user_downstream.json"), User::class.java)
        userEntity.subscriptionForm.infoList.forEach { infoRow ->
            val expectedInfoRow = userDownstream.subscriptionForm.infoList.first { it.key == infoRow.key }
            Assert.assertEquals(expectedInfoRow.label, infoRow.label)
            Assert.assertEquals(expectedInfoRow.infoType, infoRow.infoType)
        }
    }

    companion object {
        private val userUpstream = User(subscriptionForm = Form(arrayListOf(
                InfoRow("country", "Country", InfoRowType.SingleSelect(mapOf())).apply { value.set("SWEDEN") },
                InfoRow("age", "Age", InfoRowType.TextInput(InputType.TYPE_CLASS_NUMBER)).apply { value.set("20") }
        )))

        private val userDownstream = User(subscriptionForm = Form(arrayListOf(
                InfoRow("operator", "Operator", InfoRowType.SingleSelect(mapOf("Telenor" to "TELENOR", "Telia" to "TELIA", "Tele 2" to "TELE_2"))),
                InfoRow("gender", "Gender", InfoRowType.SingleSelect(mapOf("Male" to "MALE", "Female" to "FEMALE")))
        )))
    }

    @Throws(FileNotFoundException::class)
    private fun readJsonFromFile(fileName: String): String? {
        return try {
            val inputStream = javaClass.classLoader.getResourceAsStream(fileName)
            val data = ByteArray(inputStream.available())
            inputStream.read(data)
            String(data)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}