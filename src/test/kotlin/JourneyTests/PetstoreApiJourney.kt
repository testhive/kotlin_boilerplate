package JourneyTests

import apiObjects.PetStoreAPI
import extensions.retry.TestWithRetry

class PetstoreApiJourney {
    private lateinit var petStoreAPI: PetStoreAPI

    @TestWithRetry
    fun createPetAndUpdate(){
        petStoreAPI = PetStoreAPI()
        val petId = petStoreAPI.createPetFromFile("petData")
        println(petId)
        println(petStoreAPI.getPetName(petId))
        petStoreAPI.updatePetCategory(petId, "Rottweiler")
        println(petStoreAPI.getPetCategory(petId))
    }
}