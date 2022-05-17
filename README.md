# Arrangement Manager Extension

This project is an example on how to create a behaviour extension for arrangement-manager. 
The example involves changing `accountHolderCountry` randomly and setting `accountHolderStreetName` to "123 Fake Street".

**NOTE** make sure you create all the extended classes under `com.backbase.dbs.product package` as this is the package that Spring will scan to create the beans.

## How to use

To use your service extension, you have to include the JAR build from this project to the CLASSPATH of the service.

By adding the profile `docker-image` to the build command, the project builds and pushes a new docker image
that already contains the extension:


    ./mvnw clean build -Pdocker-image


To use the extension in other scenarios, please check the community documentation.

## Community Documentation

* [Extend the behavior of a service](https://community.backbase.com/documentation/ServiceSDK/latest/extend_service_behavior)
