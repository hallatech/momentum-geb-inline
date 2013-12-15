momentum-geb-inline
===================

Momentum User tests with Geb using inline scripting only via gradle.

Simple inline scripts using gradle to test Momentum with GEB. This version only uses inline groovy scripts for POC.

The tests assume you have Momentum Storefront running on localhost on port 8080, either local on your host or in a VM with port forwarding.

Includes the gradle wrapper if you don't have gradle installed.

The default gradle task is 'test' so you don't need to include an actual task.

The following command will launch the tests with firefox browser only:

    ./gradlew test
    ./gradlew

or if you have gradle already installed:

    gradle test
    gradle
    
The defaults will leave the browser windows open where the test completes. To close the browser after each test add the -Pclose option

	./gradlew -Pclose
	gradle -Pclose
    
There is no re-usability between scripts as its just a POC before we move on to Page Objects,  re-usability and configuration injection.

Note: There is a random function on the email/login so the tests can be run multiple times without having to reset the database.

Notice in the scripts scope for
- ATG generic objects (profile, category, product, order, price)
- Site Builder objects (page) - For functional test the other objects are not so much required, as the tests are fairly simple to construct,
and they all follow the general pattern of 1.go, 2.find, 3.assert.
Possibly for data validation we can create a more intricate DSL extension but its basically not even required for straight forward user tests.
Scope for default pages to be generated from the data sources, embedded with their own site specific URLs
- Re-use of such things as price validation e.g. currency, major, minor to match the mustache template configuration
- Re-use of partial functionality - e.g. login
- Nice easy way to have a suite of checkout tests given a collection of baskets/carts
- Easily testing validation - how much time will this save ??!!

Note: the most challenging of all of this is the jQuery-ish find/filter facilities.
Something we have to learn but not difficult and many ways to achieve the same end goal.

So onto level-2: momentum-geb-pom (Page Object Model)


