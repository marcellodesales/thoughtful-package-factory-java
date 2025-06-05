# Multi-stage build for Java 21 application with Gradle 8.x on Alpine Linux
# Stage 1: Build the application
ARG BUIlD_BASE_IMAGE
ARG RUNTIME_BASE_IMAGE
FROM ${BUIlD_BASE_IMAGE} AS builder

# Set working directory
WORKDIR /app

# Copy build configuration files
COPY build.gradle.kts settings.gradle.kts ./

# Download dependencies (this layer will be cached if dependencies don't change)
RUN gradle dependencies --no-daemon --configuration runtimeClasspath

# Copy source code
COPY src/ src/

# Build the application
RUN gradle bootJar --no-daemon

# Stage 2: Create the runtime image
FROM ${RUNTIME_BASE_IMAGE}

# Set metadata
LABEL maintainer="Package Classification System"
LABEL description="Box Classification & Stack Assignment System"
LABEL version="1.0-SNAPSHOT"

# Install necessary packages for Alpine
RUN apk add --no-cache \
    curl \
    && rm -rf /var/cache/apk/*

# Create app user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser


# Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Default command - show help
ENTRYPOINT ["java", "-jar", "app.jar"]

# Usage examples:
# docker build -t marcellodesales/thoughtful-package-sorter .
# docker run --rm marcellodesales/thoughtful-package-sorter --help
# docker run --rm marcellodesales/thoughtful-package-sorter "50,30,20,5000"
# docker run --rm marcellodesales/thoughtful-package-sorter "150,50,30,10000"

