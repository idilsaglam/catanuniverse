# Catanuniverse

## Pre-requisites

This project developed with JDK 15 and Gradle 7.2.

## Project structure

This project is composed of 5 Java modules and a web application. Each module contains a detailed readme. For more details on a specific module please click on the module name.

- [`core`](./core): Contains all the logic of the game, including components such as `Board` or `Configuration`, which the server and local client will use.
- [`commons`](./commons): Contains all the common classes and client logic which cli and client will use.
- [`server`](./server): Contains the source code of the game server.
- [`client`](./client): Contains the source code of the JavaFX client.
- [`cli`](./cli):  Contains the source code of the command line client
- [`web`](./web): Contains the source code of the web client.


## Building

- To build the entire project:
	```shell
	gradle -q build
	```
- To build a specific module:
	```shell
	gradle -q <module_name>:build
	```

## Running

You will probably need to run modules separately.
```shell
gradle -q <module_name>:run
```
