FROM adoptopenjdk/openjdk8-openj9
VOLUME /tmp

ARG JAR_FILE=build/libs/halo-1.4.3.jar
ARG PORT=8090
ARG TIME_ZONE=Asia/Shanghai

ENV TZ=${TIME_ZONE}
ENV JVM_XMS="512m"
ENV JVM_XMX="512m"

COPY ${JAR_FILE} halo.jar

EXPOSE ${PORT}

ENTRYPOINT java -Xms${JVM_XMS} -Xmx${JVM_XMX} -Djava.security.egd=file:/dev/./urandom -server -jar halo.jar
