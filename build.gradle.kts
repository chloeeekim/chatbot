plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "1.9.25"
}

group = "chloe"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "1.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("com.h2database:h2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// queryDsl
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("jakarta.annotation:jakarta.annotation-api")

	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// QueryDsl
val queryDslSrcDir = "build/generated/querydsl"

sourceSets {
	named("main") {
		java.srcDir(queryDslSrcDir)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.generatedSourceOutputDirectory.set(file(queryDslSrcDir))
}

tasks.withType<Delete> {
	delete(file(queryDslSrcDir))
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	create("querydsl") {
		extendsFrom(configurations.compileClasspath.get())
	}
}