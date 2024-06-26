plugins {
    java
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.bothq.core"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // handles oauth authentication with discord
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // we still use the servlet way of handling things, however we need the webflux WebClient to use the OAuth Client
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // implementation("com.github.ben-manes.caffeine:caffeine")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.bothq.lib:bothq-lib")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
