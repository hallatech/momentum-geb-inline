buildscript {
	repositories {
	  mavenCentral()
	}
	dependencies {
	  classpath "org.gebish:geb-core:0.9.2"
	  classpath "org.seleniumhq.selenium:selenium-firefox-driver:2.38.0"
	}
  }
  
  import geb.Browser
  import org.openqa.selenium.firefox.FirefoxDriver
  
  def browser = new Browser(driver: new FirefoxDriver())
  
  // To execute: gradle build
  browser.go "http://localhost:8080"
  assert browser.page.title == "Spindrift Momentum — Welcome to the home of online shopping."
  
  
