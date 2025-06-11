plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.kungnection"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // ✅ Spring Boot Starter Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // ✅ Lombok (重點)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // ✅ 測試
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")

    // ✅ JJWT - JSON Web Token (io.jsonwebtoken)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("startMysql") {
    doLast {
        exec {
            commandLine("docker", "run", "--name", "kungnection", "-e", "MYSQL_ROOT_PASSWORD=password", "-e", "MYSQL_DATABASE=kungnection", "-p", "3306:3306", "-d", "mysql:latest")
        }
    }
}