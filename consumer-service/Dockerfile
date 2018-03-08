FROM pliljenberg/scala-sbt:2.12.3-1.0.2 as builder
WORKDIR /build

COPY project /build/project
RUN sbt update

COPY build.sbt /build
RUN sbt update

COPY . /build
RUN sbt clean test stage


FROM java:8-jre

WORKDIR /opt

COPY --from=builder /build/target/universal/stage/ /opt

ENTRYPOINT ["bin/consumer-service"]


