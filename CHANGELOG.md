# Changelog

## [1.2.3] - 2025-09-11
### Changed
- Updated project version to 1.2.3-SNAPSHOT in pom.xml
- Updated mcpdirect-backend dependency version to 1.1.3-SNAPSHOT
- Updated mcpdirect-gateway dependency version to 1.1.2-SNAPSHOT

## [1.2.2] - 2025-09-11
### Changed
- Updated project version to 1.2.2-SNAPSHOT in pom.xml
- Updated mcpdirect-backend dependency version to 1.1.2-SNAPSHOT
- Updated mcpdirect-gateway dependency version to 1.1.1-SNAPSHOT

## [1.2.1] - 2025-09-10
### Changed
- Updated project version to 1.2.1-SNAPSHOT in pom.xml
- Updated mcpdirect-backend dependency version to 1.1.1-SNAPSHOT

## [1.2.0] - 2025-09-10
### Added
- SSL support for HSTP service hosts
- doGet method to HstpServlet for health checks
- SSLContextFactory implementation for secure connections

### Changed
- Updated project version to 1.2.0-SNAPSHOT in pom.xml
- Updated mcpdirect-gateway dependency version to 1.1.0-SNAPSHOT
- Refactored package structure for servlet classes
- Configured serviceHosts to use SSL (ssl://0.0.0.0:53100)
- Modified ServletComponentScan and ServiceScan annotations to include gateway packages
- Updated roadmap in README.md

### Fixed
- Added ServletException import to HstpServlet

## [1.1.0] - 2025-08-30
### Changed
- Updated project name from "MCPDirect Studio" to "MCPDirect Community Server" in documentation
- Updated version from 1.0.0-SNAPSHOT to 1.1.0-SNAPSHOT in pom.xml
- Updated mcpdirect-backend dependency version from 1.0.0-SNAPSHOT to 1.1.0-SNAPSHOT

## [1.0.0] - 2025-08-30
### Added
- Initial project setup
- Basic project structure with src, pom.xml
- QWEN.md with project guidelines