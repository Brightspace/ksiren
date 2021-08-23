# KSiren
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.3.31-blue.svg)](http://kotlinlang.org)
![Build status](https://github.com/Brightspace/ksiren/actions/workflows/ci.yml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/Brightspace/ksiren/badge.svg?branch=master)](https://coveralls.io/github/Brightspace/ksiren?branch=master)
[![Download](https://api.bintray.com/packages/brightspace/ksiren/ksiren/images/download.svg) ](https://bintray.com/brightspace/ksiren/ksiren/_latestVersion)

A Kotlin library for parsing responses from Siren API endpoints and building Siren Action requests.

## Quick start

Options for getting started:

* [Download the latest release](../../releases).
* Clone the repo: `git clone https://github.com/Brightspace/ksiren.git`.
* Include the library in your project with gradle:
```
compile 'com.brightspace.ksiren:ksiren:1.1.0'
```
You must import, or create yourself, a JSON parsing plugin; adapters for moshi and gson are available and can be pulled in using one of the following gradle compile commands:
```
compile 'com.brightspace.ksiren:ksiren-moshi-adapter:1.1.0'
compile 'com.brightspace.ksiren:ksiren-gson-adapter:1.1.0'
```

Optionally, you can include, or create, a request building plugin; an adapter for okhttp3 is available:
```
compile 'com.brightspace.ksiren:ksiren-okhttp3-request-builder:1.0.0'
```

## Documentation

See [the wiki](https://github.com/Brightspace/ksiren/wiki).

## Versioning

Ksiren is maintained under [the Semantic Versioning guidelines](http://semver.org/).

## Contributing

Please read through our [contributing guidelines](CONTRIBUTING.md). Included are directions for opening issues, coding standards, and notes on development.
