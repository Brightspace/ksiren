# KSiren
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.1.3--2-blue.svg)](http://kotlinlang.org)
![Build status](https://travis-ci.com/Brightspace/ksiren.svg?token=bx5yfkuXAPjvTyLvsLn4&branch=master)
[![Coverage Status](https://coveralls.io/repos/github/Brightspace/ksiren/badge.svg?branch=master&t=tP7WjH)](https://coveralls.io/github/Brightspace/ksiren?branch=master)

A Kotlin library for parsing responses from Siren API endpoints and building Siren Action requests.

## Quick start

Options for getting started:

* [Download the latest release](../../releases).
* Clone the repo: `git clone https://github.com/Brightspace/ksiren.git`.
* Include the library in your project with Gradle:
```
compile 'com.brightspace.ksiren:ksiren:1.0.0''
```
You must import, or create yourself, a JSON parsing plugin; adapters for moshi and gson are available and can be pulled in using one of the following Gradle compile commands:
```
compile 'com.brightspace.ksiren:ksiren-moshi-adapter:1.0.0'
compile 'com.brightspace:ksiren.gson-adapter:0.2' //outdated
```

Optionally, you can include, or create, a request building plugin; an adapter for okhttp3 is available:
```
compile 'com.brightspace.ksiren:ksiren-okhttp3-request-builder:1.0.0'
```

The above artifacts are available in the following repository

## Documentation

See [the wiki](https://github.com/Brightspace/ksiren/wiki).

## Versioning

Ksiren is maintained under [the Semantic Versioning guidelines](http://semver.org/).

## Contributing

Please read through our [contributing guidelines](CONTRIBUTING.md). Included are directions for opening issues, coding standards, and notes on development.
