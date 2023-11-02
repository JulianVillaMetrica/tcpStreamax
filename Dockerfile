# Use an official OpenJDK image as a parent image
FROM ubuntu:22.04

# Set the working directory in the container
WORKDIR /app

# Copy the source code into the container
COPY src /app/src

# Install the javac command
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Install the wget command
RUN apt-get update && apt-get install -y wget

# Download the latest version of Gson
RUN wget https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.9.0/gson-2.9.0.jar -O gson-2.9.0.jar

# Compile your Java code (adjust the compilation command as needed)
RUN javac -cp gson-2.9.0.jar src/*.java

# Set the classpath to include your downloaded JAR and the current directory
ENV CLASSPATH=".:/app/gson-2.9.0.jar"

WORKDIR /app/src
# Specify the main class to run when the container starts
CMD ["java", "Server"]