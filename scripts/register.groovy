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

// Set base URL
browser.baseUrl = "http://localhost:8080"
def r = (new Random()).nextInt(10000)
def user=[
	'login':"superman${r}",
	'firstName':'Clark',
	'lastName':'Kent',
	'email':"superman${r}@krypton.com",
	'password':'Password1',
]
def rootTitle = "Spindrift Momentum — "
def homePageTitle = "${rootTitle}Welcome to the home of online shopping."
def registrationPageTitle = "${rootTitle}Register"
def welcomeMessage = "Welcome back!"
def userExistsMessage = "A user already exists with the login ${user.login}"


try {
	// Go to homepage - base Url
	browser.go()
	assert browser.page.title == homePageTitle
	
	// Go to register page
	browser.go "/register" 
	assert browser.page.title == registrationPageTitle
	assert browser.$('h1.page-title').text() == 'Registration'
	
	// Fill in registration form and submit
	browser.$('input', name:'login').value(user.login)
	browser.$('input', name:'firstName').value(user.firstName)
	browser.$('input', name:'lastName').value(user.lastName)
	browser.$('input', name:'email').value(user.email)
	browser.$('input', name:'password').value(user.password)
	browser.$('input', name:'confirmPassword').value(user.password)
	browser.$('input', name:'autoLogin').value(true)
	browser.$('input', type:'submit', value:'Register').click()
	
	//Confirm registration
	assert browser.page.title == homePageTitle
	// As the text is spread over a few lines just check it contains the name and the welcome string
	def messageToTest = browser.$('p', class:'primary-login-register').text()
	assert  messageToTest.contains('Clark') && messageToTest.contains(welcomeMessage)
	
	// Lets repeat and confirm a failure to re-register
	browser.go "/register"
	assert browser.page.title == registrationPageTitle
	
	// Fill in registration form and submit
	browser.$('input', name:'login').value(user.login)
	browser.$('input', name:'firstName').value(user.firstName)
	browser.$('input', name:'lastName').value(user.lastName)
	browser.$('input', name:'email').value(user.email)
	browser.$('input', name:'password').value(user.password)
	browser.$('input', name:'confirmPassword').value(user.password)
	browser.$('input', name:'autoLogin').value(true)
	browser.$('input', type:'submit', value:'Register').click()

	//Confirm registration failure - still on same page
	assert browser.page.title == registrationPageTitle
	def nav = browser.$('div', class:'content-container', role:'main').find('div', class:'content-layout-a').find('div',class:'content').find('span',0).find('ul',0).find('li',0).text() == userExistsMessage
	
}
finally {
	if (hasProperty('close')) {
		browser.close()
	}
}
  
