# Sequence

A sequential timer for structured task execution. It orchestrates multiple **Segments** into a
single, continuous **Sequence**, eliminating manual resets between intervals.

**Note**: This project is in active development. Expect breaking changes.

## Key Features

- **Variable Segment Durations**: Define specific durations for each segment within a sequence.
- **Automated Transitions**: Move from one segment to the next without user intervention.
- **Auditory Feedback**: Distinct audio cues mark the approach of time depletion and segment
  completion.
- **Consistency Tracking**: Integrated heatmap for long-term consistency tracking.
- **Fully Offline Functionality**: Track time anywhere even without internet connection.

## Quick Start

1. Clone the project: `git clone https://github.com/swdegay/Sequence.git`.
2. Open in Android Studio.
3. Sync Gradle and run the `:app` module.

## Development Environment

Use the latest stable version of Android Studio. To use a custom JDK instead of the bundled
JetBrains Runtime, ensure your JDK is version 17 or higher and update the configuration
in [build.gradle.kts](build.gradle.kts).

## License

This project is licensed under the GNU General Public License v3. See the [LICENSE](LICENSE) file
for details.