# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.0.2-1.21] - 2024-08-22
### Changed
- Make `ArmorStandTickBoxScreen#name` accessible

## [v21.0.1-1.21] - 2024-08-15
### Fixed
- Fix `ClassCastException` when `AbstractArmorStandScreen::getMenu` is called, but the current holder is no instance of `ArmorStandMenu`

## [v21.0.0-1.21] - 2024-07-07
- Port to Minecraft 1.21
- Forge is no longer supported in favor of NeoForge
