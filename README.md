# Rick and Morty Character Viewer

## Instructions for building and running

1. Clone the repository
2. Open in Android Studio
3. Run the app

## Architectural choices

- **Clean Architecture** with 3 layers: Data, Domain, Presentation
- **MVVM** pattern with ViewModels managing UI state
- **Jetpack Compose** for UI
- **Hilt** for dependency injection
- **Retrofit** for API calls
- **Repository pattern** for data access

## Assumptions and decisions

- Load 20 characters per page (API default)
- Infinite scroll pagination instead of page buttons
- Search with 500ms debounce to reduce API calls
- Pass character data through navigation arguments
- Simple error handling with retry functionality
- No offline caching (network-only approach)
