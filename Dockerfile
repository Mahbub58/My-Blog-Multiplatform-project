# Use Java base image
FROM eclipse-temurin:21-jdk

# Define arguments and environment
ARG KOBWEB_CLI_VERSION=0.9.18
ENV KOBWEB_APP_ROOT=site
ENV NODE_MAJOR=20

# Set working directory
WORKDIR /project

# Copy project files
COPY . .

# Install required system packages and Node.js
RUN apt-get update && \
    apt-get install -y curl gnupg unzip wget ca-certificates && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" > /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install -y nodejs && \
    npm init -y && \
    npx playwright install --with-deps chromium

# Install Kobweb CLI
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip && \
    unzip kobweb-${KOBWEB_CLI_VERSION}.zip && \
    rm kobweb-${KOBWEB_CLI_VERSION}.zip

# Add Kobweb to PATH
ENV PATH="/project/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

# Gradle memory limit (optional for low-resource environments)
RUN mkdir -p ~/.gradle && echo "org.gradle.jvmargs=-Xmx512m" >> ~/.gradle/gradle.properties

# Set working directory to the app root
WORKDIR /project/${KOBWEB_APP_ROOT}

# Expose default Kobweb port
EXPOSE 8080

# At runtime: export the site and run the server
ENTRYPOINT ["sh", "-c", "kobweb export --notty && .kobweb/server/start.sh"]
