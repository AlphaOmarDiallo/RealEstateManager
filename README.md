# RealEstateManager

Project number of the Android developer program at Openclassrooms. 
This repository contains an application for project 9 of the path **Grande École du Numérique**. 

## Identification of the project and the mission

Name and nature: 
Building a app to help a real estate agents to be more efficient when they are not at the office.  

Requirements: 
* The data must persist when the device does not have any connection to internet, a SQlite database must be implemented.
* Other apps should be able to access part of the data of your app via a content provider.
* UI must follow the latest guidlines.

Challenge: 
Make it as simple and as eficient as possible.

Tech stack used:
* Language: Kotlin
* Dependency injection: DAGGER2 / HILT
* Asynchronous tasks: Coroutines, LiveData
* SQLite database: Room persistence library
* UI: Compose, XML
* Backend: Firebase
* REST API: Retrofit, Okhttp 
* REST API with Retrofit
* API's: Maps, Places, Autocomplete, Abstract currency
* Versionning: GIT

## Project setup

This is an Android application, written in Kotlin and runs on SDK version 21. To run the project, clone this repository and open it on Android Studio. 

## Project architecture

MVVM is the architecture pattern used for this project. Repositories are implemented and dependency injection is done with Dagger2 / Hilt.
This is a single Activity application using Jetpack Navigation and simplify passign data between the fragments.
Some UI uses jetpack-compose.

## Version Control

We use the "Git flow" approach: main is the release branch - it should always be releasable, and only merged into when we have tested and verified that everything works and is good to go. 

Daily development is done in the development branch depending on the feature being built. Features, bugfixes and other tasks are done as branches off of develop, then merged back into develop directly or via pull requests.

Keep commit clear and self-explanatory. Clean messy branches before merge. 

## Testing

This application is using Hilt, most of the test are in the Android Test folder where we test the behaviour of the complete app. 

## How to improve this project

* Firebase Auth to securise the back-end

You can either clone the repository and freely reuse it or you can make a pull request.
