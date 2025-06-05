# 📦 Package Classifier CLI Usage

## 🚀 Running from Gradle

### Show Help
```bash
./gradlew runCli
```

### sort a Package
```bash
./gradlew sort -Pargs="width,height,length,mass"
```

**Examples:**
```bash
# Standard package
./gradlew sort -Pargs="50,30,20,5000"
# Output: STANDARD

# Bulky package (dimension >= 150cm)
./gradlew sort -Pargs="150,30,20,5000"
# Output: SPECIAL

# Heavy package (weight >= 20kg)
./gradlew sort -Pargs="50,30,20,25000"
# Output: SPECIAL

# Rejected package (both bulky and heavy)
./gradlew sort -Pargs="150,30,20,25000"
# Output: REJECTED

# Bulky by volume (100^3 = 1,000,000 cm³)
./gradlew sort -Pargs="100,100,100,15000"
# Output: SPECIAL
```

## 📦 Running Standalone JAR

### Build the JAR
```bash
./gradlew buildCli
```

### Run the JAR
```bash
# Show help
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar --help

# sort packages
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar "50,30,20,5000"
java -jar build/libs/packages-factory-1.0-SNAPSHOT.jar "150,30,20,25000"
```

## 📋 Input Format

**Required format:** `"width,height,length,mass"`

- **width** - Package width in centimeters (positive integer)
- **height** - Package height in centimeters (positive integer) 
- **length** - Package length in centimeters (positive integer)
- **mass** - Package mass in grams (positive number, can be decimal)

## 📤 Output

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

## 🎯 Testing with Unit Test Cases

The CLI uses the same format as the unit tests, so you can directly test any test case:

```bash
# From PackageFactoryTest cases
./gradlew sort -Pargs="50,50,50,10000"     # STANDARD
./gradlew sort -Pargs="100,50,30,15000"    # STANDARD  
./gradlew sort -Pargs="99,99,99,19999"     # STANDARD
./gradlew sort -Pargs="150,10,10,5000"     # SPECIAL (bulky)
./gradlew sort -Pargs="10,10,10,25000"     # SPECIAL (heavy)
./gradlew sort -Pargs="150,150,150,25000"  # REJECTED (both)
```

