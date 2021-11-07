plugins {
    id("java-library")
}

group = "org.catanuniverse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
