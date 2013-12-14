momentum-geb-inline
===================

Momentum User tests with Geb using inline scripting only via gradle

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

