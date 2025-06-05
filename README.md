# 📦 Box Classification & Stack Assignment System

[![Java 21 LTS](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Gradle 8.10](https://img.shields.io/badge/Gradle-8.10-brightgreen.svg)](https://gradle.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10.0-brightgreen.svg)](https://junit.org/junit5/)
[![Code Coverage](https://img.shields.io/badge/Coverage-97.2%25-brightgreen.svg)](#-code-coverage)

An intelligent classification system that analyzes **box measurements** (length, width, height) and **weight** to determine the appropriate **stack type** for warehouse operations. The system implements a two-stage classification algorithm: first categorizing boxes by their physical properties, then assigning them to specific stacks based on handling requirements.

## 📋 Table of Contents

- [System Overview](#-system-overview)
- [Classification Algorithm](#-classification-algorithm)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Running Tests](#-running-tests)
- [Code Coverage](#-code-coverage)
- [System Usage](#-system-usage)
- [Algorithm Details](#-algorithm-details)
- [Development](#-development)

## 🎯 System Overview

### Input → Classification → Stack Assignment

The system takes **box measurements as input** and produces **stack assignments as output** through a two-stage classification process:

**📥 Input:** Box dimensions (length × width × height in cm) + weight (in grams)  
**📤 Output:** Stack assignment (STANDARD, SPECIAL, or REJECTED)

### Stage 1: Box Classification

Boxes are first classified into categories based on their physical properties:

| Classification | Criteria |
|----------------|----------|
| **📏 BULKY** | Any dimension ≥ 150 cm **OR** volume ≥ 1,000,000 cm³ |
| **⚖️ HEAVY** | Weight ≥ 20 kg (20,000 grams) |
| **📄 STANDARD** | Neither bulky nor heavy |

### Stage 2: Stack Assignment

Based on classification results, boxes are assigned to stacks:

| Stack Type | Assignment Rule | Handling Requirements |
|------------|----------------|----------------------|
| **🚫 REJECTED** | BULKY **AND** HEAVY | Cannot be processed - requires special handling |
| **⭐ SPECIAL** | BULKY **OR** HEAVY (not both) | Requires specialized equipment or procedures |
| **✅ STANDARD** | Neither bulky nor heavy | Standard processing workflow |

### Algorithm Examples

| Box Dimensions | Weight | Classification | Stack Assignment | Reason |
|----------------|--------|----------------|------------------|--------|
| 100×50×30 cm | 10 kg | STANDARD | STANDARD | Normal size and weight |
| 150×50×30 cm | 10 kg | BULKY | SPECIAL | Oversized dimension |
| 100×50×30 cm | 25 kg | HEAVY | SPECIAL | Exceeds weight limit |
| 150×50×30 cm | 25 kg | BULKY | REJECTED | Both oversized and overweight |
| 100×100×100 cm | 15 kg | BULKY | SPECIAL | Volume = 1,000,000 cm³ |

## 🔄 Classification Algorithm

### Classification Decision Flow

```mermaid
flowchart TD
    A["📦 Box Input<br/>L×W×H cm, Weight kg"] --> B{"📏 Any dimension ≥ 150cm?"}
    B -->|Yes| C["🏷️ BULKY"]
    B -->|No| D{"📊 Volume ≥ 1,000,000 cm³?"}
    D -->|Yes| C
    D -->|No| E{"⚖️ Weight ≥ 20kg?"}
    E -->|Yes| F["🏷️ HEAVY"]
    E -->|No| G["🏷️ STANDARD"]
    
    C --> H{"⚖️ Also HEAVY?"}
    F --> I{"📏 Also BULKY?"}
    G --> J["✅ STANDARD Stack<br/>Normal processing"]
    
    H -->|Yes| K["🚫 REJECTED Stack<br/>Cannot process"]
    H -->|No| L["⭐ SPECIAL Stack<br/>Special handling"]
    I -->|Yes| K
    I -->|No| L
```

### Technical Requirements

- **Java 21 LTS** - Long Term Support version
- **Gradle 8.10** - Build automation
- **JUnit 5** - Testing framework
- **JaCoCo** - Code coverage analysis
- **Immutable Design** - All classes are immutable
- **Factory Pattern** - Centralized object creation
- **Comprehensive Testing** - 97%+ code coverage

## 🏗️ Project Structure

```
src/
├── main/java/ai/thoughtful/platform/factory/
│   ├── PackageClassification.java    # Classification types (BULKY, HEAVY, STANDARD)
│   ├── PackageFactory.java           # Main API for box classification
│   ├── StackType.java               # Stack assignments (STANDARD, SPECIAL, REJECTED)
│   └── model/
│       ├── Package.java             # Box representation with measurements
│       └── PackageDimension.java    # Dimension calculations and validation
└── test/java/ai/thoughtful/platform/factory/
    ├── PackageClassificationTest.java  # Tests classification algorithm
    ├── PackageFactoryTest.java         # Tests main API functionality
    ├── StackTypeTest.java             # Tests stack assignment logic
    └── model/
        ├── PackageTest.java           # Tests box behavior
        └── PackageDimensionTest.java  # Tests dimension calculations
```

## 🧠 Core Components

### System Architecture

1. **🏭 Classification Engine**
   - Analyzes box measurements against BULKY/HEAVY criteria
   - Validates input parameters (positive dimensions and weight)
   - Central entry point for box analysis

2. **📦 Box Representation**
   - Immutable data structure containing measurements
   - Automatically calculates volume (L × W × H)
   - Provides classification and stack assignment

3. **📐 Dimension Handler**
   - Validates dimensional constraints
   - Performs volume calculations
   - Checks dimensional thresholds

4. **🏷️ Classification Results**
   - `BoxClassification`: BULKY, HEAVY, STANDARD
   - `StackAssignment`: REJECTED, SPECIAL, STANDARD

## 🚀 Quick Start

### Prerequisites

- **Java 21** (LTS version)
- **Gradle 8.10+** (or use the included wrapper)

### Clone and Build

```bash
# Clone the repository
git clone <repository-url>
cd package-classification-system

# Build the project
./gradlew build

# Run all tests
./gradlew test
```

## 🧪 Running Tests

### Test Execution

```bash
# Run all tests with detailed output
./gradlew test

# Run tests with coverage report
./gradlew testWithCoverage

# Run specific test class
./gradlew test --tests "*PackageFactoryTest"

# Run tests matching pattern
./gradlew test --tests "*Test" --info
```

### Test Categories

The test suite covers:

- ✅ **Classification Tests** - Verify BULKY/HEAVY/STANDARD rules
- ✅ **Stack Assignment Tests** - Verify STANDARD/SPECIAL/REJECTED logic
- ✅ **Boundary Tests** - Test threshold values (150 cm, 20 kg, 1M cm³)
- ✅ **Volume Calculation Tests** - Verify mathematical accuracy
- ✅ **Input Validation Tests** - Test error handling for invalid inputs
- ✅ **Edge Case Tests** - Test corner cases and extreme values

### Sample Test Output

```
> Task :test

PackageClassification Tests > Bulky Package Tests > Should classify as bulky when any dimension is >= 150cm > [1] 150, 10, 10, 5000 PASSED
PackageFactory Tests > Business Logic Tests > Should create bulky package by dimension PASSED
StackType Tests > Special Stack Tests > Should classify stack as SPECIAL for bulky or heavy packages PASSED

BUILD SUCCESSFUL in 2s
119 tests completed, 119 passed
```

## 📊 Code Coverage

### Coverage Reporting

```bash
# Generate coverage report with console output
./gradlew test jacocoTestReport

# Verify coverage meets threshold (80%)
./gradlew jacocoTestCoverageVerification
```

### Current Coverage Metrics

```
📊 CODE COVERAGE REPORT
================================================================================

📄 INDIVIDUAL FILE COVERAGE:
--------------------------------------------------------------------------------
Class                                              Instructions     Branches        Lines
--------------------------------------------------------------------------------
ai.thoughtful.platform.factory.model.PackageFactory  81.3% (13/16)          N/A  66.7% (2/3)
ai.thoughtful.platform.factory.StackType             96.4% (54/56)  83.3% (5/6)  92.9% (13/14)
ai.thoughtful.platform.factory.PackageClassification 100.0% (53/53) 100.0% (10/10) 100.0% (11/11)
ai.thoughtful.platform.factory.model.PackageDimension 100.0% (32/32) 100.0% (6/6) 100.0% (4/4)
ai.thoughtful.platform.factory.model.Package          100.0% (22/22) 100.0% (2/2) 100.0% (4/4)
--------------------------------------------------------------------------------

📊 OVERALL SUMMARY:
📋 Instructions: 97.2% (174/179)
🌿 Branches:     95.8% (23/24)
📄 Lines:       94.4% (34/36)
```

### Coverage Reports

- **Console Report** - Immediate feedback during build
- **HTML Report** - `build/reports/jacoco/test/html/index.html`
- **XML Report** - `build/reports/jacoco/test/jacocoTestReport.xml`

## 💡 System Usage

### Input/Output Examples

The system processes box measurements and returns stack assignments:

#### API Usage Examples

**Example 1: Standard Box Processing**

```java
// 1. Create package using fluent API
Package box = PackageFactory.builder()
    .withLengthInCm(50)
    .withWidthInCm(30) 
    .withHeightInCm(20)
    .withMassInGrams(5000)
    .build();

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Empty set (STANDARD = no classifications)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(30, 20, 50, 5000); // width, height, length, mass
// Result: "STANDARD" (normal processing)
```

**Example 2: Standard Box (Larger but within limits)**

```java
// 1. Create package using fluent API with clear parameter names
Package box = PackageFactory.builder()
    .withLengthInCm(80)
    .withWidthInCm(60)
    .withHeightInCm(40)
    .withMassInGrams(15000)
    .build();

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Empty set (dimensions < 150cm, weight < 20kg, volume = 192,000 cm³)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(60, 40, 80, 15000); // width, height, length, mass
// Result: "STANDARD" (normal processing)
```

**Example 3: Standard Box (Near threshold) - Fluent API**

```java
// 1. Create package using fluent API - any order is fine!
Package box = PackageFactory.builder()
    .withMassInGrams(19999)      // Can set mass first
    .withHeightInCm(99)          // Then height
    .withWidthInCm(99)           // Then width  
    .withLengthInCm(99)          // Finally length
    .build();                    // Build validates all properties are set

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Empty set (all dimensions < 150cm, weight < 20kg, volume = 970,299 cm³)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(99, 99, 99, 19999); // width, height, length, mass
// Result: "STANDARD" (normal processing)
```

#### Special Handling Examples

**Example 1: Bulky by Dimension**

```java
// 1. Create package using factory
Package box = PackageFactory.make(150, 10, 10, 5000); // width, height, length, mass in grams

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Contains BULKY (length = 150cm ≥ threshold)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(150, 10, 10, 5000);
// Result: "SPECIAL" (bulky but not heavy)
```

**Example 2: Heavy Package**

```java
// 1. Create package using factory
Package box = PackageFactory.make(10, 10, 10, 25000); // width, height, length, mass in grams

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Contains HEAVY (weight = 25kg ≥ 20kg threshold)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(10, 10, 10, 25000);
// Result: "SPECIAL" (heavy but not bulky)
```

**Example 3: Bulky by Volume**

```java
// 1. Create package using factory
Package box = PackageFactory.make(100, 100, 100, 5000); // width, height, length, mass in grams

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Contains BULKY (volume = 1,000,000 cm³ ≥ threshold)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(100, 100, 100, 5000);
// Result: "SPECIAL" (bulky but not heavy)
```

#### Rejected Processing Examples

**Example 1: Both Bulky and Heavy**

```java
// 1. Create package using factory  
Package box = PackageFactory.make(150, 150, 150, 25000); // width, height, length, mass in grams

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Contains both BULKY and HEAVY

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(150, 150, 150, 25000);
// Result: "REJECTED" (both bulky AND heavy)
```

**Example 2: Oversized Length and Heavy**

```java
// 1. Create package using factory
Package box = PackageFactory.make(200, 50, 30, 22000); // width, height, length, mass in grams

// 2. Get classification using static method
EnumSet<PackageClassification> classification = PackageClassification.classify(box);
// Result: Contains both BULKY and HEAVY (length = 200cm > 150cm, weight > 20kg)

// 3. Determine stack assignment using static method
String stackAssignment = StackType.sort(200, 50, 30, 22000);
// Result: "REJECTED" (both bulky AND heavy)
```

### API Endpoints

#### Classification API

**Function:** `classifyBox(length, width, height, weightInGrams)`

**Parameters:**
- `length` - Box length in cm (must be > 0)
- `width` - Box width in cm (must be > 0)
- `height` - Box height in cm (must be > 0)
- `weightInGrams` - Box weight in grams (must be > 0)

**Returns:** Classification result (BULKY, HEAVY, or STANDARD)

#### Stack Assignment API

**Function:** `assignStack(length, width, height, weightInGrams)`

**Parameters:** Same as classification API

**Returns:** Stack assignment (STANDARD, SPECIAL, or REJECTED)

### Error Conditions

The system validates all inputs and rejects invalid measurements:

| Invalid Input | Error Reason |
|---------------|-------------|
| Dimension ≤ 0 | Physical impossibility |
| Weight ≤ 0 | Physical impossibility |
| NaN or Infinite values | Invalid measurement |

### Volume Calculation

The system automatically calculates box volume: **Volume = Length × Width × Height**

Boxes with volume ≥ 1,000,000 cm³ are classified as BULKY even if no single dimension exceeds 150 cm.

## 📚 Algorithm Details

### Classification Rules

#### BULKY Classification

**Rule 1: Dimensional Check**
- If ANY dimension (length, width, or height) ≥ 150 cm → BULKY

**Rule 2: Volume Check**
- If total volume ≥ 1,000,000 cm³ → BULKY
- Volume = Length × Width × Height

#### HEAVY Classification

**Rule:** Weight ≥ 20,000 grams (20 kg) → HEAVY

#### STANDARD Classification

**Rule:** Box is neither BULKY nor HEAVY → STANDARD

### Stack Assignment Logic

#### Decision Matrix

| BULKY | HEAVY | Stack Assignment |
|-------|-------|------------------|
| ❌ | ❌ | STANDARD |
| ✅ | ❌ | SPECIAL |
| ❌ | ✅ | SPECIAL |
| ✅ | ✅ | REJECTED |

### Implementation Architecture

**Core Components:**
- **Input Validator:** Ensures all measurements are positive and finite
- **Classification Engine:** Applies BULKY and HEAVY rules
- **Stack Assigner:** Maps classifications to stack types
- **Result Formatter:** Returns structured classification results

**Data Flow:**
1. Receive box measurements
2. Validate input parameters
3. Calculate volume from dimensions
4. Apply classification rules
5. Determine stack assignment
6. Return result

## 🛠️ Development

### Code Quality Standards

- **Immutability** - All data structures are immutable
- **Validation** - Comprehensive input validation
- **Testing** - Minimum 95% code coverage
- **Documentation** - Javadoc for public APIs
- **Modern Java** - Uses Java 21 features (records, pattern matching)

### Build Tasks

```bash
# Clean build
./gradlew clean build

# Run tests with coverage
./gradlew test jacocoTestReport

# Verify code coverage threshold
./gradlew jacocoTestCoverageVerification

# Generate all reports
./gradlew test jacocoTestReport build

# Development workflow
./gradlew testWithCoverage
```

### IDE Integration

- **IntelliJ IDEA** - Import as Gradle project
- **Eclipse** - Use Gradle plugin
- **VS Code** - Java Extension Pack + Gradle plugin

### Continuous Integration

The project is configured for CI with:
- Automated testing on every commit
- Code coverage reporting
- Build verification
- Java 21 LTS compatibility

---

## 📄 License

This project is part of a coding assessment and serves as a demonstration of:
- Clean Code principles
- Test-Driven Development (TDD)
- Modern Java features
- Comprehensive testing strategies
- Build automation with Gradle

**Built with ❤️ using Java 21 LTS and modern development practices.**

