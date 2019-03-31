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

A docker image for this project can be pulled from my [repository on Docker Hub](https://cloud.docker.com/u/mayankrastogi/repository/docker/mayankrastogi/chessservice):

```
docker pull mayankrastogi/chessservice:0.0.1-SNAPSHOT
```

### API Specification

// TODO

### Instructions

#### Running the Chess Service Locally

1. Clone or download this repository
2. Open Command Prompt (if on Windows) or Terminal (if on Linux/Mac) and browse to the project directory
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
6. Make a request to start a new game by sending a `POST` request to `localhost:8080/chess/new`. Check the [API specification](#api-specification) section to find out the set of parameters to pass
7. The application can be terminated by pressing the `Ctrl` + `C` hot key on the Command Prompt / Terminal

#### Building the Chess Service Virtual Appliance (VAP) using OSv and Capstan

1. Log into a Linux/Mac machine that has QEMU and KVM installed and configured
2. Open terminal and browse to the project directory
3. Install `capstan` if you don't have it already

    ```
    curl https://raw.githubusercontent.com/cloudius-systems/capstan/master/scripts/download | bash
    ```

4. Copy `openjdk8` base images from `osv-base-images` directory by running `install-images.sh` script. This script copies and extracts the base images in that directory to `~/.capstan/repositories/mayankrastogi/`.

    ```
    sudo osv-base-images/install-images.sh
    ```
    
    If you get an error regarding `\r`, please convert the line endings in that script from `CRLF` to `LF` using your favorite text editor.

5. Build the unikernal image

    ```
    sudo $HOME/bin/capstan build -v
    ```
  
6. Launch the VM instance and forward port `8080` of the VM to port `8080` of the host

    ```
    sudo $HOME/bin/capstan run -f 8080:8080
    ```

7. The VM should have launched now and our Spring Boot application should have started. Test it by making a request to start a new game by sending a `POST` request to `localhost:8080/chess/new`. Check the [API specification](#api-specification) section to find out the set of parameters to pass

#### Containerizing the Chess Service using Docker and publishing it on Docker Hub

// TODO

### AWS Deployment

#### Deploying the OSv Virtual Appliance on AWS EC2

Watch this [video on YouTube](https://youtu.be/lqg4FtTALQ4) for details on how to build the VAP and deploy it on AWS EC2:

[![How to deploy OSv unikernal Virtual Appliance on AWS EC2](https://img.youtube.com/vi/lqg4FtTALQ4/maxresdefault.jpg)](https://youtu.be/lqg4FtTALQ4) 

#### Deploying docker container on AWS EC2

// TODO
