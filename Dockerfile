# Stage 1: Build and export with Kobweb CLI
FROM eclipse-temurin:17 as build

ENV KOBWEB_CLI_VERSION=0.9.13
ENV NODE_MAJOR=20
ARG KOBWEB_APP_ROOT=site

# Install required packages
RUN apt-get update && apt-get install -y \
    curl gnupg unzip wget ca-certificates npm && \
    rm -rf /var/lib/apt/lists/*

# Install Node.js
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_MAJOR}.x | bash - && \
    apt-get update && apt-get install -y nodejs && \
    npm install -g npm

# Install Playwright Chromium
RUN npm init -y && \
    npx playwright install --with-deps chromium

# Install Kobweb CLI
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip && \
    unzip kobweb-${KOBWEB_CLI_VERSION}.zip && \
    rm kobweb-${KOBWEB_CLI_VERSION}.zip
ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

# Copy source code
WORKDIR /app
COPY . .

# Reduce Gradle memory usage
RUN mkdir -p /root/.gradle && \
    echo "org.gradle.jvmargs=-Xmx512m" >> /root/.gradle/gradle.properties

# Run export
WORKDIR /app/${KOBWEB_APP_ROOT}
RUN kobweb export --notty

# Stage 2: Minimal runtime
FROM eclipse-temurin:17-jre as run

ARG KOBWEB_APP_ROOT=site
WORKDIR /app

# Copy only the exported Kobweb server
COPY --from=build /app/${KOBWEB_APP_ROOT}/.kobweb .kobweb

# Expose port and run the server
EXPOSE 8080
ENTRYPOINT [".kobweb/server/start.sh"]
