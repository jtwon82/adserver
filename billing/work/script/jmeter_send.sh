#!/bin/sh

#nohup java -Xms4g -Xmx4g -XX:+UseG1GC -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8999 -Djava.rmi.server.hostname=192.168.2.79 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar /home/dreamsearch/mobon-kafka-batch-0.0.1-SNAPSHOT.jar >/dev/null 2>&1

nohup /home/dreamsearch/apache-jmeter-3.2/bin/jmeter -n -t /home/dreamsearch/apache-jmeter-3.2/bin/ThreadGroup1.jmx > /dev/null 2>&1

