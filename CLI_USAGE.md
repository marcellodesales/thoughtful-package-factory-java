# 📦 Package Classifier CLI Usage
This document provides comprehensive usage instructions for the Box Classification & Stack Assignment System CLI. The system supports **two distinct runtime environments**:

- **🐳 Production Runtime** - Docker-based containerized execution (recommended)
- **⚙️ Development Runtime** - Gradle-based local development and testing

## 📋 Table of Contents

- [Production Usage (Docker)](#-production-usage-docker)
- [Development Usage (Gradle)](#%EF%B8%8F-development-usage-gradle)
- [Input Format](#-input-format)
- [Output Types](#-output-types)
- [Classification Rules](#%EF%B8%8F-classification-rules)
- [Error Handling](#%EF%B8%8F-error-handling)
- [Testing Examples](#-testing-examples)

---

## 🐳 Production Usage (Docker)

**Recommended for production deployments, CI/CD pipelines, and users without Java development stack.**

### Prerequisites
- Docker 20.10+ installed
- Docker Compose 2.0+ installed
- No Java installation required

### Build the Container
```bash
# Build the Docker image
docker compose build

# Verify successful build
docker compose run --rm sorter --help
```

### Show Help Documentation
```bash
docker compose run --rm sorter --help
```

### Classify Packages

**Basic Usage:**
```bash
docker compose run --rm sorter "width,height,length,mass"
```

**Classification Examples:**
```bash
# Standard package (normal size and weight)
docker compose run --rm sorter "50,30,20,5000"
# Output: STANDARD

# Bulky package (oversized dimension)
docker compose run --rm sorter "150,30,20,5000"
# Output: SPECIAL

# Heavy package (exceeds weight threshold)
docker compose run --rm sorter "50,30,20,25000"
# Output: SPECIAL

# Rejected package (both bulky and heavy)
docker compose run --rm sorter "150,30,20,25000"
# Output: REJECTED

# Bulky by volume (100³ = 1,000,000 cm³)
docker compose run --rm sorter "100,100,100,15000"
# Output: SPECIAL
```

### Production Benefits

✅ **Zero Dependencies** - No Java installation required  
✅ **Consistent Environment** - Same behavior across all platforms  
✅ **Fast Startup** - 2-3 seconds cold start time  
✅ **Resource Efficient** - ~150MB memory footprint  
✅ **Security Hardened** - Non-root user execution  
✅ **Cloud Ready** - Kubernetes and orchestration platform compatible  

---

## ⚙️ Development Usage (Gradle)

**Recommended for local development, testing, and code modifications. Requires Java development stack.**

### Prerequisites
- Java 21 LTS installed
- Gradle 8.10+ (or use included wrapper)
- Development environment setup

### Development Workflow

The Gradle-based CLI is primarily designed for:
- **Local Development** - Testing code changes during development
- **Unit Testing** - Validating test cases and edge scenarios
- **Debugging** - Investigating classification logic
- **Build Verification** - Ensuring JAR builds work correctly

## 🚀 Running from Gradle

### Show Help
```bash
./gradlew runCli
```

### Classify a Package
```bash
./gradlew sort -Pargs="width,height,length,mass"
```

**Development Examples:**
```bash
# Standard package - for testing normal classification
./gradlew sort -Pargs="50,30,20,5000"
# Output: STANDARD

# Bulky package (dimension >= 150cm) - for testing bulky logic
./gradlew sort -Pargs="150,30,20,5000"
# Output: SPECIAL

# Heavy package (weight >= 20kg) - for testing heavy logic
./gradlew sort -Pargs="50,30,20,25000"
# Output: SPECIAL

# Rejected package (both bulky and heavy) - for testing rejection logic
./gradlew sort -Pargs="150,30,20,25000"
# Output: REJECTED

# Bulky by volume (100³ = 1,000,000 cm³) - for testing volume calculation
./gradlew sort -Pargs="100,100,100,15000"
# Output: SPECIAL
```

### Running Standalone JAR (Development)

**Use case:** Testing JAR build and deployment verification

#### Build the JAR
```bash
./gradlew buildCli
```

#### Run the JAR
```bash
# Show help
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar --help

# Test package classification
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar "50,30,20,5000"
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar "150,30,20,25000"
```

### Development vs Production Comparison

| Aspect | Development (Gradle) | Production (Docker) |
|--------|---------------------|--------------------|
| **Purpose** | Local development & testing | Production deployment |
| **Prerequisites** | Java 21 + Gradle | Docker only |
| **Startup Time** | ~5-10 seconds | ~2-3 seconds |
| **Memory Usage** | ~200-300MB | ~150MB |
| **Environment** | Local JVM | Containerized |
| **Use Cases** | Code changes, debugging | CI/CD, production |
| **Command** | `./gradlew sort -Pargs="..."` | `docker compose run --rm sorter "..."` |

---

## 📋 Input Format

**Required format:** `"width,height,length,mass"`

- **width** - Package width in centimeters (positive integer)
- **height** - Package height in centimeters (positive integer) 
- **length** - Package length in centimeters (positive integer)
- **mass** - Package mass in grams (positive number, can be decimal)

## 📤 Output Types

The CLI returns one of three stack types:

- **STANDARD** - Normal processing (not bulky, not heavy)
- **SPECIAL** - Special handling (bulky OR heavy, but not both)
- **REJECTED** - Cannot process (both bulky AND heavy)

## 🏷️ Classification Rules

### BULKY Package
- Any dimension ≥ 150 cm **OR**
- Total volume ≥ 1,000,000 cm³

### HEAVY Package  
- Mass ≥ 20,000 grams (20 kg)

### Stack Assignment
- **BULKY AND HEAVY** → REJECTED
- **BULKY OR HEAVY** (not both) → SPECIAL
- **Neither** → STANDARD

## ⚠️ Error Handling

**Both Docker and Gradle CLIs provide identical error handling:**

### Docker Error Examples
```bash
# Wrong number of parameters
docker compose run --rm sorter "50,30"
# Error: Input must have exactly 4 comma-separated values

# Invalid numbers
docker compose run --rm sorter "abc,30,20,5000"
# Error: All values must be valid numbers

# Negative values
docker compose run --rm sorter "-50,30,20,5000"
# Error: All dimensions and mass must be positive values
```

### Gradle Error Examples (Development)
```bash
# Wrong number of parameters
./gradlew sort -Pargs="50,30"
# Error: Input must have exactly 4 comma-separated values

# Invalid numbers
./gradlew sort -Pargs="abc,30,20,5000"
# Error: All values must be valid numbers

# Negative values
./gradlew sort -Pargs="-50,30,20,5000"
# Error: All dimensions and mass must be positive values
```

## 🎯 Testing Examples

### Production Testing (Docker)

**Recommended for validating production deployments:**

```bash
# Standard packages
docker compose run --rm sorter "50,50,50,10000"     # STANDARD
docker compose run --rm sorter "100,50,30,15000"    # STANDARD  
docker compose run --rm sorter "99,99,99,19999"     # STANDARD (boundary test)

# Special handling packages
docker compose run --rm sorter "150,10,10,5000"     # SPECIAL (bulky dimension)
docker compose run --rm sorter "10,10,10,25000"     # SPECIAL (heavy weight)
docker compose run --rm sorter "100,100,100,5000"   # SPECIAL (bulky volume)

# Rejected packages
docker compose run --rm sorter "150,150,150,25000"  # REJECTED (bulky + heavy)
docker compose run --rm sorter "200,50,30,22000"    # REJECTED (bulky + heavy)
```

### Development Testing (Gradle)

**Recommended for unit test validation and development testing:**

```bash
# From PackageFactoryTest cases - for development verification
./gradlew sort -Pargs="50,50,50,10000"     # STANDARD
./gradlew sort -Pargs="100,50,30,15000"    # STANDARD  
./gradlew sort -Pargs="99,99,99,19999"     # STANDARD (boundary test)
./gradlew sort -Pargs="150,10,10,5000"     # SPECIAL (bulky)
./gradlew sort -Pargs="10,10,10,25000"     # SPECIAL (heavy)
./gradlew sort -Pargs="150,150,150,25000"  # REJECTED (both)
```

### Quick Validation Suite

**Run these tests to verify both environments work correctly:**

| Test Case | Input | Expected Output | Purpose |
|-----------|-------|-----------------|----------|
| Normal package | `"50,30,20,5000"` | `STANDARD` | Basic functionality |
| Boundary volume | `"99,99,99,19999"` | `STANDARD` | Edge case testing |
| Bulky dimension | `"150,10,10,5000"` | `SPECIAL` | Dimension threshold |
| Heavy weight | `"50,30,20,25000"` | `SPECIAL` | Weight threshold |
| Bulky volume | `"100,100,100,15000"` | `SPECIAL` | Volume calculation |
| Rejected | `"150,50,30,25000"` | `REJECTED` | Combined classification |

---

## 📚 Additional Resources

- **README.md** - Complete system overview and setup instructions
- **Development Guide** - See [Development](#%EF%B8%8F-development) section in README
- **API Documentation** - See algorithm details and examples in README
- **Docker Documentation** - See [Runtime](#-runtime) section in README

---

**💡 Recommendation:** Use **Docker CLI for production** and **Gradle CLI for development testing**.

