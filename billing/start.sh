#!/bin/sh

for j in $(ls *.jar -ltr | tail -1 | awk '{print $8}')
do
 nohup /usr/local/java/bin/java -Xms10g -Xmx10g -XX:+UseG1GC -server -jar $j >/dev/null 2>&1
done

