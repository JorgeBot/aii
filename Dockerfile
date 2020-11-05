FROM java:8
ADD aii-0.0.1-SNAPSHOT.jar aii.jar
RUN echo 'Asia/Shanghai' >/etc/timezone
CMD java -jar clock.jar
