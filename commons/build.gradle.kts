plugins {
id("java-library")

}

group = "org.catanuniverse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation(project(":core"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
