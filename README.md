## CS 441 - Engineering Distributed Objects for Cloud Computing
## Homework 4

---

### Overview

The objective of this homework was to create a RESTful web service that allows anyone to play chess with an AI opponent.

#### My Environment

The project was developed using the following environment:

- **OS:** Windows 10, Ubuntu 18.04 LTS running in VMware
- **IDE:** IntelliJ IDEA Ultimate 2018.3
- **Hypervisor:** VMware Workstation 15 Pro
- **Build System:** Gradle
- **Framework:** Spring Boot

#### Prerequisites

- [Ubuntu](https://www.ubuntu.com/) or any other Unix based OS capable of running [KVM](https://www.linux-kvm.org/page/Main_Page)
- [QEMU](https://www.qemu.org/) hypervisor installed on your system 
- [Capstan](https://github.com/cloudius-systems/capstan) installed on your system
- [Docker](https://www.docker.com/) running on your system

#### Docker Image

A docker image for this project can be pulled from my [repository on Docker Hub]():

```
docker pull mayankrastogi/chessservice:0.0.1-SNAPSHOT
```

### API Specification

// TODO

### Instructions

#### Running the Chess Service Locally

1. Clone or download this repository
2. Open Command Prompt (if on Windows) or Terminal (if on Linux/Mac)
3. Run the test cases and build a fat `jar` of the application using `Gradle`
    
    *On Windows:* 
    
    ```
    gradlew clean test bootJar
    ```
    
    *On Linux/Mac:*
    
    ```
    ./gradlew clean test bootJar
    ```
    
4. Run the Spring Boot application using the built `jar` file
    
    ```
    java -jar build/libs/chessservice-0.0.1-SNAPSHOT.jar
    ```
5. Open [Postman](https://www.getpostman.com/) or any other application which lets you send `HTTP` requests
6. Make a request to start a new game by sending a `POST` request to `localhost:8080/chess/new`. Check the API specification section below to find out the set of parameters to pass
7. The application can be terminated by pressing the `Ctrl` + `C` hot key on the Command Prompt / Terminal

#### Building the Chess Service Virtual Appliance (VAP) using OSv and Capstan

// TODO

#### Containerizing the Chess Service using Docker and publishing it on Docker Hub

// TODO

### AWS Deployment

#### Deploying the OSv Virtual Appliance on AWS EC2

Watch this video for details on how to build the VAP and deploy it on AWS EC2:

[![How to deploy OSv unikernal Virtual Appliance on AWS EC2](https://img.youtube.com/vi/lqg4FtTALQ4/maxresdefault.jpg)](https://youtu.be/lqg4FtTALQ4) 

#### Deploying docker container on AWS EC2

// TODO
