#-----------------------------------------------------------------------------
# Variables shared across multiple stages (they need to be explicitly opted
# into each stage by being declaring there too, but their values need only be
# specified once).
#-----------------------------------------------------------------------------

ARG KOBWEB_APP_ROOT="site"

#-----------------------------------------------------------------------------
# Stage: Java base image
#-----------------------------------------------------------------------------

FROM eclipse-temurin:21.0.6 AS java

#-----------------------------------------------------------------------------
# Stage: Build and export site
#-----------------------------------------------------------------------------

FROM java AS export

ENV KOBWEB_CLI_VERSION=0.9.18
ARG KOBWEB_APP_ROOT

ENV NODE_MAJOR=23

COPY . /project

RUN apt-get update \
    && apt-get install -y ca-certificates curl gnupg unzip wget \
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

RUN mkdir ~/.gradle && \
    echo "org.gradle.jvmargs=-Xmx256m" >> ~/.gradle/gradle.properties

RUN kobweb export --notty

#-----------------------------------------------------------------------------
# Stage: Final runtime image
#-----------------------------------------------------------------------------

FROM java AS run

ARG KOBWEB_APP_ROOT

# Uncomment if you need to copy the exported site
COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb .kobweb

ENTRYPOINT [".kobweb/server/start.sh"]

