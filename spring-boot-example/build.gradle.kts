plugins {
    `java-library`
    id("org.springframework.boot") version "2.2.4.RELEASE"
}

repositories {
    jcenter()
}

dependencies {

    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.2.4.RELEASE"))

    implementation("org.springframework.boot:spring-boot-loader")
}

springBoot {
    mainClassName = "io.github.fp7.jvmremote.example.Main"
}