Introduction
==============

This is an example application of my blog entries that describe how you can integration Spring Social and
Spring Security. This example application uses

* Spring Framework 3.2.X
* Spring Social 1.1.0
* Spring Security 3.2.0


Prerequisites
===============

This example application has the following software prerequisites:

* JDK 7
* Maven 3
* MySQL 5.6.X

You also have to create a Twitter application which is used to sign in the user by using Twitter. You can get
more information about this from these website:

* [Implementing Sign in with Twitter](https://dev.twitter.com/docs/auth/implementing-sign-twitter).
* [Browser sign in flow](https://dev.twitter.com/docs/browser-sign-flow).

Running The Example Application
================================

This section describes the steps which are required to run the example application.

Preparations
--------------

* Ensure that you have installed all required software.
* Ensure that you have created a Twitter application used with this example.
* Create a MySQL database for the example application.

Configuration
---------------

You can configure the application by following these steps:

1.  Configure the database connection. The database connection is configured in the file *profiles/dev/config.properties*.

    db.driver=com.mysql.jdbc.Driver
    db.url=jdbc:mysql://localhost:3306/socialtwitter
    db.username=socialtwitter
    db.password=password

2.  Configure the Twitter application. You can configure your twitter application by creating a file
    *profiles/dev/socialConfig.properties*. The contents of this file looks as follows:

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

Running Tests
================

1.  You can run unit tests by using this command at the command prompt:

    mvn test -P dev
