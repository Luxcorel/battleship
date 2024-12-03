plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Run project"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.example.Controller.Main")
}

tasks.test {
    useJUnitPlatform()
}