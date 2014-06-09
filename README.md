Date4JPrecise
=============
A more precise implementation of the date4J library. Supporting unto nano second precision. This package is built using gradle. To get a working jar run:
 
  $ gradle buildJar
    
The jar file would be located in the repos directory and named as date4jprecise.jar.

The [date4j](http://www.date4j.net/) library is an alternative to Date, Calendar, and related Java classes. The JDK's treatment of dates can't handle a few things well.

The main goals of date4j are:

* Easy manipulation of dates/times in the Gregorian calendar (the civil calendar used in almost all countries).
* Easy storage and retrieval of such dates/times from a relational database.
* A simplified model of civil timekeeping, similar to the model used by many databases.

However, the problem with date4J is that it doesn't handle times to the precision of nano seconds. This might be useful in certain simulation scenarious where nano seconds matter.

Here are a few examples to use this library:

```java
DateTime now = DateTime.now(TimeZone.getDefault());
now = now.plusPreciseNano(0, 0, 0, 0, 0, 10,123456789, DateTime.DayOverflow.Spillover);
