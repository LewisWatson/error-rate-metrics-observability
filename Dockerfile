FROM gcr.io/distroless/java:11
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/error-rate-demo-*.jar error-rate-demo.jar
EXPOSE 8080
CMD ["error-rate-demo.jar"]
