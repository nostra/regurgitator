#!/bin/sh
#
# Simple run script expecting that Maven 3.x and Java 8 is in your path.
#
mvn clean install
rm -f rm *-service/target/original*
java -jar *-service/target/*-service-*-SNAPSHOT.jar server *-app/src/main/resources/etc/*.yml