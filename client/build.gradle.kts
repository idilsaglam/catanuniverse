plugins {
    id("application")
}

group = "org.catanuniverse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation(project(":commons"))
    implementation(project(":core"))
}

application {
    mainClass.set("org.catanuniverse.client.CatanUniverse")
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
