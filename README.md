# ShoppingList

[![Android Build & Unit Test](https://github.com/RemonShehata/ShoppingList/actions/workflows/android_build&unit_test.yml/badge.svg)](https://github.com/RemonShehata/ShoppingList/actions/workflows/android_build&unit_test.yml)

<p align="center">
ShoppingList app is a small demo application to demonstrate modern Android application tech-stacks with MVVM architecture, with emphasize on Unit & UI testing.
</p>

## Screenshots:

## Tech stack & Open-source libraries
- Minimum SDK level 28
- [Kotlin](https://kotlinlang.org/)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
- [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/index.html)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) is a dependency injection library for Android.
- [JetPack](https://developer.android.com/jetpack)
    - LiveData - Notify domain layer data to views.
    - Lifecycle - Dispose of observing data when lifecycle state changes.
    - Fragment-ktx - A set of Kotlin extensions that helps with fragment lifecycle.
    - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Allows you to more easily write code that interacts with views
    - ViewModel - UI related data holder, lifecycle aware.
    - Room Persistence - construct a database using the abstract layer.
- Architecture
    - MVVM Architecture (Model - View - ViewModel)
    - Repository pattern.
    - Clean Architecture approach.
- [Gradle Groovy](https://docs.gradle.org/current/userguide/groovy_plugin.html)
- [Detekt](https://github.com/detekt/detekt)- A static code analysis tool for the Kotlin programming language. Visit the project website for installation guides, rule descriptions, configuration options and more.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components.
- [CI/CD with github Actions](https://docs.github.com/en/actions) - Automate, customize, and execute your software development workflows.
- tasks done by CI/CD: 
  - build the project.
  - generate unit test reports.
  - run detekt static code analysis.
  - publish the app to firebase distribution everytime we push an update to a release branch. 
  - <b>Note</b>: no firebase key/credentials are published to the public repository.

## Handled scenarios:
- This application is built with reactive approach in mind. This means that everything is a stream that we listen to and change the UI based upon
