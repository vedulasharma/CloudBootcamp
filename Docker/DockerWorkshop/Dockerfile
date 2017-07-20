FROM java:7

ENV FOO bar

COPY src /home/root/DockerWorkshop/src

WORKDIR /home/root/DockerWorkshop
RUN mkdir bin
RUN javac -d bin src/HelloDocker.java
RUN apt-get update && apt-get install -y vim

ENTRYPOINT ["java", "-cp", "bin", "HelloDocker"]