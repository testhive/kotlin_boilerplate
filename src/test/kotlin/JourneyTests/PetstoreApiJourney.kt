package JourneyTests

import apiObjects.PetStoreAPI
import org.junit.jupiter.api.Test

class PetstoreApiJourney {
    private lateinit var petStoreAPI: PetStoreAPI

    @Test
    fun createPetAndUpdate(){
        petStoreAPI = PetStoreAPI()
        val petId = petStoreAPI.createPetFromFile("petData")
        println(petId)
        println(petStoreAPI.getPetName(petId))
        petStoreAPI.updatePetCategory(petId, "Rottweiler")
        println(petStoreAPI.getPetCategory(petId))
    }
}