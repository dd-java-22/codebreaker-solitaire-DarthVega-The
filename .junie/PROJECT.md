# Project Standards

This project is a Codebreaker game implementation for Android using Hilt for dependency injection.

### Build Configuration
- Android projects use `Gradle` with Kotlin DSL (`build.gradle.kts`).
- `Hilt` is used for Dependency Injection.
- Ensure all Hilt-enabled classes are annotated with `@AndroidEntryPoint`.

### Package Structure
- The root package for the app is `edu.cnm.deepdive.codebreaker.app`.
- Packages follow standard Android architectural patterns:
    - `controller` for Activities and Fragments.
    - `viewmodel` for ViewModels.
    - `model` for data models and entities.
    - `service` for API clients and repositories.

### Database and API
- API specification is in `api/spec/codebreaker.yaml`.
- ERD is in `work/erd.md`.
