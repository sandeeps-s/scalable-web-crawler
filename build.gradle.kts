plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {

    implementation("org.jsoup:jsoup:1.12.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("com.github.tomakehurst:wiremock:2.24.1")
}

application {
    mainClassName = "com.scalecap.App"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.scalecap.App"
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
