Introduction
==============

This is an example application of my blog entries that describe how you can integration Spring Social and
Spring Security. These blog posts are:

* [Adding Social Sign In to a Spring MVC Web Application: Configuration](http://www.petrikainulainen.net/programming/spring-framework/adding-social-sign-in-to-a-spring-mvc-web-application-configuration/)
* [Adding Social Sign In to a Spring MVC Web Application: Registration and Login](http://www.petrikainulainen.net/programming/spring-framework/adding-social-sign-in-to-a-spring-mvc-web-application-registration-and-login/)


This example application uses

* Spring Framework 3.2.X
* Spring Social 1.1.0
* Spring Security 3.2.0


Prerequisites
===============

This example application has the following software prerequisites:

* JDK 7
* Maven 3
* MySQL 5.6.X
* Twitter application (Enable the "allow this application to be used to Sign in with Twitter" checkbox)
* Facebook application (Select "website with Facebook login" when you are asked how your application integrates with FB)

You can create the required applications by following these links:

* [Facebook Developers](https://developers.facebook.com/)
* [Twitter Developers](https://dev.twitter.com/)

If you don't know how to create these applications, read the following tutorials:

* [Facebook Developers - Creating an app details page](https://developers.facebook.com/docs/guides/appcenter/#creating)
* [How to Create a Twitter App in 8 Easy Steps](http://iag.me/socialmedia/how-to-create-a-twitter-app-in-8-easy-steps/)

Running The Example Application
================================

This section describes the steps which are required to run the example application.

Preparations
--------------

* Ensure that you have installed all required software.
* Ensure that you have created a Facebook and a Twitter application used with this example.
* Create a MySQL database for the example application.

Configuration
---------------

You can configure the application by following these steps:

1.  Configure the database connection. The database connection is configured in the file *profiles/dev/config.properties*.

        db.driver=com.mysql.jdbc.Driver
        db.url=jdbc:mysql://localhost:3306/socialtwitter
        db.username=socialtwitter
        db.password=password

2.  Configure the Facebook application. You can configure your Facebook application by creating a file
    *profiles/dev/socialConfig.properties*. The content of this file looks as follows:

        facebook.app.id=foo
        facebook.app.secret=bar

3.  Configure the Twitter application. You can configure your twitter application by adding the following
    configuration to the file *profiles/dev/socialConfig.properties*:

        twitter.consumer.key=foo
        twitter.consumer.secret=bar

Initializing the database
---------------------------

You can initialize the database by running the following command at the command prompt:

    mvn liquibase:update -P dev

Running The Application
-------------------------

You can run the application by running the following command at the command prompt:

    mvn jetty:run -P dev

If you want to deploy the application to Tomcat, you have to use Tomcat 7 or newer.

Running Tests
================

1.  You can run unit tests by running the following command at the command prompt:

        mvn test -P dev
