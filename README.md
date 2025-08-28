Pokémon App 🎮
A clean and modern Android app that displays Pokémon data using the PokéAPI. Built with Jetpack Compose and following MVVM architecture patterns.

What it does
Browse through a paginated list of Pokémon (20 at a time)

Tap any Pokémon to see detailed stats, abilities, and high-quality artwork

Pull down to refresh the list

Smooth infinite scrolling that loads more Pokémon automatically

Tech Stack
Kotlin with Jetpack Compose for modern UI

MVVM architecture for clean code separation

Retrofit for API calls to PokéAPI

Coil for image loading

Koin for dependency injection

Coroutines + Flow for handling async operations

Project Structure
text
├── model/          # Data classes and API interface
├── viewmodel/      # Business logic and state management  
├── view/           # Compose UI screens
├── di/             # Dependency injection setup
└── MainActivity.kt # App entry point
Key Features
List Screen: Displays Pokémon with names, IDs, and images

Detail Screen: Shows comprehensive info including stats, types, abilities, height/weight

Pagination: Loads 20 Pokémon at a time with smooth scrolling

Error Handling: Retry buttons and user-friendly error messages

Loading States: Progress indicators while fetching data

API Integration
Uses PokéAPI endpoints:

GET /pokemon?limit=20&offset=0 - Pokemon list

GET /pokemon/{id} - Individual Pokemon details

Requirements
Minimum Android 7.0 (API 24)

Internet connection for API calls

Setup
Clone the repo

Open in Android Studio

Let Gradle sync complete

Run on device or emulator

What I Learned
This project helped me understand modern Android development patterns, especially working with Compose for UI, handling API responses with proper error states, and structuring code with MVVM architecture.

The pagination implementation was particularly interesting - detecting when users scroll near the end and automatically loading more content while showing appropriate loading indicators.
