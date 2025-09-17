plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "lab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // Spring WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Spring Framework 핵심 의존성
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter")

    // Kotlin 리플렉션
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Kafka (Webflux)
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("org.mockito:mockito-inline:5.2.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
