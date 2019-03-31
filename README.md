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

#### Start a new game

- HTTP Method: `POST`
- End Point: `/chess/new`
- Parameters:
    
```
playerName      Your name.
playerColor     The color you wish to play with (BLACK | WHITE).
opponentAILevel Level of AI you wish to play against (1 | 2).
```

- Sample Response:

```
{
    "gameID": "cc809856-dfed-4cc5-8790-59708d5829d0",
    "clientMove": null,
    "serverMove": {
        "fromSquare": "d2",
        "toSquare": "d4",
        "promotionPiece": null
    },
    "status": {
        "hasGameEnded": false,
        "status": "WAITING_FOR_OPPONENT",
        "fen": "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1",
        "pgn": "[Event \"Game\"]\n[Date \"2019.3.30\"]\n[White \"Chess AI\"]\n[Black \"Mayank\"]\n\n1. d2-d4 ",
        "winner": null,
        "moves": [
            {
                "fromSquare": "d2",
                "toSquare": "d4",
                "promotionPiece": null
            }
        ]
    }
}
```

#### Make a Move

- HTTP Method: `PUT`
- End Point: `/chess/{gameID}/new`
- Parameters:
    
```
(Path parameter) gameID:   The game on which the move is to be made.
(Request Body)             Description of the move. The promotionPiece should be supplied when a move is made that results in a pawn reaching the opponent's end of the chess board.
```
    
- Sample Request Body:

```
{
   "fromSquare": "e7",
   "toSquare": "e8",
   "promotionPiece": "QUEEN"
}
```

- Sample Response:

```
{
    "gameID": "6e89bc43-672f-44e4-ab6f-843183b7a792",
    "clientMove": {
        "fromSquare": "e7",
        "toSquare": "e6",
        "promotionPiece": null
    },
    "serverMove": {
        "fromSquare": "a2",
        "toSquare": "a4",
        "promotionPiece": null
    },
    "status": {
        "hasGameEnded": false,
        "status": "WAITING_FOR_OPPONENT",
        "fen": "rnbqkbnr/pppp1ppp/4p3/8/P7/2N5/1PPPPPPP/R1BQKBNR b KQkq a3 0 2",
        "pgn": "[Event \"Game\"]\n[Date \"2019.3.31\"]\n[White \"Chess AI\"]\n[Black \"Mayank\"]\n\n1. Nb1-c3 e7-e6 2. a2-a4 ",
        "winner": null,
        "moves": [
            {
                "fromSquare": "b1",
                "toSquare": "c3",
                "promotionPiece": null
            },
            {
                "fromSquare": "e7",
                "toSquare": "e6",
                "promotionPiece": null
            },
            {
                "fromSquare": "a2",
                "toSquare": "a4",
                "promotionPiece": null
            }
        ]
    }
}
```

#### View Game Status

- HTTP Method: `GET`
- End Point: `/chess/{gameID}`
- Parameters:
    
```
(Path Parameter) gameID: The ID of the chess game whose state is to be fetched.
```

- Sample Response:

```
{
    "hasGameEnded": false,
    "status": "WAITING_FOR_OPPONENT",
    "fen": "rnbqkbnr/pppp1ppp/4p3/8/P7/2N5/1PPPPPPP/R1BQKBNR b KQkq a3 0 2",
    "pgn": "[Event \"Game\"]\n[Date \"2019.3.31\"]\n[White \"Chess AI\"]\n[Black \"Mayank\"]\n\n1. Nb1-c3 e7-e6 2. a2-a4 ",
    "winner": null,
    "moves": [
        {
            "fromSquare": "b1",
            "toSquare": "c3",
            "promotionPiece": null
        },
        {
            "fromSquare": "e7",
            "toSquare": "e6",
            "promotionPiece": null
        },
        {
            "fromSquare": "a2",
            "toSquare": "a4",
            "promotionPiece": null
        }
    ]
}
```

#### Play against another instance

- HTTP Method: `POST`
- End Point: `/play`
- Parameters:
    
```
serverURL           URL of the chess service.
serverPlayerAILevel AI level of the server player.
playerName          The name by which this AI player should be identified.
playerColor         The color to use for this AI player.
playerAILevel       The AI level of this player.
maxMoves            Maximum number of moves before the game is considered to be over. Default: 100.
```

- Sample Response:

