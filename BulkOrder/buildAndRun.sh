#!/bin/sh
mvn clean package && docker build -t de.franden.opentracing/BulkOrder .
docker rm -f BulkOrder || true && docker run -d -p 8080:8080 -p 4848:4848 --name BulkOrder de.franden.opentracing/BulkOrder 
