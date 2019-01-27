package apiObjects

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import java.util.*

class PetStoreAPI {
    private var petDetails: JsonObject? = null
    private var lastPetId: Int? = null

    fun createPetFromFile(fileName: String): Int {
        val fileContent = PetStoreAPI::class.java.getResourceAsStream("/api/$fileName.json")
        val jsonObject = Parser().parse(fileContent) as JsonObject

        val id = (1000000..9999999).random()

        jsonObject.obj("category")!!["id"] = id
        jsonObject["id"] = id

        val (request, response, result) = "https://petstore.swagger.io/v2/pet/"
                .httpPost()
                .body(jsonObject.toJsonString())
                .header(mapOf(
                        Pair("Content-Type", "application/json"),
                        Pair("accept", "application/json")
                )).responseString()

        assert(response.statusCode == 200)
        val jsonString = StringBuilder(result.get())
        val pet = Parser().parse(jsonString) as JsonObject
        petDetails = pet
        lastPetId = id

        return id
    }

    fun getPetDetails(petId: Int): JsonObject? {
        if(lastPetId != null && lastPetId == petId ){
            return petDetails
        }
        else{
            val (request, response, result) = "https://petstore.swagger.io/v2/pet/$petId"
                    .httpGet()
                    .header(mapOf(
                            Pair("accept", "application/json")
                    )).responseString()

            assert(response.statusCode == 200)

            val jsonString = StringBuilder(result.get())
            val pet = Parser().parse(jsonString) as JsonObject
            petDetails = pet
            return pet
        }
    }

    fun getPetName(petId: Int): String? {
        getPetDetails(petId)
        return petDetails!!.string("name")
    }

    fun updatePetCategory(petId: Int, newCategory: String){
        var newJson = petDetails
        newJson!!.obj("category")!!["name"] = newCategory

        val (request, response, result) = "https://petstore.swagger.io/v2/pet/"
                .httpPut()
                .body(newJson.toJsonString())
                .header(mapOf(
                        Pair("Content-Type", "application/json"),
                        Pair("accept", "application/json")
                )).responseString()

        assert(response.statusCode == 200)
        val jsonString = StringBuilder(result.get())
        val pet = Parser().parse(jsonString) as JsonObject
        petDetails = pet
        lastPetId = petId
    }

    fun getPetCategory(petId: Int): String? {
        getPetDetails(petId)
        return petDetails!!.obj("category")!!.string("name")
    }
}

fun Request.getResultString(): String {
    return this.responseString().third.get()
}

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start