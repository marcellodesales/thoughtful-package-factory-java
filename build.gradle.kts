plugins {
    id("java")
    id("application")
    id("jacoco")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

tasks.withType<JavaExec> {
    standardOutput = System.out
    errorOutput = System.err
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.google.guava:guava:33.4.8-jre")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    
    // Generate test reports
    reports {
        html.required.set(true)
        junitXml.required.set(true)
    }
    
    // Show test results in console
    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
    
    // Always run tests (don't use cached results)
    outputs.upToDateWhen { false }
    
    // Ensure test task finalizes with coverage report
    finalizedBy(tasks.jacocoTestReport)
}

// JaCoCo configuration
jacoco {
    toolVersion = "0.8.12"
}

// Configure JaCoCo test report
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    
    // Exclude Main class from coverage analysis
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/Main.class")
                exclude("**/Main$*.class")
            }
        })
    )
    
    finalizedBy("printCoverage")
}

// Configure JaCoCo coverage verification
tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/Main.class")
                exclude("**/Main$*.class")
            }
        })
    )
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}


// Task to print coverage summary to console
tasks.register("printCoverage") {
    dependsOn("jacocoTestReport")
    group = "verification"
    description = "Prints code coverage summary to console"
    
    doLast {
        val xmlFile = file("${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
        if (xmlFile.exists()) {
            println("\n" + "=".repeat(80))
            println("📊 CODE COVERAGE REPORT")
            println("=".repeat(80))
            
            val xmlContent = xmlFile.readText()
            
            // Extract all class elements with their coverage data
            val classPattern = """<class name="([^"]+)"[^>]*>(.*?)</class>""".toRegex(RegexOption.DOT_MATCHES_ALL)
            val classMatches = classPattern.findAll(xmlContent)
            
            val classData = mutableListOf<Triple<String, Map<String, Pair<Int, Int>>, Double>>()
            var totalInstructions = 0 to 0
            var totalBranches = 0 to 0
            var totalLines = 0 to 0
            
            classMatches.forEach { classMatch ->
                val fullClassName = classMatch.groupValues[1].replace('/', '.')
                val classContent = classMatch.groupValues[2]
                
                // Extract counters for this class
                val counterPattern = """<counter type="([^"]+)" missed="([^"]+)" covered="([^"]+)"/>""".toRegex()
                val counters = counterPattern.findAll(classContent).associate { match ->
                    val type = match.groupValues[1]
                    val missed = match.groupValues[2].toInt()
                    val covered = match.groupValues[3].toInt()
                    type to (missed to covered)
                }
                
                // Calculate overall class coverage (weighted by instructions)
                val instrCoverage = counters["INSTRUCTION"]?.let { (missed, covered) ->
                    val total = missed + covered
                    if (total > 0) covered.toDouble() / total * 100 else 0.0
                } ?: 0.0
                
                classData.add(Triple(fullClassName, counters, instrCoverage))
                
                // Accumulate totals
                counters["INSTRUCTION"]?.let { (missed, covered) ->
                    totalInstructions = (totalInstructions.first + missed) to (totalInstructions.second + covered)
                }
                counters["BRANCH"]?.let { (missed, covered) ->
                    totalBranches = (totalBranches.first + missed) to (totalBranches.second + covered)
                }
                counters["LINE"]?.let { (missed, covered) ->
                    totalLines = (totalLines.first + missed) to (totalLines.second + covered)
                }
            }
            
            // Sort classes by coverage (lowest first)
            val sortedClasses = classData.sortedBy { it.third }
            
            // Print individual file coverage
            println("\n📄 INDIVIDUAL FILE COVERAGE:")
            println("-".repeat(80))
            println(String.format("%-50s %12s %12s %12s", "Class", "Instructions", "Branches", "Lines"))
            println("-".repeat(80))
            
            sortedClasses.forEach { (className, counters, _) ->
                val instrStats = counters["INSTRUCTION"]?.let { (missed, covered) ->
                    val total = missed + covered
                    val percentage = if (total > 0) covered.toDouble() / total * 100 else 0.0
                    String.format("%5.1f%% (%d/%d)", percentage, covered, total)
                } ?: "N/A"
                
                val branchStats = counters["BRANCH"]?.let { (missed, covered) ->
                    val total = missed + covered
                    val percentage = if (total > 0) covered.toDouble() / total * 100 else 0.0
                    String.format("%5.1f%% (%d/%d)", percentage, covered, total)
                } ?: "N/A"
                
                val lineStats = counters["LINE"]?.let { (missed, covered) ->
                    val total = missed + covered
                    val percentage = if (total > 0) covered.toDouble() / total * 100 else 0.0
                    String.format("%5.1f%% (%d/%d)", percentage, covered, total)
                } ?: "N/A"
                
                println(String.format("%-50s %12s %12s %12s", 
                    className.take(50), instrStats, branchStats, lineStats))
            }
            
            // Print overall summary
            println("-".repeat(80))
            println("\n📊 OVERALL SUMMARY:")
            
            val (missedInstr, coveredInstr) = totalInstructions
            val totalInstr = missedInstr + coveredInstr
            val instrPercentage = if (totalInstr > 0) coveredInstr.toDouble() / totalInstr * 100 else 0.0
            println("📋 Instructions: ${String.format("%.1f%%", instrPercentage)} ($coveredInstr/$totalInstr)")
            
            val (missedBranch, coveredBranch) = totalBranches
            val totalBranch = missedBranch + coveredBranch
            val branchPercentage = if (totalBranch > 0) coveredBranch.toDouble() / totalBranch * 100 else 0.0
            println("🌿 Branches:     ${String.format("%.1f%%", branchPercentage)} ($coveredBranch/$totalBranch)")
            
            val (missedLine, coveredLine) = totalLines
            val totalLine = missedLine + coveredLine
            val linePercentage = if (totalLine > 0) coveredLine.toDouble() / totalLine * 100 else 0.0
            println("📄 Lines:       ${String.format("%.1f%%", linePercentage)} ($coveredLine/$totalLine)")
            
            println("\n📁 HTML Report: ${layout.buildDirectory.get()}/reports/jacoco/test/html/index.html")
            println("=".repeat(80) + "\n")
        } else {
            println("⚠️  No coverage data found. Run 'gradle test jacocoTestReport' first.")
        }
    }
}

// Task to run tests with coverage
tasks.register("testWithCoverage") {
    dependsOn("jacocoTestReport")
    group = "verification"
    description = "Runs tests and generates code coverage reports"
}

// Task to run the CLI application with help
tasks.register<JavaExec>("runCli") {
    dependsOn("classes")
    group = "application"
    description = "Runs the package classifier CLI application with help"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ai.thoughtful.platform.factory.PackageSorterApplication")
    args = listOf("--help")
}

// Task to run the CLI with custom arguments
tasks.register<JavaExec>("sort") {
    dependsOn("classes")
    group = "application"
    description = "Classifies a package. Usage: gradle sort -Pargs='width,height,length,mass'"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ai.thoughtful.platform.factory.PackageSorterApplication")
    
    if (project.hasProperty("args")) {
        args = listOf(project.property("args").toString())
    } else {
        args = listOf("--help")
    }
}

// Configure application main class
application {
    mainClass.set("ai.thoughtful.platform.factory.PackageSorterApplication")
}

// Task to build executable JAR
tasks.register("buildCli") {
    dependsOn("bootJar")
    group = "build"
    description = "Builds the executable CLI JAR file"
    doLast {
        println("\n✅ CLI JAR built successfully!")
        println("📦 Location: ${layout.buildDirectory.get()}/libs/${project.name}-${project.version}.jar")
        println("\n🚀 Usage:")
        println("  java -jar build/libs/${project.name}-${project.version}.jar \"50,30,20,5000\"")
        println("  java -jar build/libs/${project.name}-${project.version}.jar --help")
    }
}
