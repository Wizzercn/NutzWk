FROM azul/zulu-openjdk-alpine:8u162
MAINTAINER wizzer <wizzer@qq.com>

ENTRYPOINT ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom  -jar /usr/share/nb.jar
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/nb.jar