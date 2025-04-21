FROM ubuntu:latest
LABEL authors="dlwns"

ENTRYPOINT ["top", "-b"]