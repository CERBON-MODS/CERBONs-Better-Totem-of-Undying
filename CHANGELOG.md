# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.1] - 2025-03-07

### Fixed

- Fix custom effects not saving

## [2.1.0] - 2025-03-02

### Added

- Add config option to make totem infinity if enchanted with Infinity effect
- Add config option to change the render offset of the Totem when used in the charm slot
- Add compatibility with [Inventorio](https://www.curseforge.com/minecraft/mc-mods/inventorio-forge) mod

### Changed

- Can add custom effects from UI
- The UI of blacklists is improved.
- Config file is now Json5
- Totem can't destroy is now a config

### Fixed

- Fix mod crashing because of custom effects
- Fix crash in Fabric servers

## [2.0.1] - 2024-01-29

### Fixed

- Should hopefully fix a mixin crash at startup

## [2.0.0] - 2024-01-29

### Added

- Compatibility with Fabric and NeoForge

### Changed

- Move project to multiloader
- Mod requires cloth config
- Mod requires CerbonsAPI 1.1.0+ 

### Removed

- Food increment feature

## [1.2.0] - 2023-12-31

### Changed

- Mod depends on [CerbonsApi](https://www.curseforge.com/minecraft/mc-mods/cerbons-api-forge/files/4992038) now
- Improved void protection ability (Player does not receive slow falling effect anymore)
- Reorganized code

## [1.1.0] - 2023-09-02

### Added

- You can now add custom effects to the totem

## [1.0.4] - 2023-07-26

### Changed

- Improved code to ensure better compatibility between other mods and to follow Mixin standards

## [1.0.3] - 2023-07-13

### Changed

- Change set health config min value to 1
- Add @Unique annotation to LivingEntityMixin to avoid conflict with other mods

## [1.0.2] - 2023-07-07

### Changed

- Update to forge 47.1.0

## [1.0.1] - 2023-06-30

### Changed

- Update forge to 1.20.1 to keep receiving the most recent updates (1.20.x)

## [1.0.0] - 2023-06-25

- Initial release