FROM openjdk:21
EXPOSE 8080
ADD target/TicTacToe.jar TicTacToe.jar
ENTRYPOINT ["java","-jar","/TicTacToe.jar"]