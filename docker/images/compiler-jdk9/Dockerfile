FROM openjdk:9

RUN apt-get update && apt-get install -y \
        ant

COPY compile /compile

WORKDIR /compile

CMD ["/usr/bin/ant"]