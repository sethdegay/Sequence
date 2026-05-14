# Sequence

<table>
  <tr>
    <td>
      <img src="screenshots/sc1.webp" alt="Home Screen" width="200">
    </td>
    <td>
      <img src="screenshots/sc2.webp" alt="Timer Screen" width="200">
    </td>
    <td>
      <img src="screenshots/sc3.webp" alt="Settings screen" width="200">
    </td>
    <td>
      <img src="screenshots/sc4.webp" alt="Settings screen" width="200">
    </td>
  </tr>
</table>

Sequence (stylized as SΞQUΞNCΞ) is a sequential timer for structured task execution.

In this app, individual **tasks** are configured as **segments**, and an organized group of these
segments is called a **sequence**.

## Features

- Configurable segment duration (days to seconds).
- Audio cues for completion and time depletion.
- Text-to-speech announcements for segment titles.
- Heatmap visualizing completion times.

## Quick Start

1. Clone the project: `git clone https://github.com/swdegay/Sequence.git`.
2. Open in Android Studio.
3. Sync Gradle and run the `:app` module.

## Development Environment

Use the latest stable Android Studio. If using a custom JDK, ensure it is version 17+ and configured
in [build.gradle.kts](build.gradle.kts).

## Architecture

This project employs a multi-module, feature-based structure to ensure separation of concerns and
decoupled code.

### Core Frameworks and Paradigms

* **UI:** 100% Jetpack Compose and Material 3 Expressive on a single activity
* **Navigation:** Navigation3 (api and impl submodules)
* **Persistence:** Room for relational data and Proto-DataStore for preferences/key-value pairs.
* **Dependency Injection:** Hilt
* **Concurrency:** Kotlin Coroutines and Flow

### **Data Flow and State**

```mermaid
graph LR
    X[Room] --> B[Repository]
    Y[Proto-DataStore] --> B
    B --> C[ViewModel]
    C --> D[UI State]
    D --> E[Compose UI]
    E --> F[User Events] --> C
```

To avoid further over-engineering, the UseCase layer was omitted. ViewModels consume Repositories
directly as the single source of truth and expose state via a single UI-specific StateFlow.

### Modularization

Modularization is based on features supported by shared code in the core modules. This approach also
utilizes
the [API/Implementation](https://developer.android.com/guide/navigation/navigation-3/modularize)
split to expose the least possible code to other modules. In this structure, the following
pattern is
followed:

| Module Pattern                 | Responsibility / Description                                                |
|:-------------------------------|:----------------------------------------------------------------------------|
| `:app`                         | The module that glues all other modules together                            |
| `:feature:[name]:api`          | Navigation keys (References to the feature).                                |
| `:feature:[name]:impl`         | Feature-specific logic, including UI, ViewModels, and entry providers.      |
| `:feature:[model]:[name]:api`  | Navigation keys for the model-specific feature.                             |
| `:feature:[model]:[name]:impl` | Model-specific implementation details for sub-features                      |
| `:core:[name]`                 | Reusable foundation modules (e.g., navigation, database, or design system). |

#### App Module Overview

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  :feature:sequence:contextmenu:api --> :core:navigation
  :feature:settings:api --> :core:navigation
  :core:database --> :core:common
  :core:database --> :core:model
  :feature:segment:editor:api --> :core:navigation
  :app --> :core:data
  :app --> :core:model
  :app --> :feature:home:api
  :feature:sequence:editor:api --> :core:navigation
  :core:data --> :core:database
  :core:data --> :core:datastore
  :feature:home:api --> :core:navigation
  :core:timer --> :core:model
  :core:ui --> :core:common
  :core:ui --> :core:designsystem
  :core:ui --> :core:model
  :feature:timer:api --> :core:navigation
  :core:datastore --> :core:datastore-proto
  :core:datastore --> :core:model
  :feature:license:api --> :core:navigation
  :feature:calendarevent:list:api --> :core:navigation
```

#### Feature Modules

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  :feature:segment:editor:impl --> :feature:segment:editor:api
  :feature:settings:impl --> :feature:settings:api
  :feature:settings:impl --> :feature:license:api
  :feature:home:impl --> :feature:calendarevent:list:api
  :feature:home:impl --> :feature:home:api
  :feature:home:impl --> :feature:sequence:contextmenu:api
  :feature:home:impl --> :feature:sequence:editor:api
  :feature:home:impl --> :feature:settings:api
  :feature:home:impl --> :feature:timer:api
  :feature:license:impl --> :feature:license:api
  :feature:calendarevent:list:impl --> :feature:calendarevent:list:api
  :feature:sequence:editor:impl --> :feature:segment:editor:api
  :feature:sequence:editor:impl --> :feature:sequence:editor:api
  :feature:timer:impl --> :feature:timer:api
  :feature:sequence:contextmenu:impl --> :feature:sequence:contextmenu:api
  :feature:sequence:contextmenu:impl --> :feature:sequence:editor:api
```

This graph maps the boundaries between feature implementations and their public APIs, showing how
features navigate to or reference each other exclusively through `:api` modules.

#### Core Modules

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  :core:data --> :core:database
  :core:data --> :core:datastore
  :core:database --> :core:common
  :core:database --> :core:model
  :core:timer --> :core:model
  :core:ui --> :core:common
  :core:ui --> :core:designsystem
  :core:ui --> :core:model
  :core:datastore --> :core:datastore-proto
  :core:datastore --> :core:model
```

This graph details the internal dependency tree of the foundational, non-feature modules responsible
for data persistence, business models, and shared UI styling.

## License

This project is licensed under the GNU General Public License v3. See the [LICENSE](LICENSE) file
for details.