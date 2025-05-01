# Stage 1: Build and Export with Kobweb CLI
FROM eclipse-temurin:17 as build

ENV KOBWEB_CLI_VERSION=0.9.13
ENV NODE_MAJOR=20
ARG KOBWEB_APP_ROOT=site

# Install required OS packages
RUN apt-get update && apt-get install -y \
    curl gnupg unzip wget ca-certificates npm && \
    rm -rf /var/lib/apt/lists/*

# Install Node.js and Playwright (Chromium)
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_MAJOR}.x | bash - && \
    apt-get update && apt-get install -y nodejs && \
    npm install -g npm && \
    npx playwright install --with-deps chromium

# Download and set up Kobweb CLI
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip && \
    unzip kobweb-${KOBWEB_CLI_VERSION}.zip && \
    rm kobweb-${KOBWEB_CLI_VERSION}.zip
ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

# Copy project
WORKDIR /app
COPY . .

# Reduce Gradle memory usage
RUN mkdir -p /root/.gradle && \
    echo "org.gradle.jvmargs=-Xmx512m" >> /root/.gradle/gradle.properties

# Export the site
WORKDIR /app/${KOBWEB_APP_ROOT}
RUN kobweb export --notty

---

# Stage 2: Lightweight runtime for Kobweb server
FROM eclipse-temurin:17-jre as run

ARG KOBWEB_APP_ROOT=site
WORKDIR /app

# Copy only exported site
COPY --from=build /app/${KOBWEB_APP_ROOT}/.kobweb .kobweb

# Open the default Kobweb port
EXPOSE 8080

# Start the Kobweb server
ENTRYPOINT [".kobweb/server/start.sh"]
