# Create an image for jdk
FROM eclipse-temurin:21-jdk

#Creating a directory to house our image's application code
WORKDIR /app
#The following COPY instruction copies the Maven wrappers and pom file from the host machine  to the container image.
#The pom.xml file contains information of project and configuration information for the maven to build the project such as
#dependencies, build directory, source directory, test source directory, plugin, goals etc

COPY .mvn ./.mvn
COPY mvnw pom.xml ./

#The following RUN instructions trigger a goal that resolves all project dependencies
#including plugins and reports and their dependencies.
RUN ./mvnw dependency:go-offline

#Next, we need to copy the most important directory of the maven project – /src.
#It includes java source code and pre-environment configuration files of the artifact.
COPY src ./src

#The Spring Boot Maven plugin includes a run goal which can be used to quickly compile and run your application.
#The last line tells Docker to compile and run your app packages.
CMD ["./mvnw","spring-boot:run"]