```
{
    "playerName": "client",
    "playerColor": "BLACK",
    "playerAILevel": 2,
    "serverPlayerName": "Chess AI",
    "serverPlayerColor": "WHITE",
    "serverPlayerAILevel": 1,
    "serverURL": "http://localhost:8080",
    "outcome": {
        "hasGameEnded": false,
        "status": "WAITING_FOR_OPPONENT",
        "fen": "rnbqkb1r/p1pppppp/8/1p6/5P2/3P1PPN/PPP5/RNBQKB1R b KQkq - 0 6",
        "pgn": "[Event \"Game\"]\n[Date \"2019.3.31\"]\n[White \"Chess AI\"]\n[Black \"client\"]\n\n1. h2-h4 b7-b5 2. f2-f4 Ng8-h6 3. Ng1-h3 Nh6-f5 4. d2-d3 Nf5xh4 5. g2-g3 Nh4-f3+ 6. e2xf3 ",
        "winner": null,
        "moves": [
            {
                "fromSquare": "h2",
                "toSquare": "h4",
                "promotionPiece": null
            },
            {
                "fromSquare": "b7",
                "toSquare": "b5",
                "promotionPiece": null
            },
            {
                "fromSquare": "f2",
                "toSquare": "f4",
                "promotionPiece": null
            },
            {
                "fromSquare": "g8",
                "toSquare": "h6",
                "promotionPiece": null
            },
            {
                "fromSquare": "g1",
                "toSquare": "h3",
                "promotionPiece": null
            },
            {
                "fromSquare": "h6",
                "toSquare": "f5",
                "promotionPiece": null
            },
            {
                "fromSquare": "d2",
                "toSquare": "d3",
                "promotionPiece": null
            },
            {
                "fromSquare": "f5",
                "toSquare": "h4",
                "promotionPiece": null
            },
            {
                "fromSquare": "g2",
                "toSquare": "g3",
                "promotionPiece": null
            },
            {
                "fromSquare": "h4",
                "toSquare": "f3",
                "promotionPiece": null
            },
            {
                "fromSquare": "e2",
                "toSquare": "f3",
                "promotionPiece": null
            }
        ]
    }
}
```

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
3. Install [Capstan](https://github.com/cloudius-systems/capstan) if you don't have it already

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

1. Ensure that docker is installed and running
2. Open Command Prompt or Terminal from where you can issue docker commands and browse to the project directory
3. Build the docker image

    ```
    docker build -t chessservice .
    ```

4. Run the docker image and forward port `8080` of the container to port `8080` of the host

    ```
    docker run -p 8080:8080 chessservice
    ```

5. Our Spring Boot application should have started. Test it by making a request to start a new game by sending a `POST` request to `localhost:8080/chess/new`. Check the [API specification](#api-specification) section to find out the set of parameters to pass

6. Stop the application by pressing `Ctrl` + `C`
7. Login to your docker hub account. Enter your credentials when prompted:

    ```
    docker login
    ```

8. Tag the docker image

    ```
    docker tag chessservice mayankrastogi/chessservice:0.0.1-SNAPSHOT
    ```    

9. Push the image to the Docker Hub repository

    ```
    docker push mayankrastogi/chessservice
    ```

### AWS Deployment

#### Deploying the OSv Virtual Appliance on AWS EC2

Watch this [video on YouTube](https://youtu.be/lqg4FtTALQ4) for details on how to build the VAP and deploy it on AWS EC2:

[![How to deploy OSv unikernal Virtual Appliance on AWS EC2](https://img.youtube.com/vi/lqg4FtTALQ4/maxresdefault.jpg)](https://youtu.be/lqg4FtTALQ4) 

#### Deploying docker container on AWS EC2

1. Log in to your AWS Console
2. Search for **"EC2"** from the *Find Services* search box
3. Click on **Launch Instance**

    ![01](https://mrasto3.people.uic.edu/cs441/hw4/01.PNG)

4. Select ***Amazon Linux AMI 2018.03.0 (HVM), SSD Volume Type** - ami-0cd3dfa4e37921605*

    ![02](https://mrasto3.people.uic.edu/cs441/hw4/02.PNG)
    
5. Choose the **t2.micro** as your instance type and click **Next: Configure Instance Details**

    ![03](https://mrasto3.people.uic.edu/cs441/hw4/03.PNG)

6. Keep the default settings on this page and select **Next: Add Storage**
7. Keep the default settings on this page and select **Next: Add Tags**
8. Keep the default settings on this page and select **Next: Configure Security Group**
9. Create a new security group and **Add Rule** of type *Custom TCP Rule*, specify *Port Range* as `8080`, and click **Next: Review and Launch**
    
    ![04](https://mrasto3.people.uic.edu/cs441/hw4/04.PNG)
    
10. Click **Launch**
11. Create a new key pair or use an existing one. It is important to specify a key pair that is working for you, otherwise you won't be able to login to this instance

    ![05](https://mrasto3.people.uic.edu/cs441/hw4/05.PNG)

12. Click **Launch Instance** to let AWS spin up a new EC2 instance
13. Now go to the **Instances** tab, wait until our instance's status becomes **running** and then clicl on **Connect**

    ![06](https://mrasto3.people.uic.edu/cs441/hw4/06.PNG)

14. Copy the example `ssh` command

    ![07](https://mrasto3.people.uic.edu/cs441/hw4/07.PNG)

15. Open an SSH client and paste that command. Remember to change the value of `-i` option which specifies the path to your private key for the key-pair you specified while creating the instance

    ![08](https://mrasto3.people.uic.edu/cs441/hw4/08.PNG)

16. Once logged in, install docker using the below commands

    ```
    sudo yum update -y
    
    sudo yum install -y docker
    ```
    
17. Start the docker service

    ```
    sudo service docker start
    ```
    
18. Deploy our docker container by pulling it from Docker Hub

    ```
    docker run -p 8080:8080 mayankrastogi/chessservice:0.0.1-SNAPSHOT
    ```
    
19. Our Spring Boot application should have started now. Test this by using Postman again. You can either use the **Public IP** of your instance or the **Public DNS** to connect to the deployed service