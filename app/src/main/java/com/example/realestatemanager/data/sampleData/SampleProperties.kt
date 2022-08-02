package com.example.realestatemanager.data.sampleData

object SampleProperties {

    val agents = SampleAgent.getSampleAgentList()
    val photos = SamplePropertyPhotos.propertyPhotoList

    val samplePropertyList = listOf(
        com.example.realestatemanager.data.model.Property(
            0,
            "Apartment",
            300000,
            300,
            10,
            5,
            3,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at turpis ante. Duis laoreet dignissim nulla, finibus condimentum urna pulvinar non. Vestibulum maximus, leo et porta ullamcorper, nisl justo rhoncus nibh, eget euismod nulla turpis dapibus eros. Vestibulum a neque pharetra, dapibus neque ut, dictum odio. Quisque mattis, mi ultricies ultrices imperdiet, lorem odio varius sapien, at placerat odio leo id tortor. Aliquam gravida commodo lacus, sit amet ultrices est scelerisque quis. Donec sit amet cursus eros, in iaculis risus. Mauris vel metus id elit placerat imperdiet. Suspendisse at porttitor libero. Praesent malesuada nunc diam, a mollis metus malesuada a. Pellentesque ac tincidunt metus. Sed sit amet sagittis ante, in congue lorem. Nullam dignissim iaculis mollis. Vestibulum sodales justo non efficitur ultrices. Donec in congue elit.",
            photos,
            "50 avenue Hoche 75017 Paris",
            "Paris",
            "17eme district",
            false,
            1657490400000,
            null,
            agents[0].id,
            false,
            true,
            true,
            true
        ),
        com.example.realestatemanager.data.model.Property(
            0,
            "House",
            1000000,
            400,
            15,
            6,
            6,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at turpis ante. Duis laoreet dignissim nulla, finibus condimentum urna pulvinar non. Vestibulum maximus, leo et porta ullamcorper, nisl justo rhoncus nibh, eget euismod nulla turpis dapibus eros. Vestibulum a neque pharetra, dapibus neque ut, dictum odio. Quisque mattis, mi ultricies ultrices imperdiet, lorem odio varius sapien, at placerat odio leo id tortor. Aliquam gravida commodo lacus, sit amet ultrices est scelerisque quis. Donec sit amet cursus eros, in iaculis risus. Mauris vel metus id elit placerat imperdiet. Suspendisse at porttitor libero. Praesent malesuada nunc diam, a mollis metus malesuada a. Pellentesque ac tincidunt metus. Sed sit amet sagittis ante, in congue lorem. Nullam dignissim iaculis mollis. Vestibulum sodales justo non efficitur ultrices. Donec in congue elit.",
            photos,
            "100 avenue Hoche 75017 Paris",
            "Paris",
            "17eme",
            false,
            1657490400000,
            null,
            agents[0].id,
            false,
            true,
            true,
            true
        ),
        com.example.realestatemanager.data.model.Property(
            0,
            "Penthouse",
            900000,
            300,
            5,
            2,
            2,
            "Super penthouse in Paris",
            photos,
            "490 avenue Hoche 75017 Paris",
            "Paris",
            "17eme",
            false,
            1657490400000,
            null,
            agents[0].id,
            false,
            true,
            true,
            true
        ),
        com.example.realestatemanager.data.model.Property(
            0,
            "Villa",
            1200000,
            367,
            8,
            4,
            3,
            "Super villa in Paris",
            photos,
            "130 avenue Hoche 75017 Paris",
            "Paris",
            "17eme",
            false,
            1657490400000,
            null,
            agents[0].id,
            true,
            false,
            true,
            true
        ),
        com.example.realestatemanager.data.model.Property(
            0, "Studio", 300000, 40, 1, 1, 1, "Super studio in Paris",
            photos, "50 avenue Hoche 75017 Paris", "Paris", "17eme", false,
            1657490400000,
            null,
            agents[0].id,
            true,
            true,
            true,
            false
        )
    )
}