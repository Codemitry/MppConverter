plugins {
    java
    kotlin("jvm") version "1.4.20"
}

group = "me.sokolov.mppconverter.codeseparator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://dl.google.com/dl/android/maven2")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    compile("com.android.tools.external.com-intellij:intellij-core:27.1.3")
    implementation(project(":PSICreator"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}