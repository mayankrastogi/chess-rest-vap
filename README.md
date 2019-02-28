# Homework 4
### Description: you will gain experience with creating a chess game VAP using a unikernel OS and a containerization toolkit.
### Grade: 5% + bonus up to 3% for adding a database to the VAP to store and retrieve game records.
#### You can obtain this Git repo using the command ```git clone git@bitbucket.org:cs441_spring2019/homework4.git```. You cannot push your code into this repo, otherwise, your grade for this homework will be ZERO!

## Preliminaries
If you have not already done so as part of your first homework, you must create your account at [BitBucket](https://bitbucket.org/), a Git repo management system. It is imperative that you use your UIC email account that has the extension @uic.edu. Once you create an account with your UIC address, BibBucket will assign you an academic status that allows you to create private repos. Bitbucket users with free accounts cannot create private repos, which are essential for submitting your homeworks and the course project. Your instructor created a team for this class named [cs441_Spring2019](https://bitbucket.org/cs441_spring2019/). Please contact your TA [Shen Wang](swang224@uic.edu) from your **UIC.EDU** account and they will add you to the team repo as developers, since they already have the admin privileges. Please use your emails from the class registration roster to add you to the team and you will receive an invitation from BitBucket to join the team. Since it is still a large class, please use your UIC email address for communications or Piazza and avoid emails from other accounts like funnybunny1992@gmail.com. If you don't receive a response within 12 hours, please contact us via Piazza, it may be a case that your direct emails went to the spam folder.

In case you have not done so, you will install [IntelliJ](https://www.jetbrains.com/student/) with your academic license, the JDK, the Scala runtime and the IntelliJ Scala plugin, the [Simple Build Toolkit (SBT)](https://www.scala-sbt.org/1.x/docs/index.html) or some other building tool like Maven or Gradle and make sure that you can create, compile, and run Java programs. Please make sure that you can run [Java monitoring tools](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr025.html) or you can choose a newer JDK and tools if you want to use a more recent one.

Please set up your account with [AWS Educate](https://aws.amazon.com/education/awseducate/). Using your UIC email address will enable you to receive free credits for running your jobs in the cloud.

## Overview
In this homework, you will create a web service that wraps some of the [open source Java chess games](https://sourceforge.net/projects/javaopenchess/) or some other open-source Java chess game programs available in public repositories like Github. Basic [chess tutorials](https://www.chessstrategyonline.com/tutorials) are also publically available along with documentations on [algebraic chess notations](https://en.wikipedia.org/wiki/Chess_notation). You do not have to learn chess at all -- all you need to know is how chess coordinates are recorded, so the notation e2-f3 means that a piece is moved from the square e2 to the square f3. Some of you brave enough may want to create your own chess engine, but I recommend against it and I will not award those who do so with additional bonus points. However, if you want to create a VAP of something else, maybe checkers instead of chess or a biorythm computation engine, please contact me in advance and we will discuss your offer.

Once you create a web service, you will deploy it using the unikernel [OSv](http://osv.io) and independently, in a [Docker container](https://www.docker.com/). For the latter, you will create your account at [DockerHub](https://hub.docker.com/) and upload your VAP docker image there. Then you will deploy them in AWS EC2 instances.

Essentially, you take an existing chess engine immplementation in Java and create the following interface for it. 
```java
interface IChessEngine {
	Response newGame(boolean firstMove) throws Exception;
	Response move(Square start, Square end, Session session) throws Exception;
	Response quit(Session session) throws Exception;
}
```
You can modify this interface any way you want, but there are three main immutable ideas that are encoded in it. First, your web service is stateful, since it keeps track of what moves are made in what session. The method newGame starts a new session (the parameter firstMove defines who plays whites and makes the first move) and them method quit ends the game. The method move is asynchronous, since it sends the next move for a given session and waits for a response. These are losely defined directions to simplify the state management. As before, this homework script is written using a retroscripting technique, in which the homework outlines are generally and loosely drawn, and the individual students improvise to create the implementation that fits their refined objectives. In doing so, students are expected to stay within the basic requirements of the homework and they are free to experiments. Asking questions is important, so please ask away at Piazza!

## Functionality
Your homework assignment can be divided roughly into five steps. First, you find an open-source Java chess game, pull it from a repo where it is located, and compile and run it. I suggest that unless you want to try some cool AI-driven chess game, there is no reason for you not to use one of Java open chess projects. However, if you insist on choosing your open-source project, choose one that has at least 100 commits and at least two contributors. Second, you componentize it by wrapping it in the interface IChessEngine through which methods clients can control the chess engine. Next, you will create a RESTful web service whose Application Programming Interface (API) reflects the methods of the interface IChessEngine. I recommend that your REST API accept JSON using [Spring Boot](https://spring.io/guides/gs/actuator-service/). You can deploy and test your web service locally on your developer workstation. Fourth, you will build the OSv appliance from your web service, load it up in the local VM, test it, and then deploy it in the AWS. Finally, you will create a docker configuration and build a dockerized container using your web service, and you will upload it to the docker hub using your account. It seems like a lot of work, however, there are plenty of tools to automate many of these (sub)steps, so this homework has a below average load.

As before, to run the VM, you can install vmWare or VirtualBox. As UIC students, you have access to free vmWare licenses, go to http://go.uic.edu/csvmware to obtain your free license. In some cases, I may have to provide your email addresses to a department administrator to enable your free VM academic licenses. Please notify me if you cannot register and gain access to the webstore.

The steps for obtaining your free academic vmWare licenses are the following:
- Go to [Onthehub vmWare](http://go.uic.edu/csvmware).
- Click on the "sign in" link at the top.
- Click on "register".
- Select "An account has been created..." and continue with the registration.
- Make sure that you use the UIC email with which you are registered with the system.

Only UIC students who are registered for this course are eligible. If you are auditing the course, you need to contact the uic webstore directly. Alternatively, you can use [VirtualBox from Oracle Corp](https://www.virtualbox.org/).

To test your homework, in case it is a chess VAP, you will use your own chess VAP playing against itself (one makes the first move and the other responds) or someone's else chess VAP as long as the moves are described using the same interface. We will start a thread on piazza to formalize it, but the initial sketch is the following.
```json
{"move": {
  "session": "MarkGrechanik1",
  "startSquare": "e2",
  "endSquare": "f3",
  "description": {
    "chesspiece": [
      {"bishop": "whitesquares", "attacks": "pawn"},
      {"pawn": "black", "attackedby": "bishop"}
    ]
  }
}}
```
Of course, this is just a suggestion, feel free to simplify the interface or adjust it as you need for your chess engine VAP.

Next, after creating and testing your chess VAP locally, you will deploy it and run it on the AWS EC2 - you can find plenty of [documentation 
online for OSv in AWS EC2](http://osv.io/amazon-ec2/) and for [deploying docker containers](https://aws.amazon.com/getting-started/tutorials/deploy-docker-containers/). You will produce a short movie that documents all steps of the deployment and execution of your VAP with your narration and you will upload this movie to [youtube](www.youtube.com) and you will submit a link to your movie as part of your submission in the README.md file. To produce a movie, you may use an academic version of [Camtasia](https://shop.techsmith.com/store/techsm/en_US/cat/categoryID.67158100) or some other cheap/free screen capture technology from the UIC webstore or an application for a movie capture of your choice. The captured web browser content should show your login name in the upper right corner of the AWS application and you should introduce yourself in the beginning of the movie speaking into the camera.

## Baseline Submission
Your baseline project submission should include your web service implementation with the open-source chess Java code engine, a conceptual explanation in the document or in the comments in the source code of API calls to interact with your web service, and the documentation that describe the build, deployment and the runtime process, to be considered for grading. Your project submission should include all your source code for the web service and capstan and dockerfile configurations as well as non-code artifacts (e.g., chess engine configuration files if applicable), your project should be buildable using one of Maven, Gradle, or SBT, and your documentation must specify the structure of the JSON payload. Simply copying Java chess programs from open-source projects and modifying them a bit to accept CLI commands will result in rejecting your submission.

## Piazza collaboration
You can post questions and replies, statements, comments, discussion, etc. on Piazza. For this homework, feel free to share your ideas, mistakes, code fragments, commands from scripts, and some of your technical solutions with the rest of the class, and you can ask and advise others using Piazza on where resources and sample programs can be found on the internet, how to resolve dependencies and configuration issues. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw2 to ensure that all discussion threads can be easily located. Active participants and problem solvers will receive bonuses from the big brother :-) who is watching your exchanges on Piazza (i.e., your class instructor). However, *you must not post your capstan or dockerfile or your source code!*

## Git logistics
**This is an individual homework.** Separate repositories will be created for each of your homeworks and for the course project. You will find a corresponding entry for this homework at git@bitbucket.org:cs441_spring2019/homework2.git. You will fork this repository and your fork will be private, no one else besides you, the TA and your course instructor will have access to your fork. Please remember to grant a read access to your repository to your TA and your instructor. In future, for the team homeworks and the course project, you should grant the write access to your forkmates, but NOT for this homework. You can commit and push your code as many times as you want. Your code will not be visible and it should not be visible to other students (except for your forkmates for a team project, but not for this homework). When you push the code into the remote repo, your instructor and the TA will see your code in your separate private fork. Making your fork public, pushing your code into the main repo, or inviting other students to join your fork for an individual homework will result in losing your grade. For grading, only the latest push timed before the deadline will be considered. **If you push after the deadline, your grade for the homework will be zero**. For more information about using the Git and Bitbucket specifically, please use this [link as the starting point](https://confluence.atlassian.com/bitbucket/bitbucket-cloud-documentation-home-221448814.html). For those of you who struggle with the Git, I recommend a book by Ryan Hodson on Ry's Git Tutorial. The other book called Pro Git is written by Scott Chacon and Ben Straub and published by Apress and it is [freely available](https://git-scm.com/book/en/v2/). There are multiple videos on youtube that go into details of the Git organization and use.

Please follow this naming convention while submitting your work : "Firstname_Lastname_hw2" without quotes, where you specify your first and last names **exactly as you are registered with the University system**, so that we can easily recognize your submission. I repeat, make sure that you will give both your TA and the course instructor the read access to your *private forked repository*.

## Discussions and submission
You can post questions and replies, statements, comments, discussion, etc. on Piazza. Remember that you cannot share your code and your solutions privately, but you can ask and advise others using Piazza and StackOverflow or some other developer networks where resources and sample programs can be found on the Internet, how to resolve dependencies and configuration issues. Yet, your implementation should be your own and you cannot share it. Alternatively, you cannot copy and paste someone else's implementation and put your name on it. Your submissions will be checked for plagiarism. **Copying code from your classmates or from some sites on the Internet will result in severe academic penalties up to the termination of your enrollment in the University**. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw1 to ensure that all discussion threads can be easily located.


## Submission deadline and logistics
Sunday, March 17 at 7PM CST via the bitbucket repository. Your submission will include the code for the web service, your documentation with instructions and detailed explanations on how to assemble and deploy your web service along with the results of its run, i.e., a record of the game that your web service plays with itself, and a document that explains how you built and deployed your web service as an OSv and Docker VAP and what your experiences are, and where the URL to the git repo for the original open-source Java chess program. Again, do not forget, please make sure that you will give both your TA and your instructor the read access to your private forked repository. Your name should be shown in your README.md file and other documents. Your code should compile and run from the command line using the commands like **sbt clean compile test** or **gradle clean build**. Also, you project should be IntelliJ friendly, i.e., your graders should be able to import your code into IntelliJ and run from there. Use .gitignore to exlude files that should not be pushed into the repo.


## Evaluation criteria
- the maximum grade for this homework is 5% with the bonus up to 3% for adding a database to store and retrieve the records of the game with well-documented APIs for this part. Points are subtracted from this maximum grade: for example, saying that 2% is lost if some requirement is not completed means that the resulting grade will be 5%-2% => 3%; if the core homework functionality does not work, no bonus points will be given;
- the code does not work in that it does not produce a correct output or crashes: up to 5% lost;
- no capstan or docker configuration files to build the VAP: up to 5% lost;
- not having tests that enable your chess VAP to play with itself: up to 5% lost;
- missing essential comments and explanations from the source code that you wrote: up to 3% lost;
- no instructions in README.md on how to install and run your program: up to 4% lost;
- your code does not have sufficient comments or your accompanying documents do not contain a description of how you designed and implemented the homework: up to 4% lost;
- no video that shows how you deployed your VAP in AWS or it is not clear how you deployed it: up to 2% lost;
- the documentation exists but it is insufficient to understand how you assembled and deployed all components: up to 4% lost;
- the minimum grade for this homework cannot be less than zero.

That's it, folks!