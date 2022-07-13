package com.example.realestatemanager.data.sampleData

object SampleProperties {

    val agents = SampleAgent.getSampleAgentList()
    val photos = SamplePropertyPhotos.propertyPhotoList

    val samplePropertyList = listOf(
        com.example.realestatemanager.data.model.Property(
            0, "Apartment", 300000, 300, 10, 5, 3,"Super apartment in Paris",
            photos, "50 avenue Hoche 75017 Paris","Paris", "17eme", null, "On sale", 1657490400000,
            null, agents[0].id
        ),
        com.example.realestatemanager.data.model.Property(
            0, "House", 1000000, 400, 15, 6, 6,"Super house in Paris",
            photos, "100 avenue Hoche 75017 Paris", "Paris", "17eme", null, "On sale", 1657490400000,
            null, agents[1].id
        ),
        com.example.realestatemanager.data.model.Property(
            0, "Penthouse", 900000, 300, 5, 2, 2,"Super penthouse in Paris",
            photos, "490 avenue Hoche 75017 Paris", "Paris", "17eme",null, "On sale", 1657490400000,
            null, agents[2].id
        ),
        com.example.realestatemanager.data.model.Property(
            0, "Villa", 1200000, 367, 8, 4, 3,"Super villa in Paris",
            photos, "130 avenue Hoche 75017 Paris", "Paris", "17eme",null, "On sale", 1657490400000,
            null, agents[3].id
        ),
        com.example.realestatemanager.data.model.Property(
            0, "Studio", 300000, 40, 1, 1, 1,"Super studio in Paris",
            photos, "50 avenue Hoche 75017 Paris", "Paris", "17eme",null, "On sale", 1657490400000,
            null, agents[0].id
        )
    )
}