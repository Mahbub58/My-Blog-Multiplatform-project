FROM eclipse-temurin:21-jdk

ARG KOBWEB_CLI_VERSION=0.9.18
ENV KOBWEB_APP_ROOT=site
ENV NODE_MAJOR=20

WORKDIR /project
COPY . .

RUN apt-get update && \
    apt-get install -y curl gnupg unzip wget ca-certificates && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" > /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install -y nodejs && \
    npm init -y && \
    npx playwright install --with-deps chromium

RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip && \
    unzip kobweb-${KOBWEB_CLI_VERSION}.zip && \
    rm kobweb-${KOBWEB_CLI_VERSION}.zip

ENV PATH="/project/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

RUN mkdir -p ~/.gradle && echo "org.gradle.jvmargs=-Xmx512m" >> ~/.gradle/gradle.properties

WORKDIR /project/${KOBWEB_APP_ROOT}
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "kobweb export --notty && .kobweb/server/start.sh"]
