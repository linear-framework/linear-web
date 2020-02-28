plugins {
  `java-library`
  scala
  `maven-publish`
}

group = "com.linearframework"
version = "0.1.1-SNAPSHOT"

repositories {
  jcenter()
  mavenCentral()
}

dependencies {
  implementation("org.scala-lang:scala-library:2.13.1")

  api("com.sparkjava:spark-core:2.9.1")

  testImplementation("junit:junit:4.13")
  testImplementation("org.scalatest:scalatest_2.13:3.1.1")
  testImplementation("org.scalatestplus:junit-4-12_2.13:3.1.1.0")
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