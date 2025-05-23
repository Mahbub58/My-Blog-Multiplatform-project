ARG KOBWEB_APP_ROOT="site"
FROM eclipse-temurin:21 as java

FROM java as export

ENV KOBWEB_CLI_VERSION=0.9.18
ARG KOBWEB_APP_ROOT
ENV NODE_MAJOR=20

COPY . /project

RUN apt-get update \
    && apt-get install -y ca-certificates curl gnupg unzip wget git \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list \
    && apt-get update \
    && apt-get install -y nodejs \
    && npm init -y \
    && npx playwright install --with-deps chromium

RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb-${KOBWEB_CLI_VERSION}.zip \
    && rm kobweb-${KOBWEB_CLI_VERSION}.zip

ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

WORKDIR /project/${KOBWEB_APP_ROOT}

RUN mkdir -p ~/.gradle \
    && echo "org.gradle.jvmargs=-Xmx325m" >> ~/.gradle/gradle.properties

# Log before export
RUN echo "Running kobweb export in $(pwd)" && ls -la

# Add verbose export and log output
RUN kobweb export --notty -v || (echo "KOBWEB EXPORT FAILED" && exit 1)

# Log after export to verify site was created
RUN echo "After kobweb export:" && ls -la .kobweb && ls -la .kobweb/site || (echo "SITE NOT GENERATED!" && exit 1)

FROM java as run
ARG KOBWEB_APP_ROOT

COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb /app/.kobweb

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Xmx512m"
ENTRYPOINT .kobweb/server/start.sh
