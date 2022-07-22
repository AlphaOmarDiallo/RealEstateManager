# RealEstateManager

Project 9 - Android developer

This repository contains an application for project 9 of the path **Grande École du Numérique**. 

## Identification of the project and the mission

Name and nature: 
Building a app to help a real estate business to  provide information to the real estate agents on the field.

Origin: 
Getting the information online and offline via an app.

Challenge: 
make it as simple and as eficient as possible.

Tech stack used:
* Kotlin
* DAGGER2/HILT
* Coroutines
* LiveData
* Room persistence library
* Compose
* XML
* Firebase
  * Firebase Auth
  * Firebase Cloud Firestore
* REST API with Retrofit
* Google:
  * Maps API
  * Places API
* GIT

## Project setup

This is an Android application, it is coded in Kotlin and runs on SDK version 32. To run he project, clone this repository and open it on Android Studio. 

## Project architecture

MVVM is the architecture pattern used for this project. Repositories are implemented and dependency injection is done with Dagger2 / Hilt.
This is a single Activity application in order to use Jetpack Navigation and simplify passign data between the fragments.
Some UI uses jetpack-compose.

## Version Control

We loosely use the "Git flow" approach: master is the release branch - it should always be releasable, and only merged into when we have tested and verified that everything works and is good to go. 

Daily development is done in the development branch depending on the feature being built. Features, bugfixes and other tasks are done as branches off of develop, then merged back into develop directly or via pull requests.

Keep commit clear and self-explanatory. Clean messy branches before merge. 

## Testing

This application is using Hilt, most of the test are in the Android Test folder where we test the behaviour of the complete app. 

## How to improve this project

* Firebase Auth to securise the back-end

You can either clone the repository and freely reuse it or you can make a pull request. It will only be accepted once I validate my retraining. 
