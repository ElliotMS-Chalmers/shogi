plugins {
    application
	jacoco
	id("org.openjfx.javafxplugin") version "0.1.0"
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Jackson for JSON parsing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.1")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "App"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.12"	
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

javafx {
	version = "20"
	modules = listOf("javafx.controls", "javafx.media")
}

tasks.named<Javadoc>("javadoc") {
    isFailOnError = false // Fail the build on Javadoc errors
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        charSet = "UTF-8"
        links("https://docs.oracle.com/en/java/javase/11/docs/api/") // Link to JDK API docs
        exclude("App.java", "controller", "view", "util")
        title = "Shogi"
    }
}
