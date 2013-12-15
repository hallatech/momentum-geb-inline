buildscript {
	repositories {
	  mavenCentral()
	}
	dependencies {
	  classpath "org.gebish:geb-core:0.9.2"
	  classpath "org.seleniumhq.selenium:selenium-firefox-driver:2.38.0"
		classpath 'org.apache.commons:commons-lang3:3.1'
	}
}
  
import geb.Browser
import org.openqa.selenium.firefox.FirefoxDriver
import org.apache.commons.lang3.StringEscapeUtils
import java.util.Random

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
	'address1':'1 High Street',
	'address2':'',
	'city':'London',
	'postCode':'SW1 1AB',
	'country':'England',
	'phone':'0121 1234567'
]
def card=[
	'cardNumber':'4111111111111111',
	'expiryMonth':'12',
	'expiryYear':'2015',
	'ccv':'123'
]
def category=[
	'id':'iccat575',
	'displayName':'Digital Cameras',
	'urlName':'digital-cameras',
	'pageTitle':'Buy from our Digital Cameras range',
]
def product=[
	'id':'icprd3888360',
	'displayName':'5D MARK II + EF 24-70mm',
	'urlName':'5d-mark-ii-ef-24-70mm',
	'pageTitle':'',
]
def orderTotalPrice=[
	'currency':'&pound;',
	'major':'3,689',
	'minor':'.10'
]

def rootTitle = "Spindrift Momentum — "
def homePageTitle = "${rootTitle}Welcome to the home of online shopping."
def categoryUrl = "/category/${category.urlName}/${category.id}/"
def categoryPageTitle = "${rootTitle}${category.pageTitle}"
def productUrl = "/product/${product.urlName}/${product.id}"
def productPageTitle = "${rootTitle}${product.displayName} in ${category.displayName}"
def basketUrl = '/basket'
def basketPageTitle = "${rootTitle}Shopping Basket"
def checkoutLoginPagetTitle = "${rootTitle}Login"
def checkoutRootTitle = "Checkout - "
def deliveryPageTitle = "${rootTitle}${checkoutRootTitle}Delivery Details"
def deliveryAddressPageTitle= "${rootTitle}${checkoutRootTitle}Delivery Address"
def paymentPageTitle = "${rootTitle}${checkoutRootTitle}Payment Details"
def confirmPageTitle = "${rootTitle}${checkoutRootTitle}Order Confirmation"


try {
	// Go to homepage - base Url
	browser.go()
	assert browser.page.title == homePageTitle
	
	// Go to category page
	browser.go categoryUrl
	assert browser.page.title == categoryPageTitle
	assert browser.$('h1.page-title').text() == category.displayName
	
	// Find the product in the gallery using the tile
	def nav =  browser.$('div',class:'product-tile').find('div',class:'tile-product-core').find('h4').find('a', text: product.displayName)
	assert nav.size() == 1
	
	//Got to PDP
	nav.click()
	assert browser.page.title == productPageTitle
	assert browser.$('div',class:'product-detail').find('h1').text() == product.displayName
	
	//Add to basket
	browser.$('input', type:'submit', id:'add-to-basket').click()
	
	//Goto Basket
	browser.go basketUrl
	assert browser.page.title == basketPageTitle
	
	//Confirm total order price
	def totalOrderPriceElement =  browser.$('div', class:'totals').find('p',class:'line grand-total').find('span',class:'price').find('span',class:'price-amount')
	assert totalOrderPriceElement.find('span',class:'price-major').text() == orderTotalPrice.major
	assert totalOrderPriceElement.find('span',class:'price-minor').text() == orderTotalPrice.minor
	assert totalOrderPriceElement.find('span',class:'price-currency').text() == StringEscapeUtils.unescapeHtml4(orderTotalPrice.currency)
	
	//Go to checkout
	browser.$('input', type:'submit', id:'btn-checkout').click()
	assert browser.page.title == checkoutLoginPagetTitle
	// Continue as anonymous user using the correct form as there are multiple forms on page with same fields
	def checkoutForm = browser.$('form', id:'guestCheckout')
	checkoutForm.find('input', name:'email').value(user.email)
	checkoutForm.find('input', type:'submit', value:'Begin checkout').click()
	assert browser.page.title == deliveryPageTitle
	
	//Add address
	browser.$('a',class:'add-address').click()
	assert browser.page.title == deliveryAddressPageTitle
	//Fill in address and submit. Form ids have embedded dynamic repository item id, so we need to match
	nav = browser.$('form', id:'frm-checkout-form-delivery-address').find('select')
	def field = nav.findAll { it.getAttribute('id').contains('frm-title') }
	assert field.size() == 1
	field.find('option',value:'Mr').click()
	
  nav = browser.$('form', id:'frm-checkout-form-delivery-address').find('input')
	field = nav.findAll { it.getAttribute('id').contains('frm-first-name') }
	field.value(user.firstName)
	field = nav.findAll { it.getAttribute('id').contains('frm-last-name') }
	field.value(user.lastName)
	field = nav.findAll { it.getAttribute('id').contains('frm-postcode') }
	field.value(user.postCode)
	//The address field does not have an id and its name partially matched, matches the corresponding hidden field
	field = nav.findAll { it.getAttribute('name').contains('frm-address-1') && !it.getAttribute('type').equals('hidden') }
	field.value(user.address1)
	
	field = nav.findAll { it.getAttribute('id').contains('frm-city') }
	field.value(user.city)
	field = nav.findAll { it.getAttribute('id').contains('frm-phone-num') }
	field.value(user.phone)
	
	browser.$('form', id:'frm-checkout-form-delivery-address').find('input',type:'submit', value:'Submit').click()
	assert browser.page.title == deliveryPageTitle
	
	//Proceed to next stage
	browser.$('input',id:'btn-update-shipping-continue',type:'submit').click()
	assert browser.page.title == paymentPageTitle
	
	//Enter payment details
	browser.$('form', id:'paymentMulti').find('input',id:'creditCardNumber').value(card.cardNumber)
	browser.$('form', id:'paymentMulti').find('input',id:'cardholderName').value("${user.firstName} ${user.lastName}")
	browser.$('form', id:'paymentMulti').find('select',id:'frm-expiry-date').find('option',value:card.expiryMonth).click()
	browser.$('form', id:'paymentMulti').find('select',id:'frm-expiry-year').find('option',value:card.expiryYear).click()
	browser.$('form', id:'paymentMulti').find('input',id:'cardVerificationNumber').value(card.ccv)
	browser.$('form', id:'paymentMulti').find('input',type:'submit',value:'Place your order').click()
	
	//Confirm order
	assert browser.page.title == confirmPageTitle
	// wait for 2 seconds so we can see the order
	synchronized (browser) {
		try {
			browser.wait(2000)
		}
		catch(Exception e) { 
			// we don't care so swallow exception
		}
	}
	//Finally return to home page
	browser.$('a', text:'Continue Shopping').click()
	assert browser.page.title == homePageTitle
}
finally {
	if (hasProperty('close')) {
		browser.close()
	}
}
  
