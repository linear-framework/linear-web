plugins {
  `java-library`
  scala
  `maven-publish`
}

group = "com.linearframework"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
  jcenter()
  mavenCentral()
}

dependencies {
  implementation("org.scala-lang:scala-library:2.13.2")
  implementation("org.scala-lang:scala-reflect:2.13.2")

  api("com.sparkjava:spark-core:2.9.1")
  api("org.reflections:reflections:0.9.12")
  api("com.fasterxml.jackson.module:jackson-module-scala_2.13:2.11.0")
  api("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.0")

  testImplementation("junit:junit:4.13")
  testImplementation("org.scalatest:scalatest_2.13:3.1.2")
  testImplementation("org.scalatestplus:junit-4-12_2.13:3.1.2.0")
  testImplementation("com.google.code.gson:gson:2.8.6")

  testRuntimeOnly("org.slf4j:slf4j-simple:1.7.25")
}

tasks.named<Jar>("jar") {
  from(sourceSets["main"].output)
  from(sourceSets["main"].allSource)
}

publishing {
  repositories {
    maven {
      name = "LinearWeb"
      url = uri("https://maven.pkg.github.com/linear-framework/linear-web")
      credentials {
        username = System.getenv("GITHUB_USER")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("PublishToGithub") {
      artifactId = "web"
      from(components["java"])
    }
  }
}