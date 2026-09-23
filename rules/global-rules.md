# Where Am I — Code Rules

- Language: Kotlin (JDK 17 toolchain, jvmTarget 17)
- UI: Jetpack Compose + Material 3, Navigation Compose
- Architecture: MVVM + Clean Architecture, package `com.whereami`
- DI via Hilt: `@HiltViewModel`, `@Inject constructor`, modules in `data/di` (`@Module` + `@InstallIn(SingletonComponent::class)`)
- Domain layer: use cases in `domain/usecase`, models in `domain/model`, repository interfaces in `domain/repository`, session/timer logic in `domain/session` and `domain/timer` — no Android dependencies
- Data layer: Room entities/daos/database in `data/local`, repository implementations in `data/repository`, network helpers in `data/network`, geocoding/location in `data/geocoding` and `data/location`
- Core utilities (clock, location math, locale, retry): `core/`
- Presentation: ViewModels + Compose screens in `presentation/<feature>/`, reusable components in `presentation/components`, theme in `presentation/theme`
- Strings externalized in `app/src/main/res/values/strings.xml` with `values-pt-rBR` translations
- Scores/results: value classes and models live in `domain/model`; scoring logic in use cases under `domain/usecase`
- Tests: unit tests mirror the package under `app/src/test/java/com/whereami/` (JUnit4, Mockito, kotlinx-coroutines-test, Turbine)
- New files must declare `package com.whereami.<layer>` matching their target directory under `app/src/main/java/`
- Output paths in code blocks MUST start with `app/src/main/java/` for production code or `app/src/test/java/` for tests — never `src/main/kotlin/`
